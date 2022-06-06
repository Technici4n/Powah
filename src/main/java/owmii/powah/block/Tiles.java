package owmii.powah.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.lib.registry.Registry;
import owmii.powah.Powah;
import owmii.powah.block.cable.CableTile;
import owmii.powah.block.discharger.EnergyDischargerTile;
import owmii.powah.block.ender.EnderCellTile;
import owmii.powah.block.ender.EnderGateTile;
import owmii.powah.block.energizing.EnergizingOrbTile;
import owmii.powah.block.energizing.EnergizingRodTile;
import owmii.powah.block.energycell.EnergyCellTile;
import owmii.powah.block.furnator.FurnatorTile;
import owmii.powah.block.hopper.EnergyHopperTile;
import owmii.powah.block.magmator.MagmatorTile;
import owmii.powah.block.reactor.ReactorPartTile;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.block.solar.SolarTile;
import owmii.powah.block.thermo.ThermoTile;
import owmii.powah.block.transmitter.PlayerTransmitterTile;

import java.util.List;
import java.util.function.Supplier;

public class Tiles {
    public static final DeferredRegister<BlockEntityType<?>> DR = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Powah.MOD_ID);
    
    private static <BE extends BlockEntity> Supplier<BlockEntityType<BE>> register(String path, BlockEntityType.BlockEntitySupplier<BE> supplier, Supplier<List<Block>> blocks) {
        return DR.register(path, () -> BlockEntityType.Builder.of(supplier, blocks.get().toArray(Block[]::new)).build(null));
    }
    
    public static final Supplier<BlockEntityType<EnergyCellTile>> ENERGY_CELL = register("energy_cell", EnergyCellTile::new, () -> Blcks.ENERGY_CELL.getAll());
    public static final Supplier<BlockEntityType<EnderCellTile>> ENDER_CELL = register("ender_cell", EnderCellTile::new, () -> Blcks.ENDER_CELL.getAll());
    public static final Supplier<BlockEntityType<EnderGateTile>> ENDER_GATE = register("ender_gate", EnderGateTile::new, () -> Blcks.ENDER_GATE.getAll());
    public static final Supplier<BlockEntityType<CableTile>> CABLE = register("energy_cable", CableTile::new, () -> Blcks.ENERGY_CABLE.getAll());
    public static final Supplier<BlockEntityType<EnergizingOrbTile>> ENERGIZING_ORB = register("energizing_orb", EnergizingOrbTile::new, () -> List.of(Blcks.ENERGIZING_ORB.get()));
    public static final Supplier<BlockEntityType<EnergizingRodTile>> ENERGIZING_ROD = register("energizing_rod", EnergizingRodTile::new, () -> Blcks.ENERGIZING_ROD.getAll());
    public static final Supplier<BlockEntityType<SolarTile>> SOLAR_PANEL = register("solar_panel", SolarTile::new, () -> Blcks.SOLAR_PANEL.getAll());
    public static final Supplier<BlockEntityType<FurnatorTile>> FURNATOR = register("furnator", FurnatorTile::new, () -> Blcks.FURNATOR.getAll());
    public static final Supplier<BlockEntityType<MagmatorTile>> MAGMATOR = register("magmator", MagmatorTile::new, () -> Blcks.MAGMATOR.getAll());
    public static final Supplier<BlockEntityType<ThermoTile>> THERMO_GEN = register("thermo_gen", ThermoTile::new, () -> Blcks.THERMO_GENERATOR.getAll());
    public static final Supplier<BlockEntityType<ReactorTile>> REACTOR = register("reactor", ReactorTile::new, () -> Blcks.REACTOR.getAll());
    public static final Supplier<BlockEntityType<ReactorPartTile>> REACTOR_PART = register("reactor_part", ReactorPartTile::new, () -> Blcks.REACTOR.getAll());
    public static final Supplier<BlockEntityType<PlayerTransmitterTile>> PLAYER_TRANSMITTER = register("player_transmitter", PlayerTransmitterTile::new, () -> Blcks.PLAYER_TRANSMITTER.getAll());
    public static final Supplier<BlockEntityType<EnergyHopperTile>> ENERGY_HOPPER = register("energy_hopper", EnergyHopperTile::new, () -> Blcks.ENERGY_HOPPER.getAll());
    public static final Supplier<BlockEntityType<EnergyDischargerTile>> ENERGY_DISCHARGER = register("energy_discharger", EnergyDischargerTile::new, () -> Blcks.ENERGY_DISCHARGER.getAll());
}
