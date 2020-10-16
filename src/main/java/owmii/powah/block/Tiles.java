package owmii.powah.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
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
    public static final Registry<TileEntityType<?>> REG = new Registry(TileEntityType.class, Powah.MOD_ID);
    public static final TileEntityType<EnergyCellTile> ENERGY_CELL = REG.register("energy_cell", EnergyCellTile::new, Blcks.ENERGY_CELL.getArr(Block[]::new));
    public static final TileEntityType<EnderCellTile> ENDER_CELL = REG.register("ender_cell", EnderCellTile::new, Blcks.ENDER_CELL.getArr(Block[]::new));
    public static final TileEntityType<EnderGateTile> ENDER_GATE = REG.register("ender_gate", EnderGateTile::new, Blcks.ENDER_GATE.getArr(Block[]::new));
    public static final TileEntityType<CableTile> CABLE = REG.register("energy_cable", CableTile::new, Blcks.ENERGY_CABLE.getArr(Block[]::new));
    public static final TileEntityType<EnergizingOrbTile> ENERGIZING_ORB = REG.register("energizing_orb", EnergizingOrbTile::new, Blcks.ENERGIZING_ORB);
    public static final TileEntityType<EnergizingRodTile> ENERGIZING_ROD = REG.register("energizing_rod", EnergizingRodTile::new, Blcks.ENERGIZING_ROD.getArr(Block[]::new));
    public static final TileEntityType<SolarTile> SOLAR_PANEL = REG.register("solar_panel", SolarTile::new, Blcks.SOLAR_PANEL.getArr(Block[]::new));
    public static final TileEntityType<FurnatorTile> FURNATOR = REG.register("furnator", FurnatorTile::new, Blcks.FURNATOR.getArr(Block[]::new));
    public static final TileEntityType<MagmatorTile> MAGMATOR = REG.register("magmator", MagmatorTile::new, Blcks.MAGMATOR.getArr(Block[]::new));
    public static final TileEntityType<ThermoTile> THERMO_GEN = REG.register("thermo_gen", ThermoTile::new, Blcks.THERMO_GENERATOR.getArr(Block[]::new));
    public static final TileEntityType<ReactorTile> REACTOR = REG.register("reactor", ReactorTile::new, Blcks.REACTOR.getArr(Block[]::new));
    public static final TileEntityType<ReactorPartTile> REACTOR_PART = REG.register("reactor_part", ReactorPartTile::new, Blcks.REACTOR.getArr(Block[]::new));
    public static final TileEntityType<PlayerTransmitterTile> PLAYER_TRANSMITTER = REG.register("player_transmitter", PlayerTransmitterTile::new, Blcks.PLAYER_TRANSMITTER.getArr(Block[]::new));
    public static final TileEntityType<EnergyHopperTile> ENERGY_HOPPER = REG.register("energy_hopper", EnergyHopperTile::new, Blcks.ENERGY_HOPPER.getArr(Block[]::new));
    public static final TileEntityType<EnergyDischargerTile> ENERGY_DISCHARGER = REG.register("energy_discharger", EnergyDischargerTile::new, Blcks.ENERGY_DISCHARGER.getArr(Block[]::new));
}
