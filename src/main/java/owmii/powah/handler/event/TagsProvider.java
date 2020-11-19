package owmii.powah.handler.event;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import owmii.powah.block.Blcks;
import owmii.powah.handler.ITags;

import static net.minecraftforge.common.Tags.Blocks.ORES;

public class TagsProvider {
    public static class Blocks extends BlockTagsProvider {
        public Blocks(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        protected void registerTags() {
            getOrCreateBuilder(ORES).add(Blcks.URANINITE_ORE).add(Blcks.URANINITE_ORE_POOR).add(Blcks.URANINITE_ORE_DENSE);
            getOrCreateBuilder(ITags.Blocks.URANINITE_ORE).add(Blcks.URANINITE_ORE);
            getOrCreateBuilder(ITags.Blocks.URANINITE_ORE_POOR).add(Blcks.URANINITE_ORE_POOR);
            getOrCreateBuilder(ITags.Blocks.URANINITE_ORE_DENSE).add(Blcks.URANINITE_ORE_DENSE);

            getOrCreateBuilder(STORAGE_BLOCKS).add(Blcks.URANINITE);
            getOrCreateBuilder(ITags.Blocks.URANINITE_BLOCK).add(Blcks.URANINITE);
        }
    }

    public static class Items extends ItemTagsProvider {
        public Items(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider) {
            super(dataGenerator, blockTagProvider);
        }

        @Override
        protected void registerTags() {
            getOrCreateBuilder(Tags.Items.ORES).add(Blcks.URANINITE_ORE.asItem()).add(Blcks.URANINITE_ORE_POOR.asItem()).add(Blcks.URANINITE_ORE_DENSE.asItem());
            getOrCreateBuilder(ITags.Items.URANINITE_ORE).add(Blcks.URANINITE_ORE.asItem());
            getOrCreateBuilder(ITags.Items.URANINITE_ORE_POOR).add(Blcks.URANINITE_ORE_POOR.asItem());
            getOrCreateBuilder(ITags.Items.URANINITE_ORE_DENSE).add(Blcks.URANINITE_ORE_DENSE.asItem());

            getOrCreateBuilder(Tags.Items.STORAGE_BLOCKS).add(Blcks.URANINITE.asItem());
            getOrCreateBuilder(ITags.Items.URANINITE_BLOCK).add(Blcks.URANINITE.asItem());
        }
    }
}
