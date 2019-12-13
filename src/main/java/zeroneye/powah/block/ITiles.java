package zeroneye.powah.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import zeroneye.powah.block.cable.CableTile;
import zeroneye.powah.block.discharger.DischargerTile;
import zeroneye.powah.block.energizing.EnergizingOrbTile;
import zeroneye.powah.block.energizing.EnergizingRodTile;
import zeroneye.powah.block.generator.furnator.FurnatorTile;
import zeroneye.powah.block.generator.magmatic.MagmaticGenTile;
import zeroneye.powah.block.generator.panel.solar.SolarPanelTile;
import zeroneye.powah.block.generator.thermoelectric.ThermoGeneratorTile;
import zeroneye.powah.block.hopper.EnergyHopperTile;
import zeroneye.powah.block.storage.EnergyCellTile;
import zeroneye.powah.block.transmitter.PlayerTransmitterTile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ITiles {
    public static final List<TileEntityType<?>> TILE_ENTITY_TYPES = new ArrayList<>();
    public static final TileEntityType<?> ENERGY_CELL = register("energy_cell", EnergyCellTile::new, IBlocks.ENERGY_CELLS);
    public static final TileEntityType<?> MAGMATIC_GENERATOR = register("magmatic_generator", MagmaticGenTile::new, IBlocks.MAGMATIC_GENERATORS);
    public static final TileEntityType<?> FURNATOR = register("furnator", FurnatorTile::new, IBlocks.FURNATORS);
    public static final TileEntityType<?> PLAYER_TRANSMITTER = register("player_transmitter", PlayerTransmitterTile::new, IBlocks.PLAYER_TRANSMITTER, IBlocks.PLAYER_TRANSMITTER_DIM);
    public static final TileEntityType<?> SOLAR_PANEL = register("solar_panel", SolarPanelTile::new, IBlocks.SOLAR_PANELS);
    public static final TileEntityType<?> THERMO_GENERATOR = register("thermo_generator_", ThermoGeneratorTile::new, IBlocks.THERMO_GENERATORS);
    public static final TileEntityType<?> CABLE = register("cable", CableTile::new, IBlocks.CABLES);
    public static final TileEntityType<?> DISCHARGER = register("discharger", DischargerTile::new, IBlocks.DISCHARGER);
    public static final TileEntityType<?> ENERGY_HOPPER = register("energy_hopper", EnergyHopperTile::new, IBlocks.ENERGY_HOPPER);
    public static final TileEntityType<?> ENERGIZING_ORB = register("energizing_orb", EnergizingOrbTile::new, IBlocks.ENERGIZING_ORB);
    public static final TileEntityType<?> ENERGIZING_ROD = register("energizing_rod", EnergizingRodTile::new, IBlocks.ENERGIZING_RODS);

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    static <T extends TileEntity> TileEntityType<T> register(String name, Supplier<? extends T> factoryIn, Block... b) {
        TileEntityType<T> type = (TileEntityType<T>) TileEntityType.Builder.create(factoryIn, b).build(null);
        type.setRegistryName(name);
        TILE_ENTITY_TYPES.add(type);
        return type;
    }

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<TileEntityType<?>> event) {
        TILE_ENTITY_TYPES.forEach(tileType -> event.getRegistry().register(tileType));
    }
}
