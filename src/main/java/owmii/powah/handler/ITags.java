package owmii.powah.handler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import owmii.powah.Powah;

public class ITags {
    public static class Blocks {
        public static final Tag<Block> ORE = new BlockTags.Wrapper(new ResourceLocation("forge", "ores/uraninite"));

        public static final Tag<Block> URANINITE_ORE = tag("ores/uraninite");
        public static final Tag<Block> URANINITE_ORE_POOR = tag("ores/uraninite/poor");
        public static final Tag<Block> URANINITE_ORE_DENSE = tag("ores/uraninite/dense");

        private static Tag<Block> tag(String name) {
            return new BlockTags.Wrapper(new ResourceLocation(Powah.MOD_ID, name));
        }
    }

    public static class Items {
        public static final Tag<Item> ORE = new ItemTags.Wrapper(new ResourceLocation("forge", "ores/uraninite"));

        public static final Tag<Item> URANINITE_ORE = tag("ores/uraninite");
        public static final Tag<Item> URANINITE_ORE_POOR = tag("ores/uraninite/poor");
        public static final Tag<Item> URANINITE_ORE_DENSE = tag("ores/uraninite/dense");

        private static Tag<Item> tag(String name) {
            return new ItemTags.Wrapper(new ResourceLocation(Powah.MOD_ID, name));
        }
    }
}
