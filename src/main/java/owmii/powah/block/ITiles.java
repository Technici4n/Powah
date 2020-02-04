package owmii.powah.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.powah.block.cable.EnergyCableTile;
import owmii.powah.block.energizing.EnergizingOrbTile;
import owmii.powah.block.energizing.EnergizingRodTile;
import owmii.powah.block.energycell.EnergyCellTile;
import owmii.powah.block.furnator.FurnatorTile;
import owmii.powah.block.magmator.MagmatorTile;
import owmii.powah.block.solarpanel.SolarPanelTile;
import owmii.powah.block.thermo.ThermoGenTile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ITiles {
    public static final List<TileEntityType<?>> TYPES = new ArrayList<>();
    public static final TileEntityType<EnergyCellTile> ENERGY_CELL = register("energy_cell", (Supplier<? extends EnergyCellTile>) EnergyCellTile::new, IBlocks.ENERGY_CELL_STARTER, IBlocks.ENERGY_CELL_BASIC, IBlocks.ENERGY_CELL_HARDENED, IBlocks.ENERGY_CELL_BLAZING, IBlocks.ENERGY_CELL_NIOTIC, IBlocks.ENERGY_CELL_SPIRITED, IBlocks.ENERGY_CELL_NITRO, IBlocks.ENERGY_CELL_CREATIVE);
    public static final TileEntityType<EnergyCableTile> ENERGY_CABLE = register("energy_cable", (Supplier<? extends EnergyCableTile>) EnergyCableTile::new, IBlocks.ENERGY_CABLE_STARTER, IBlocks.ENERGY_CABLE_BASIC, IBlocks.ENERGY_CABLE_HARDENED, IBlocks.ENERGY_CABLE_BLAZING, IBlocks.ENERGY_CABLE_NIOTIC, IBlocks.ENERGY_CABLE_SPIRITED, IBlocks.ENERGY_CABLE_NITRO);
    public static final TileEntityType<EnergizingOrbTile> ENERGIZING_ORB = register("energizing_orb", (Supplier<? extends EnergizingOrbTile>) EnergizingOrbTile::new, IBlocks.ENERGIZING_ORB);
    public static final TileEntityType<EnergizingRodTile> ENERGIZING_ROD = register("energizing_rod", (Supplier<? extends EnergizingRodTile>) EnergizingRodTile::new, IBlocks.ENERGIZING_ROD_STARTER, IBlocks.ENERGIZING_ROD_BASIC, IBlocks.ENERGIZING_ROD_HARDENED, IBlocks.ENERGIZING_ROD_BLAZING, IBlocks.ENERGIZING_ROD_NIOTIC, IBlocks.ENERGIZING_ROD_SPIRITED, IBlocks.ENERGIZING_ROD_NITRO);
    public static final TileEntityType<SolarPanelTile> SOLAR_PANEL = register("solar_panel", (Supplier<? extends SolarPanelTile>) SolarPanelTile::new, IBlocks.SOLAR_PANEL_STARTER, IBlocks.SOLAR_PANEL_BASIC, IBlocks.SOLAR_PANEL_HARDENED, IBlocks.SOLAR_PANEL_BLAZING, IBlocks.SOLAR_PANEL_NIOTIC, IBlocks.SOLAR_PANEL_SPIRITED, IBlocks.SOLAR_PANEL_NITRO);
    public static final TileEntityType<FurnatorTile> FURNATOR = register("furnator", (Supplier<? extends FurnatorTile>) FurnatorTile::new, IBlocks.FURNATOR_STARTER, IBlocks.FURNATOR_BASIC, IBlocks.FURNATOR_HARDENED, IBlocks.FURNATOR_BLAZING, IBlocks.FURNATOR_NIOTIC, IBlocks.FURNATOR_SPIRITED, IBlocks.FURNATOR_NITRO);
    public static final TileEntityType<MagmatorTile> MAGMATOR = register("magmator", (Supplier<? extends MagmatorTile>) MagmatorTile::new, IBlocks.MAGMATOR_STARTER, IBlocks.MAGMATOR_BASIC, IBlocks.MAGMATOR_HARDENED, IBlocks.MAGMATOR_BLAZING, IBlocks.MAGMATOR_NIOTIC, IBlocks.MAGMATOR_SPIRITED, IBlocks.MAGMATOR_NITRO);
    public static final TileEntityType<ThermoGenTile> THERMO_GEN = register("thermo_gen", (Supplier<? extends ThermoGenTile>) ThermoGenTile::new, IBlocks.THERMO_STARTER, IBlocks.THERMO_BASIC, IBlocks.THERMO_HARDENED, IBlocks.THERMO_BLAZING, IBlocks.THERMO_NIOTIC, IBlocks.THERMO_SPIRITED, IBlocks.THERMO_NITRO);

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
