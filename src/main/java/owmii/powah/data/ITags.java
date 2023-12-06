package owmii.powah.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ITags {
    public static class Blocks {
        // Remove non-dry ice if Forge handles them in the future
        public static final TagKey<Block> ICES = tag("ices");
        public static final TagKey<Block> ICES_ICE = tag("ices/ice");
        public static final TagKey<Block> ICES_PACKED = tag("ices/packed");
        public static final TagKey<Block> ICES_BLUE = tag("ices/blue");
        public static final TagKey<Block> ICES_DRY = tag("ices/dry");
        public static final TagKey<Block> URANINITE_ORE = tag("ores/uraninite");
        public static final TagKey<Block> URANINITE_BLOCK = tag("storage_blocks/uraninite");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }
    }

    public static class Items {
        // Remove non-dry ice if Forge handles them in the future
        public static final TagKey<Item> ICES = tag("ices");
        public static final TagKey<Item> ICES_ICE = tag("ices/ice");
        public static final TagKey<Item> ICES_PACKED = tag("ices/packed");
        public static final TagKey<Item> ICES_BLUE = tag("ices/blue");
        public static final TagKey<Item> ICES_DRY = tag("ices/dry");
        public static final TagKey<Item> URANINITE_ORE = tag("ores/uraninite");
        public static final TagKey<Item> URANINITE_BLOCK = tag("storage_blocks/uraninite");
        public static final TagKey<Item> URANINITE_RAW = tag("raw_materials/uraninite");
        public static final TagKey<Item> WRENCHES = tag("tools/wrench");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }
    }

    // Abstractions over Fabric tags for items
    public static class ItemAbstractions {
        public static final TagKey<Item> GLASS = tag("glass_blocks");
        public static final TagKey<Item> GLASS_PANES = tag("glass_panes");
        public static final TagKey<Item> QUARTZ_BLOCKS = tag("quartz_blocks");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation("c", name));
        }
    }
}
