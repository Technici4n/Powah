package owmii.powah.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

public class Tiles {
    @SuppressWarnings("unchecked")
    public static final Registry<BlockEntityType<?>> REG = new Registry(BlockEntityType.class, Powah.MOD_ID);
    public static final BlockEntityType<EnergyCellTile> ENERGY_CELL = REG.registerTile("energy_cell", EnergyCellTile::new, Blcks.ENERGY_CELL.getArr(Block[]::new));
    public static final BlockEntityType<EnderCellTile> ENDER_CELL = REG.registerTile("ender_cell", EnderCellTile::new, Blcks.ENDER_CELL.getArr(Block[]::new));
    public static final BlockEntityType<EnderGateTile> ENDER_GATE = REG.registerTile("ender_gate", EnderGateTile::new, Blcks.ENDER_GATE.getArr(Block[]::new));
    public static final BlockEntityType<CableTile> CABLE = REG.registerTile("energy_cable", CableTile::new, Blcks.ENERGY_CABLE.getArr(Block[]::new));
    public static final BlockEntityType<EnergizingOrbTile> ENERGIZING_ORB = REG.registerTile("energizing_orb", EnergizingOrbTile::new, Blcks.ENERGIZING_ORB);
    public static final BlockEntityType<EnergizingRodTile> ENERGIZING_ROD = REG.registerTile("energizing_rod", EnergizingRodTile::new, Blcks.ENERGIZING_ROD.getArr(Block[]::new));
    public static final BlockEntityType<SolarTile> SOLAR_PANEL = REG.registerTile("solar_panel", SolarTile::new, Blcks.SOLAR_PANEL.getArr(Block[]::new));
    public static final BlockEntityType<FurnatorTile> FURNATOR = REG.registerTile("furnator", FurnatorTile::new, Blcks.FURNATOR.getArr(Block[]::new));
    public static final BlockEntityType<MagmatorTile> MAGMATOR = REG.registerTile("magmator", MagmatorTile::new, Blcks.MAGMATOR.getArr(Block[]::new));
    public static final BlockEntityType<ThermoTile> THERMO_GEN = REG.registerTile("thermo_gen", ThermoTile::new, Blcks.THERMO_GENERATOR.getArr(Block[]::new));
    public static final BlockEntityType<ReactorTile> REACTOR = REG.registerTile("reactor", ReactorTile::new, Blcks.REACTOR.getArr(Block[]::new));
    public static final BlockEntityType<ReactorPartTile> REACTOR_PART = REG.registerTile("reactor_part", ReactorPartTile::new, Blcks.REACTOR.getArr(Block[]::new));
    public static final BlockEntityType<PlayerTransmitterTile> PLAYER_TRANSMITTER = REG.registerTile("player_transmitter", PlayerTransmitterTile::new, Blcks.PLAYER_TRANSMITTER.getArr(Block[]::new));
    public static final BlockEntityType<EnergyHopperTile> ENERGY_HOPPER = REG.registerTile("energy_hopper", EnergyHopperTile::new, Blcks.ENERGY_HOPPER.getArr(Block[]::new));
    public static final BlockEntityType<EnergyDischargerTile> ENERGY_DISCHARGER = REG.registerTile("energy_discharger", EnergyDischargerTile::new, Blcks.ENERGY_DISCHARGER.getArr(Block[]::new));
}
