package net.intothevoid.energyqa;

import com.google.gson.GsonBuilder;

import net.minecraft.commands.Commands;
import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.transfer.energy.*;
import net.neoforged.neoforge.transfer.transaction.*;

import owmii.powah.block.cable.*;

import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;

/** Excluded, loopback-only tests. No production connection or copied player data. */
@Mod("itv_energy_qa")
public class EnergyQA {
    static final Map<BlockPos, SimpleEnergyHandler> SINKS = new HashMap<>();
    static long visits;

    static class Cable extends CableBlockEntity {
        boolean active = true;

        Cable(BlockPos p, ServerLevel level) {
            super(
                    p,
                    owmii.powah.block.Blcks.ENERGY_CABLE
                            .get(owmii.powah.block.Tier.STARTER)
                            .defaultBlockState());
            setLevel(level);
            getEnergy().setCapacity(10000);
            getEnergy().setTransfer(10000);
        }

        @Override
        public boolean isActive() {
            visits++;
            return active;
        }

        @Override
        public boolean checkRedstone() {
            return true;
        }

        @Override
        public boolean canReceiveEnergy(Direction side) {
            return true;
        }

        @Override
        public boolean canExtractEnergy(Direction side) {
            return true;
        }

        void cursor(int n) {
            startIndex = n;
        }

        int cursor() {
            return startIndex;
        }
    }

    static class CustomCable extends Cable {
        final List<CableBlockEntity> traversal;

        CustomCable(BlockPos pos, ServerLevel level, List<CableBlockEntity> traversal) {
            super(pos, level);
            this.traversal = traversal;
        }

        @Override
        protected Iterable<CableBlockEntity> getCables() {
            return traversal;
        }
    }

    public EnergyQA(IEventBus bus) {
        if (!Boolean.getBoolean("itv.energy.fixture"))
            throw new IllegalStateException("Local fixture flag required");
        bus.addListener(this::capabilities);
        NeoForge.EVENT_BUS.addListener(this::commands);
    }

    void capabilities(RegisterCapabilitiesEvent e) {
        e.registerBlockEntity(
                Capabilities.Energy.BLOCK,
                BlockEntityType.BARREL,
                (be, side) -> SINKS.get(be.getBlockPos()));
    }

    static void require(boolean v, String message) {
        if (!v) throw new AssertionError(message);
    }

    static List<Cable> network(ServerLevel level, int size) throws Exception {
        var list = new ArrayList<Cable>();
        for (int i = 0; i < size; i++) list.add(new Cable(new BlockPos(i * 2, 80, 30), level));
        Constructor<CableNet> ctor = CableNet.class.getDeclaredConstructor(List.class);
        ctor.setAccessible(true);
        Object net = ctor.newInstance(list);
        Field field = CableBlockEntity.class.getDeclaredField("net");
        field.setAccessible(true);
        Field guard = CableBlockEntity.class.getDeclaredField("netInsertionGuard");
        guard.setAccessible(true);
        var shared = new org.apache.commons.lang3.mutable.MutableBoolean();
        for (Cable c : list) {
            field.set(c, net);
            guard.set(c, shared);
        }
        return list;
    }

    void commands(RegisterCommandsEvent e) {
        e.getDispatcher()
                .register(
                        Commands.literal("energylagqa")
                                .requires(
                                        s ->
                                                s.getEntity() == null
                                                        && s.getServer()
                                                                .getLocalIp()
                                                                .equals("127.0.0.1"))
                                .executes(
                                        ctx -> {
                                            var out = new LinkedHashMap<String, Object>();
                                            var cases = new ArrayList<String>();
                                            try {
                                                var server = ctx.getSource().getServer();
                                                require(
                                                        server.getPlayerList()
                                                                .getPlayers()
                                                                .isEmpty(),
                                                        "No players permitted");
                                                ServerLevel level = server.overworld();
                                                var cables = network(level, 12);
                                                Cable input = cables.getFirst();
                                                // Distinct sided native handlers let ordering and
                                                // actual committed energy be observed.
                                                for (int i = 0; i < 12; i++) {
                                                    Cable c = cables.get(i);
                                                    BlockPos sink = c.getBlockPos().east();
                                                    level.setBlock(
                                                            sink,
                                                            Blocks.BARREL.defaultBlockState(),
                                                            3);
                                                    SINKS.put(
                                                            sink,
                                                            new SimpleEnergyHandler(
                                                                    10000, 10000, 10000));
                                                    level.invalidateCapabilities(sink);
                                                    c.energySides.add(Direction.EAST);
                                                }
                                                var allocations = new ArrayList<List<Long>>();
                                                for (int cursor = 0; cursor < 24; cursor++) {
                                                    SINKS.values().forEach(s -> s.set(0));
                                                    input.cursor(cursor);
                                                    try (var tx = Transaction.openRoot()) {
                                                        long n =
                                                                input.insertEnergy(
                                                                        50, tx, Direction.NORTH);
                                                        require(n == 50, "Transfer amount");
                                                        tx.commit();
                                                    }
                                                    var row =
                                                            cables.stream()
                                                                    .map(
                                                                            c ->
                                                                                    SINKS.get(
                                                                                                    c.getBlockPos()
                                                                                                            .east())
                                                                                            .getAmountAsLong())
                                                                    .toList();
                                                    require(
                                                            row.get(cursor % 12) == 50,
                                                            "Round robin changed");
                                                    require(
                                                            row.stream()
                                                                            .mapToLong(
                                                                                    Long::longValue)
                                                                            .sum()
                                                                    == 50,
                                                            "Energy conservation");
                                                    allocations.add(row);
                                                }
                                                cases.add(
                                                        "24_round_robin_offsets_and_exact_energy_conservation");
                                                out.put("allocations", allocations);
                                                SINKS.values().forEach(s -> s.set(0));
                                                input.cursor(5);
                                                try (var tx = Transaction.openRoot()) {
                                                    require(
                                                            input.insertEnergy(
                                                                            50, tx, Direction.NORTH)
                                                                    == 50,
                                                            "Abort setup");
                                                }
                                                require(
                                                        SINKS.values().stream()
                                                                .allMatch(
                                                                        s ->
                                                                                s.getAmountAsLong()
                                                                                        == 0),
                                                        "Abort leaked energy");
                                                require(input.cursor() == 5, "Abort leaked cursor");
                                                cases.add(
                                                        "abort_restores_energy_and_round_robin_cursor");
                                                SINKS.values().forEach(s -> s.set(0));
                                                var reverse =
                                                        new ArrayList<CableBlockEntity>(cables);
                                                Collections.reverse(reverse);
                                                var custom =
                                                        new CustomCable(
                                                                input.getBlockPos(),
                                                                level,
                                                                reverse);
                                                try (var tx = Transaction.openRoot()) {
                                                    require(
                                                            custom.insertEnergy(
                                                                            50, tx, Direction.NORTH)
                                                                    == 50,
                                                            "Override transfer");
                                                    tx.commit();
                                                }
                                                require(
                                                        SINKS.get(
                                                                                cables.getLast()
                                                                                        .getBlockPos()
                                                                                        .east())
                                                                        .getAmountAsLong()
                                                                == 50,
                                                        "Subclass traversal override ignored");
                                                cases.add("subclass_custom_traversal_preserved");

                                                // A large network with no external consumers
                                                // reproduces the full-network traversal.
                                                var big = network(level, 8192);
                                                Cable source = big.getFirst();
                                                long[] timings = new long[5];
                                                for (int round = -2; round < 5; round++) {
                                                    long began = System.nanoTime();
                                                    for (int i = 0; i < 1000; i++)
                                                        try (var tx = Transaction.openRoot()) {
                                                            require(
                                                                    source.insertEnergy(
                                                                                    100,
                                                                                    tx,
                                                                                    Direction.NORTH)
                                                                            == 0,
                                                                    "Empty network accepted"
                                                                        + " energy");
                                                            tx.commit();
                                                        }
                                                    if (round >= 0)
                                                        timings[round] = System.nanoTime() - began;
                                                }
                                                cases.add(
                                                        "large_empty_network_native_transfer_benchmark");
                                                out.put(
                                                        "benchmark_nanos_1000_transfers_8192_cables",
                                                        timings);
                                                out.put("phase", "passed");
                                            } catch (Throwable error) {
                                                out.put("phase", "failed");
                                                out.put("error", error.toString());
                                                error.printStackTrace();
                                            }
                                            out.put("checks", cases);
                                            out.put("production_actions", 0);
                                            try {
                                                Files.writeString(
                                                        Path.of("energy-qa.json"),
                                                        new GsonBuilder()
                                                                .setPrettyPrinting()
                                                                .create()
                                                                .toJson(out));
                                            } catch (Exception x) {
                                                throw new RuntimeException(x);
                                            }
                                            ctx.getSource()
                                                    .sendSuccess(
                                                            () ->
                                                                    Component.literal(
                                                                            "ENERGY_QA "
                                                                                    + out.get(
                                                                                            "phase")),
                                                            false);
                                            return 1;
                                        }));
    }
}
