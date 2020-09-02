package owmii.powah.block;

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
    public static final TileEntityType<EnergyCellTile> ENERGY_CELL = REG.register("energy_cell", EnergyCellTile::new, Blcks.ENERGY_CELL_STARTER, Blcks.ENERGY_CELL_BASIC, Blcks.ENERGY_CELL_HARDENED, Blcks.ENERGY_CELL_BLAZING, Blcks.ENERGY_CELL_NIOTIC, Blcks.ENERGY_CELL_SPIRITED, Blcks.ENERGY_CELL_NITRO, Blcks.ENERGY_CELL_CREATIVE);
    public static final TileEntityType<EnderCellTile> ENDER_CELL = REG.register("ender_cell", EnderCellTile::new, Blcks.ENDER_CELL_STARTER, Blcks.ENDER_CELL_BASIC, Blcks.ENDER_CELL_HARDENED, Blcks.ENDER_CELL_BLAZING, Blcks.ENDER_CELL_NIOTIC, Blcks.ENDER_CELL_SPIRITED, Blcks.ENDER_CELL_NITRO);
    public static final TileEntityType<EnderGateTile> ENDER_GATE = REG.register("ender_gate", EnderGateTile::new, Blcks.ENDER_GATE_STARTER, Blcks.ENDER_GATE_BASIC, Blcks.ENDER_GATE_HARDENED, Blcks.ENDER_GATE_BLAZING, Blcks.ENDER_GATE_NIOTIC, Blcks.ENDER_GATE_SPIRITED, Blcks.ENDER_GATE_NITRO);
    public static final TileEntityType<CableTile> CABLE = REG.register("energy_cable", CableTile::new, Blcks.CABLE_STARTER, Blcks.CABLE_BASIC, Blcks.CABLE_HARDENED, Blcks.CABLE_BLAZING, Blcks.CABLE_NIOTIC, Blcks.CABLE_SPIRITED, Blcks.CABLE_NITRO);
    public static final TileEntityType<EnergizingOrbTile> ENERGIZING_ORB = REG.register("energizing_orb", EnergizingOrbTile::new, Blcks.ENERGIZING_ORB);
    public static final TileEntityType<EnergizingRodTile> ENERGIZING_ROD = REG.register("energizing_rod", EnergizingRodTile::new, Blcks.ENERGIZING_ROD_STARTER, Blcks.ENERGIZING_ROD_BASIC, Blcks.ENERGIZING_ROD_HARDENED, Blcks.ENERGIZING_ROD_BLAZING, Blcks.ENERGIZING_ROD_NIOTIC, Blcks.ENERGIZING_ROD_SPIRITED, Blcks.ENERGIZING_ROD_NITRO);
    public static final TileEntityType<SolarTile> SOLAR_PANEL = REG.register("solar_panel", SolarTile::new, Blcks.SOLAR_PANEL_STARTER, Blcks.SOLAR_PANEL_BASIC, Blcks.SOLAR_PANEL_HARDENED, Blcks.SOLAR_PANEL_BLAZING, Blcks.SOLAR_PANEL_NIOTIC, Blcks.SOLAR_PANEL_SPIRITED, Blcks.SOLAR_PANEL_NITRO);
    public static final TileEntityType<FurnatorTile> FURNATOR = REG.register("furnator", FurnatorTile::new, Blcks.FURNATOR_STARTER, Blcks.FURNATOR_BASIC, Blcks.FURNATOR_HARDENED, Blcks.FURNATOR_BLAZING, Blcks.FURNATOR_NIOTIC, Blcks.FURNATOR_SPIRITED, Blcks.FURNATOR_NITRO);
    public static final TileEntityType<MagmatorTile> MAGMATOR = REG.register("magmator", MagmatorTile::new, Blcks.MAGMATOR_STARTER, Blcks.MAGMATOR_BASIC, Blcks.MAGMATOR_HARDENED, Blcks.MAGMATOR_BLAZING, Blcks.MAGMATOR_NIOTIC, Blcks.MAGMATOR_SPIRITED, Blcks.MAGMATOR_NITRO);
    public static final TileEntityType<ThermoTile> THERMO_GEN = REG.register("thermo_gen", ThermoTile::new, Blcks.THERMO_STARTER, Blcks.THERMO_BASIC, Blcks.THERMO_HARDENED, Blcks.THERMO_BLAZING, Blcks.THERMO_NIOTIC, Blcks.THERMO_SPIRITED, Blcks.THERMO_NITRO);
    public static final TileEntityType<ReactorTile> REACTOR = REG.register("reactor", ReactorTile::new, Blcks.REACTOR_STARTER, Blcks.REACTOR_BASIC, Blcks.REACTOR_HARDENED, Blcks.REACTOR_BLAZING, Blcks.REACTOR_NIOTIC, Blcks.REACTOR_SPIRITED, Blcks.REACTOR_NITRO);
    public static final TileEntityType<ReactorPartTile> REACTOR_PART = REG.register("reactor_part", ReactorPartTile::new, Blcks.REACTOR_STARTER, Blcks.REACTOR_BASIC, Blcks.REACTOR_HARDENED, Blcks.REACTOR_BLAZING, Blcks.REACTOR_NIOTIC, Blcks.REACTOR_SPIRITED, Blcks.REACTOR_NITRO);
    public static final TileEntityType<PlayerTransmitterTile> PLAYER_TRANSMITTER = REG.register("player_transmitter", PlayerTransmitterTile::new, Blcks.PLAYER_TRANSMITTER_STARTER, Blcks.PLAYER_TRANSMITTER_BASIC, Blcks.PLAYER_TRANSMITTER_HARDENED, Blcks.PLAYER_TRANSMITTER_BLAZING, Blcks.PLAYER_TRANSMITTER_NIOTIC, Blcks.PLAYER_TRANSMITTER_SPIRITED, Blcks.PLAYER_TRANSMITTER_NITRO);
    public static final TileEntityType<EnergyHopperTile> ENERGY_HOPPER = REG.register("energy_hopper", EnergyHopperTile::new, Blcks.ENERGY_HOPPER_STARTER, Blcks.ENERGY_HOPPER_BASIC, Blcks.ENERGY_HOPPER_HARDENED, Blcks.ENERGY_HOPPER_BLAZING, Blcks.ENERGY_HOPPER_NIOTIC, Blcks.ENERGY_HOPPER_SPIRITED, Blcks.ENERGY_HOPPER_NITRO);
    public static final TileEntityType<EnergyDischargerTile> ENERGY_DISCHARGER = REG.register("energy_discharger", EnergyDischargerTile::new, Blcks.ENERGY_DISCHARGER_STARTER, Blcks.ENERGY_DISCHARGER_BASIC, Blcks.ENERGY_DISCHARGER_HARDENED, Blcks.ENERGY_DISCHARGER_BLAZING, Blcks.ENERGY_DISCHARGER_NIOTIC, Blcks.ENERGY_DISCHARGER_SPIRITED, Blcks.ENERGY_DISCHARGER_NITRO);
}
