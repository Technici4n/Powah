package owmii.powah.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import owmii.powah.Powah;

public class ITags {
    public static class Blocks {
        // Remove non-dry ice if Forge handles them in the future
        public static final TagKey<Block> ICES = tag("ices");
        public static final TagKey<Block> ICES_ICE = tag("ices/ice");
        public static final TagKey<Block> ICES_PACKED = tag("ices/packed");
        public static final TagKey<Block> ICES_BLUE = tag("ices/blue");
        public static final TagKey<Block> ICES_DRY = tag("ices/dry");
        public static final TagKey<Block> URANINITE_ORE = tag("ores/uraninite");
        public static final TagKey<Block> URANINITE_ORE_REGULAR = tag("ores/uraninite_regular");
        public static final TagKey<Block> URANINITE_ORE_DENSE = tag("ores/uraninite_dense");
        public static final TagKey<Block> URANINITE_ORE_POOR = tag("ores/uraninite_poor");
        public static final TagKey<Block> URANINITE_BLOCK = tag("storage_blocks/uraninite");

        public static final TagKey<Block> ENERGY_CELLS = powah("energy_cells");
        public static final TagKey<Block> ENDER_CELLS = powah("ender_cells");
        public static final TagKey<Block> ENERGY_CABLES = powah("energy_cables");
        public static final TagKey<Block> ENDER_GATES = powah("ender_gates");
        public static final TagKey<Block> ENERGIZING_RODS = powah("energizing_rods");
        public static final TagKey<Block> FURNATORS = powah("furnators");
        public static final TagKey<Block> MAGMATORS = powah("magmators");
        public static final TagKey<Block> THERMO_GENERATORS = powah("thermo_generators");
        public static final TagKey<Block> SOLAR_PANELS = powah("solar_panels");
        public static final TagKey<Block> REACTORS = powah("reactors");
        public static final TagKey<Block> PLAYER_TRANSMITTERS = powah("player_transmitters");
        public static final TagKey<Block> ENERGY_HOPPERS = powah("energy_hoppers");
        public static final TagKey<Block> ENERGY_DISCHARGERS = powah("energy_dischargers");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }

        private static TagKey<Block> powah(String name) {
            return BlockTags.create(Powah.id(name));
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
        public static final TagKey<Item> URANINITE_ORE_REGULAR = tag("ores/uraninite_regular");
        public static final TagKey<Item> URANINITE_ORE_DENSE = tag("ores/uraninite_dense");
        public static final TagKey<Item> URANINITE_ORE_POOR = tag("ores/uraninite_poor");
        public static final TagKey<Item> URANINITE_BLOCK = tag("storage_blocks/uraninite");
        public static final TagKey<Item> URANINITE_RAW = tag("raw_materials/uraninite");
        public static final TagKey<Item> WRENCHES = tag("tools/wrench");
        public static final TagKey<Item> QUARTZ_BLOCKS = tag("storage_blocks/quartz");

        public static final TagKey<Item> ENERGY_CELLS = powah("energy_cells");
        public static final TagKey<Item> ENDER_CELLS = powah("ender_cells");
        public static final TagKey<Item> ENERGY_CABLES = powah("energy_cables");
        public static final TagKey<Item> ENDER_GATES = powah("ender_gates");
        public static final TagKey<Item> ENERGIZING_RODS = powah("energizing_rods");
        public static final TagKey<Item> FURNATORS = powah("furnators");
        public static final TagKey<Item> MAGMATORS = powah("magmators");
        public static final TagKey<Item> THERMO_GENERATORS = powah("thermo_generators");
        public static final TagKey<Item> SOLAR_PANELS = powah("solar_panels");
        public static final TagKey<Item> REACTORS = powah("reactors");
        public static final TagKey<Item> PLAYER_TRANSMITTERS = powah("player_transmitters");
        public static final TagKey<Item> ENERGY_HOPPERS = powah("energy_hoppers");
        public static final TagKey<Item> ENERGY_DISCHARGERS = powah("energy_dischargers");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }

        private static TagKey<Item> powah(String name) {
            return ItemTags.create(Powah.id(name));
        }
    }
}
