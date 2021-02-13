package owmii.powah.handler.event;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import owmii.powah.block.Blcks;
import owmii.powah.handler.ITags;

import static net.minecraftforge.common.Tags.Blocks.ORES;
import static net.minecraftforge.common.Tags.Blocks.STORAGE_BLOCKS;

public class TagsProvider {
    public static class Blocks extends BlockTagsProvider {
        public Blocks(DataGenerator generatorIn, String modId, ExistingFileHelper existingFileHelper) {
            super(generatorIn, modId, existingFileHelper);
        }

        @Override
        protected void registerTags() {
            // Remove non-dry ice if Forge handles them in the future
            getOrCreateBuilder(ITags.Blocks.ICES).addTag(ITags.Blocks.ICES_ICE).addTag(ITags.Blocks.ICES_PACKED).addTag(ITags.Blocks.ICES_BLUE);
            getOrCreateBuilder(ITags.Blocks.ICES_ICE).add(net.minecraft.block.Blocks.ICE);
            getOrCreateBuilder(ITags.Blocks.ICES_PACKED).add(net.minecraft.block.Blocks.PACKED_ICE);
            getOrCreateBuilder(ITags.Blocks.ICES_BLUE).add(net.minecraft.block.Blocks.BLUE_ICE);
            getOrCreateBuilder(ITags.Blocks.ICES).addTag(ITags.Blocks.ICES_DRY);
            getOrCreateBuilder(ITags.Blocks.ICES_DRY).add(Blcks.DRY_ICE);

            getOrCreateBuilder(ORES).add(Blcks.URANINITE_ORE).add(Blcks.URANINITE_ORE_POOR).add(Blcks.URANINITE_ORE_DENSE);
            getOrCreateBuilder(ITags.Blocks.URANINITE_ORE).add(Blcks.URANINITE_ORE);
            getOrCreateBuilder(ITags.Blocks.URANINITE_ORE_POOR).add(Blcks.URANINITE_ORE_POOR);
            getOrCreateBuilder(ITags.Blocks.URANINITE_ORE_DENSE).add(Blcks.URANINITE_ORE_DENSE);

            getOrCreateBuilder(STORAGE_BLOCKS).add(Blcks.URANINITE);
            getOrCreateBuilder(ITags.Blocks.URANINITE_BLOCK).add(Blcks.URANINITE);
        }
    }

    public static class Items extends ItemTagsProvider {
        public Items(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, String modId, ExistingFileHelper existingFileHelper) {
            super(dataGenerator, blockTagProvider, modId, existingFileHelper);
        }

        @Override
        protected void registerTags() {
            // Remove non-dry ice if Forge handles them in the future
            getOrCreateBuilder(ITags.Items.ICES).addTag(ITags.Items.ICES_ICE).addTag(ITags.Items.ICES_PACKED).addTag(ITags.Items.ICES_BLUE);
            getOrCreateBuilder(ITags.Items.ICES_ICE).add(net.minecraft.item.Items.ICE);
            getOrCreateBuilder(ITags.Items.ICES_PACKED).add(net.minecraft.item.Items.PACKED_ICE);
            getOrCreateBuilder(ITags.Items.ICES_BLUE).add(net.minecraft.item.Items.BLUE_ICE);
            getOrCreateBuilder(ITags.Items.ICES).addTag(ITags.Items.ICES_DRY);
            getOrCreateBuilder(ITags.Items.ICES_DRY).add(Blcks.DRY_ICE.asItem());

            getOrCreateBuilder(Tags.Items.ORES).add(Blcks.URANINITE_ORE.asItem()).add(Blcks.URANINITE_ORE_POOR.asItem()).add(Blcks.URANINITE_ORE_DENSE.asItem());
            getOrCreateBuilder(ITags.Items.URANINITE_ORE).add(Blcks.URANINITE_ORE.asItem());
            getOrCreateBuilder(ITags.Items.URANINITE_ORE_POOR).add(Blcks.URANINITE_ORE_POOR.asItem());
            getOrCreateBuilder(ITags.Items.URANINITE_ORE_DENSE).add(Blcks.URANINITE_ORE_DENSE.asItem());

            getOrCreateBuilder(Tags.Items.STORAGE_BLOCKS).add(Blcks.URANINITE.asItem());
            getOrCreateBuilder(ITags.Items.URANINITE_BLOCK).add(Blcks.URANINITE.asItem());
        }
    }
}
