package zeroneye.powah.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import zeroneye.powah.block.generator.furnator.FurnatorTile;
import zeroneye.powah.block.generator.magmatic.MagmaticGenTile;
import zeroneye.powah.block.storage.EnergyCellTile;
import zeroneye.powah.block.transmitter.PlayerTransmitterTile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ITiles {
    public static final List<TileEntityType<?>> TILE_ENTITY_TYPES = new ArrayList<>();
    public static final TileEntityType<?> ENERGY_CELL = register("energy_cell", EnergyCellTile::new, IBlocks.BLOCKS.stream().filter(block -> block.createTileEntity(block.getDefaultState(), null) instanceof EnergyCellTile).toArray(Block[]::new));
    public static final TileEntityType<?> MAGMATIC_GENERATOR = register("magmatic_generator", MagmaticGenTile::new, IBlocks.BLOCKS.stream().filter(block -> block.createTileEntity(block.getDefaultState(), null) instanceof MagmaticGenTile).toArray(Block[]::new));
    public static final TileEntityType<?> FURNATOR = register("furnator", FurnatorTile::new, IBlocks.BLOCKS.stream().filter(block -> block.createTileEntity(block.getDefaultState(), null) instanceof FurnatorTile).toArray(Block[]::new));
    public static final TileEntityType<?> PLAYER_TRANSMITTER = register("player_transmitter", PlayerTransmitterTile::new, IBlocks.PLAYER_TRANSMITTER, IBlocks.PLAYER_TRANSMITTER_DIM);

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
