package owmii.powah.fabric.data;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ITags {
    public static class Blocks {
        // Remove non-dry ice if Forge handles them in the future
        public static final TagKey<Block> ICES = tag("ices");
        public static final TagKey<Block> ICES_ICE = tag("regular_ices");
        public static final TagKey<Block> ICES_PACKED = tag("packed_ices");
        public static final TagKey<Block> ICES_BLUE = tag("blue_ices");
        public static final TagKey<Block> ICES_DRY = tag("dry_ices");
        public static final TagKey<Block> URANINITE_ORE = tag("uraninite_ores");
        public static final TagKey<Block> URANINITE_BLOCK = tag("uraninite_blocks");

        private static TagKey<Block> tag(String name) {
            return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("c", name));
        }
    }

    public static class Items {
        // Remove non-dry ice if Forge handles them in the future
        public static final TagKey<Item> ICES = tag("ices");
        public static final TagKey<Item> ICES_ICE = tag("regular_ices");
        public static final TagKey<Item> ICES_PACKED = tag("packed_ices");
        public static final TagKey<Item> ICES_BLUE = tag("blue_ices");
        public static final TagKey<Item> ICES_DRY = tag("dry_ices");
        public static final TagKey<Item> URANINITE_ORE = tag("uraninite_ores");
        public static final TagKey<Item> URANINITE_BLOCK = tag("uraninite_blocks");
        public static final TagKey<Item> URANINITE_RAW = tag("raw_uraninite_ores");
        public static final TagKey<Item> QUARTZ_BLOCKS = tag("quartz_blocks"); // TODO PR TO FABRIC
        public static final TagKey<Item> WRENCHES = tag("wrenches");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("c", name));
        }
    }
}
