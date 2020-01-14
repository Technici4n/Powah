package owmii.powah.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.lib.block.IBlock;
import owmii.powah.item.ItemGroups;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IBlocks {
    public static final List<BlockItem> BLOCK_ITEMS = new ArrayList<>();
    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final UraniniteOreBlock URANINITE_ORE_POOR;
    public static final UraniniteOreBlock URANINITE_ORE;
    public static final UraniniteOreBlock URANINITE_ORE_DENSE;

    static {


        URANINITE_ORE_POOR = register("uraninite_ore_poor", new UraniniteOreBlock(Block.Properties.create(Material.ROCK)));
        URANINITE_ORE = register("uraninite_ore", new UraniniteOreBlock(Block.Properties.create(Material.ROCK)));
        URANINITE_ORE_DENSE = register("uraninite_ore_dense", new UraniniteOreBlock(Block.Properties.create(Material.ROCK)));
    }

    static <T extends Block & IBlock> T register(String name, T block) {
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
