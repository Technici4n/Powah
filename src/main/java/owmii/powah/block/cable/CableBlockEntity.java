package owmii.powah.block.cable;

import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jspecify.annotations.Nullable;
import owmii.powah.block.Tiles;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.PowahBaseEnergyStorageBlockEntity;

public class CableBlockEntity extends PowahBaseEnergyStorageBlockEntity<CableBlock> implements IInventoryHolder {

    /**
     * Tag-Name used for synchronizing connected sides to the client.
     */
    private static final String NBT_ENERGY_SIDES = "cs";

    public final EnumSet<Direction> energySides = EnumSet.noneOf(Direction.class);
    @Nullable
    CableNet net = null;
    /**
     * True when energy is being inserted into the network.
     * Must be called after {@link #getCables()} to make sure that it is up-to-date for the network.
     */
    protected MutableBoolean netInsertionGuard = new MutableBoolean(false);
    protected int startIndex = 0;
    private final SnapshotJournal<Integer> startIndexJournal = new SnapshotJournal<>() {
        @Override
        protected Integer createSnapshot() {
            return startIndex;
        }

        @Override
        protected void revertToSnapshot(Integer snapshot) {
            startIndex = snapshot;
        }
    };
    @SuppressWarnings("unchecked")
    private final BlockCapabilityCache<EnergyHandler, @Nullable Direction>[] capabilityCaches = new BlockCapabilityCache[6];

    public CableBlockEntity(BlockPos pos, BlockState state) {
        super(Tiles.CABLE.get(), pos, state);
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        CableNet.addCable(this);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        CableNet.removeCable(this);
    }

    public boolean isActive() {
        if (getLevel() instanceof ServerLevel serverLevel) {
            return serverLevel.getChunkSource().isPositionTicking(ChunkPos.pack(getBlockPos()));
        }
        return false;
    }

    protected Iterable<CableBlockEntity> getCables() {
        if (net == null) {
            CableNet.calculateNetwork(this);
        }
        startIndex %= net.cableList.size();
        return Iterables.concat(net.cableList.subList(startIndex, net.cableList.size()), net.cableList.subList(0, startIndex));
    }

    @Override
    public void readSync(ValueInput input) {
        super.readSync(input);
        readEnergySides(input);
    }

    @Override
    public void writeSync(ValueOutput output) {
        writeEnergySides(output);

        super.writeSync(output);
    }

    private void readEnergySides(ValueInput input) {
        // Read connected sides
        this.energySides.clear();
        var sideBits = input.getByteOr(NBT_ENERGY_SIDES, (byte) 0);
        for (var side : Direction.values()) {
            if ((sideBits & getSideMask(side)) != 0) {
                this.energySides.add(side);
            }
        }
    }

    private void writeEnergySides(ValueOutput output) {
        // Write connected sides
        byte sideBits = 0;
        for (var side : this.energySides) {
            sideBits |= getSideMask(side);
        }
        output.putByte(NBT_ENERGY_SIDES, sideBits);
    }

    private static byte getSideMask(Direction side) {
        return (byte) (1 << side.ordinal());
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean keepStorable() {
        return false;
    }

    @Override
    public long insertEnergy(long maxReceive, TransactionContext tx, @Nullable Direction direction) {
        if (!(this.level instanceof ServerLevel serverLevel) || direction == null || !checkRedstone() || !canReceiveEnergy(direction)) {
            return 0;
        }

        startIndexJournal.updateSnapshots(tx);

        long received = 0;
        var cables = getCables();

        var insertionGuard = this.netInsertionGuard;
        if (insertionGuard.isTrue())
            return 0;
        insertionGuard.setTrue();

        try {
            startIndex++;

            for (var cable : cables) {
                long amount = maxReceive - received;
                if (amount <= 0)
                    break;
                if (!cable.energySides.isEmpty() && cable.isActive()) {
                    received += cable.pushEnergy(serverLevel, amount, tx, direction, this);
                }
            }

            return received;
        } finally {
            insertionGuard.setFalse();
        }
    }

    private long pushEnergy(ServerLevel level, long maxReceive, TransactionContext tx, @Nullable Direction direction, CableBlockEntity cable) {
        long received = 0;
        for (int i = 0; i < 6; ++i) {
            // Shift by tick count to ensure that it distributes evenly on average
            Direction side = Direction.from3DDataValue((i + level.getServer().getTickCount()) % 6);
            if (!this.energySides.contains(side))
                continue;

            long amount = Math.min(maxReceive - received, getEnergy().getMaxExtract());
            if (amount <= 0)
                break;
            if (cable.equals(this) && side.equals(direction) || !canExtractEnergy(side))
                continue;
            BlockPos pos = this.worldPosition.relative(side);
            if (direction != null && cable.getBlockPos().relative(direction).equals(pos))
                continue;
            received += receive(level, pos, side.getOpposite(), amount, tx);
        }
        return received;
    }

    private long receive(ServerLevel level, BlockPos pos, Direction side, long amount, TransactionContext tx) {
        if (capabilityCaches[side.ordinal()] == null) {
            capabilityCaches[side.ordinal()] = BlockCapabilityCache.create(Capabilities.Energy.BLOCK, level, pos, side);
        }
        var energy = capabilityCaches[side.ordinal()].getCapability();
        return energy != null ? energy.insert(Ints.saturatedCast(amount), tx) : 0;
    }

    public boolean canConnectTo(CableBlockEntity adjCable) {
        return getBlockState().is(adjCable.getBlockState().getBlock());
    }
}
