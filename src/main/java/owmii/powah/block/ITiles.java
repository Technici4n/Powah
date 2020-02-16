package owmii.powah.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.powah.block.cable.EnergyCableTile;
import owmii.powah.block.endercell.EnderCellTile;
import owmii.powah.block.endergate.EnderGateTile;
import owmii.powah.block.energizing.EnergizingOrbTile;
import owmii.powah.block.energizing.EnergizingRodTile;
import owmii.powah.block.energycell.EnergyCellTile;
import owmii.powah.block.energydischarger.EnergyDischargerTile;
import owmii.powah.block.energyhopper.EnergyHopperTile;
import owmii.powah.block.furnator.FurnatorTile;
import owmii.powah.block.magmator.MagmatorTile;
import owmii.powah.block.playertransmitter.PlayerTransmitterTile;
import owmii.powah.block.reactor.ReactorPartTile;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.block.solarpanel.SolarPanelTile;
import owmii.powah.block.thermo.ThermoGenTile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ITiles {
    public static final List<TileEntityType<?>> TYPES = new ArrayList<>();
    public static final TileEntityType<EnergyCellTile> ENERGY_CELL = register("energy_cell", EnergyCellTile::new, IBlocks.ENERGY_CELL_STARTER, IBlocks.ENERGY_CELL_BASIC, IBlocks.ENERGY_CELL_HARDENED, IBlocks.ENERGY_CELL_BLAZING, IBlocks.ENERGY_CELL_NIOTIC, IBlocks.ENERGY_CELL_SPIRITED, IBlocks.ENERGY_CELL_NITRO, IBlocks.ENERGY_CELL_CREATIVE);
    public static final TileEntityType<EnderCellTile> ENDER_CELL = register("ender_cell", EnderCellTile::new, IBlocks.ENDER_CELL_STARTER, IBlocks.ENDER_CELL_BASIC, IBlocks.ENDER_CELL_HARDENED, IBlocks.ENDER_CELL_BLAZING, IBlocks.ENDER_CELL_NIOTIC, IBlocks.ENDER_CELL_SPIRITED, IBlocks.ENDER_CELL_NITRO);
    public static final TileEntityType<EnderGateTile> ENDER_GATE = register("ender_gate", EnderGateTile::new, IBlocks.ENDER_GATE_STARTER, IBlocks.ENDER_GATE_BASIC, IBlocks.ENDER_GATE_HARDENED, IBlocks.ENDER_GATE_BLAZING, IBlocks.ENDER_GATE_NIOTIC, IBlocks.ENDER_GATE_SPIRITED, IBlocks.ENDER_GATE_NITRO);
    public static final TileEntityType<EnergyCableTile> ENERGY_CABLE = register("energy_cable", EnergyCableTile::new, IBlocks.ENERGY_CABLE_STARTER, IBlocks.ENERGY_CABLE_BASIC, IBlocks.ENERGY_CABLE_HARDENED, IBlocks.ENERGY_CABLE_BLAZING, IBlocks.ENERGY_CABLE_NIOTIC, IBlocks.ENERGY_CABLE_SPIRITED, IBlocks.ENERGY_CABLE_NITRO);
    public static final TileEntityType<EnergizingOrbTile> ENERGIZING_ORB = register("energizing_orb", EnergizingOrbTile::new, IBlocks.ENERGIZING_ORB);
    public static final TileEntityType<EnergizingRodTile> ENERGIZING_ROD = register("energizing_rod", EnergizingRodTile::new, IBlocks.ENERGIZING_ROD_STARTER, IBlocks.ENERGIZING_ROD_BASIC, IBlocks.ENERGIZING_ROD_HARDENED, IBlocks.ENERGIZING_ROD_BLAZING, IBlocks.ENERGIZING_ROD_NIOTIC, IBlocks.ENERGIZING_ROD_SPIRITED, IBlocks.ENERGIZING_ROD_NITRO);
    public static final TileEntityType<SolarPanelTile> SOLAR_PANEL = register("solar_panel", SolarPanelTile::new, IBlocks.SOLAR_PANEL_STARTER, IBlocks.SOLAR_PANEL_BASIC, IBlocks.SOLAR_PANEL_HARDENED, IBlocks.SOLAR_PANEL_BLAZING, IBlocks.SOLAR_PANEL_NIOTIC, IBlocks.SOLAR_PANEL_SPIRITED, IBlocks.SOLAR_PANEL_NITRO);
    public static final TileEntityType<FurnatorTile> FURNATOR = register("furnator", FurnatorTile::new, IBlocks.FURNATOR_STARTER, IBlocks.FURNATOR_BASIC, IBlocks.FURNATOR_HARDENED, IBlocks.FURNATOR_BLAZING, IBlocks.FURNATOR_NIOTIC, IBlocks.FURNATOR_SPIRITED, IBlocks.FURNATOR_NITRO);
    public static final TileEntityType<MagmatorTile> MAGMATOR = register("magmator", MagmatorTile::new, IBlocks.MAGMATOR_STARTER, IBlocks.MAGMATOR_BASIC, IBlocks.MAGMATOR_HARDENED, IBlocks.MAGMATOR_BLAZING, IBlocks.MAGMATOR_NIOTIC, IBlocks.MAGMATOR_SPIRITED, IBlocks.MAGMATOR_NITRO);
    public static final TileEntityType<ThermoGenTile> THERMO_GEN = register("thermo_gen", ThermoGenTile::new, IBlocks.THERMO_STARTER, IBlocks.THERMO_BASIC, IBlocks.THERMO_HARDENED, IBlocks.THERMO_BLAZING, IBlocks.THERMO_NIOTIC, IBlocks.THERMO_SPIRITED, IBlocks.THERMO_NITRO);
    public static final TileEntityType<ReactorTile> REACTOR = register("reactor", ReactorTile::new, IBlocks.REACTOR_STARTER, IBlocks.REACTOR_BASIC, IBlocks.REACTOR_HARDENED, IBlocks.REACTOR_BLAZING, IBlocks.REACTOR_NIOTIC, IBlocks.REACTOR_SPIRITED, IBlocks.REACTOR_NITRO);
    public static final TileEntityType<ReactorPartTile> REACTOR_PART = register("reactor_part", ReactorPartTile::new, IBlocks.REACTOR_STARTER, IBlocks.REACTOR_BASIC, IBlocks.REACTOR_HARDENED, IBlocks.REACTOR_BLAZING, IBlocks.REACTOR_NIOTIC, IBlocks.REACTOR_SPIRITED, IBlocks.REACTOR_NITRO);
    public static final TileEntityType<PlayerTransmitterTile> PLAYER_TRANSMITTER = register("player_transmitter", PlayerTransmitterTile::new, IBlocks.PLAYER_TRANSMITTER_STARTER, IBlocks.PLAYER_TRANSMITTER_BASIC, IBlocks.PLAYER_TRANSMITTER_HARDENED, IBlocks.PLAYER_TRANSMITTER_BLAZING, IBlocks.PLAYER_TRANSMITTER_NIOTIC, IBlocks.PLAYER_TRANSMITTER_SPIRITED, IBlocks.PLAYER_TRANSMITTER_NITRO);
    public static final TileEntityType<EnergyHopperTile> ENERGY_HOPPER = register("energy_hopper", EnergyHopperTile::new, IBlocks.ENERGY_HOPPER_STARTER, IBlocks.ENERGY_HOPPER_BASIC, IBlocks.ENERGY_HOPPER_HARDENED, IBlocks.ENERGY_HOPPER_BLAZING, IBlocks.ENERGY_HOPPER_NIOTIC, IBlocks.ENERGY_HOPPER_SPIRITED, IBlocks.ENERGY_HOPPER_NITRO);
    public static final TileEntityType<EnergyDischargerTile> ENERGY_DISCHARGER = register("energy_discharger", EnergyDischargerTile::new, IBlocks.ENERGY_DISCHARGER_STARTER, IBlocks.ENERGY_DISCHARGER_BASIC, IBlocks.ENERGY_DISCHARGER_HARDENED, IBlocks.ENERGY_DISCHARGER_BLAZING, IBlocks.ENERGY_DISCHARGER_NIOTIC, IBlocks.ENERGY_DISCHARGER_SPIRITED, IBlocks.ENERGY_DISCHARGER_NITRO);

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    static <T extends TileEntity> TileEntityType<T> register(final String name, final Supplier<? extends T> factoryIn, final Block... b) {
        final TileEntityType<T> type = TileEntityType.Builder.create((Supplier) factoryIn, b).build(null);
        type.setRegistryName(name);
        ITiles.TYPES.add(type);
        return type;
    }

    @SubscribeEvent
    public static void onRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        ITiles.TYPES.forEach(tileType -> event.getRegistry().register(tileType));
    }

}
