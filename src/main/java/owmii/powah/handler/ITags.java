package owmii.powah.handler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ITags {
    public static class Blocks {
        public static final Tags.IOptionalNamedTag<Block> URANINITE_ORE = tag("ores/uraninite");
        public static final Tags.IOptionalNamedTag<Block> URANINITE_ORE_POOR = tag("ores/uraninite_poor");
        public static final Tags.IOptionalNamedTag<Block> URANINITE_ORE_DENSE = tag("ores/uraninite_dense");

        private static Tags.IOptionalNamedTag<Block> tag(String name) {
            return BlockTags.createOptional(new ResourceLocation("forge", name));
        }

    }

    public static class Items {
        public static final Tags.IOptionalNamedTag<Item> URANINITE_ORE = tag("ores/uraninite");
        public static final Tags.IOptionalNamedTag<Item> URANINITE_ORE_POOR = tag("ores/uraninite_poor");
        public static final Tags.IOptionalNamedTag<Item> URANINITE_ORE_DENSE = tag("ores/uraninite_dense");

        private static Tags.IOptionalNamedTag<Item> tag(String name) {
            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }
    }
}
