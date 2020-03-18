package owmii.powah.handler.event;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import owmii.powah.block.IBlocks;
import owmii.powah.handler.ITags;

import static net.minecraftforge.common.Tags.Blocks.ORES;

public class TagsProvider {
    public static class Blocks extends BlockTagsProvider {
        public Blocks(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        protected void registerTags() {
            getBuilder(ORES).add(IBlocks.URANINITE_ORE);
            getBuilder(ITags.Blocks.URANINITE_ORE).add(IBlocks.URANINITE_ORE);
            getBuilder(ITags.Blocks.URANINITE_ORE_POOR).add(IBlocks.URANINITE_ORE_POOR);
            getBuilder(ITags.Blocks.URANINITE_ORE_DENSE).add(IBlocks.URANINITE_ORE_DENSE);
        }
    }

    public static class Items extends ItemTagsProvider {
        public Items(DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        protected void registerTags() {
            getBuilder(Tags.Items.ORES).add(IBlocks.URANINITE_ORE.asItem());
            getBuilder(ITags.Items.URANINITE_ORE).add(IBlocks.URANINITE_ORE.asItem());
            getBuilder(ITags.Items.URANINITE_ORE_POOR).add(IBlocks.URANINITE_ORE_POOR.asItem());
            getBuilder(ITags.Items.URANINITE_ORE_DENSE).add(IBlocks.URANINITE_ORE_DENSE.asItem());
        }
    }
}
