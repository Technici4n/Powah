package zeroneye.powah.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import zeroneye.lib.block.IBlockBase;
import zeroneye.powah.block.storage.EnergyCellBlock;
import zeroneye.powah.block.storage.EnergyCells;
import zeroneye.powah.item.ItemGroups;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IBlocks {
    public static final List<BlockItem> BLOCK_ITEMS = new ArrayList<>();
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final EnergyCellBlock[] ENERGY_CELLS;

    static {
        EnergyCells[] values = EnergyCells.values();
        ENERGY_CELLS = new EnergyCellBlock[values.length];
        for (int i = 0; i < values.length; i++) {
            EnergyCells cell = values[i];
            ENERGY_CELLS[i] = register("energy_cell_" + cell.name().toLowerCase(), new EnergyCellBlock(Block.Properties.create(cell.material).hardnessAndResistance(2.0F, cell.resistance), cell.capacity, cell.transfer).setCreative(cell.isCreative));
        }
    }

    static <T extends Block & IBlockBase> T register(String name, T block) {
        BlockItem itemBlock = block.getBlockItem(new Item.Properties(), ItemGroups.MAIN);
        itemBlock.setRegistryName(name);
        block.setRegistryName(name);
        BLOCK_ITEMS.add(itemBlock);
        BLOCKS.add(block);
        return block;
    }

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Block> event) {
        BLOCKS.forEach(block -> event.getRegistry().register(block));
    }
}
