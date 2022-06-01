package owmii.powah.handler.event;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
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
        protected void addTags() {
            // Remove non-dry ice if Forge handles them in the future
            tag(ITags.Blocks.ICES).addTag(ITags.Blocks.ICES_ICE).addTag(ITags.Blocks.ICES_PACKED).addTag(ITags.Blocks.ICES_BLUE);
            tag(ITags.Blocks.ICES_ICE).add(net.minecraft.world.level.block.Blocks.ICE);
            tag(ITags.Blocks.ICES_PACKED).add(net.minecraft.world.level.block.Blocks.PACKED_ICE);
            tag(ITags.Blocks.ICES_BLUE).add(net.minecraft.world.level.block.Blocks.BLUE_ICE);
            tag(ITags.Blocks.ICES).addTag(ITags.Blocks.ICES_DRY);
            tag(ITags.Blocks.ICES_DRY).add(Blcks.DRY_ICE);

            tag(ORES).add(Blcks.URANINITE_ORE).add(Blcks.URANINITE_ORE_POOR).add(Blcks.URANINITE_ORE_DENSE);
            tag(ITags.Blocks.URANINITE_ORE).add(Blcks.URANINITE_ORE);
            tag(ITags.Blocks.URANINITE_ORE_POOR).add(Blcks.URANINITE_ORE_POOR);
            tag(ITags.Blocks.URANINITE_ORE_DENSE).add(Blcks.URANINITE_ORE_DENSE);

            tag(STORAGE_BLOCKS).add(Blcks.URANINITE);
            tag(ITags.Blocks.URANINITE_BLOCK).add(Blcks.URANINITE);
        }
    }

    public static class Items extends ItemTagsProvider {
        public Items(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, String modId, ExistingFileHelper existingFileHelper) {
            super(dataGenerator, blockTagProvider, modId, existingFileHelper);
        }

        @Override
        protected void addTags() {
            // Remove non-dry ice if Forge handles them in the future
            tag(ITags.Items.ICES).addTag(ITags.Items.ICES_ICE).addTag(ITags.Items.ICES_PACKED).addTag(ITags.Items.ICES_BLUE);
            tag(ITags.Items.ICES_ICE).add(net.minecraft.world.item.Items.ICE);
            tag(ITags.Items.ICES_PACKED).add(net.minecraft.world.item.Items.PACKED_ICE);
            tag(ITags.Items.ICES_BLUE).add(net.minecraft.world.item.Items.BLUE_ICE);
            tag(ITags.Items.ICES).addTag(ITags.Items.ICES_DRY);
            tag(ITags.Items.ICES_DRY).add(Blcks.DRY_ICE.asItem());

            tag(Tags.Items.ORES).add(Blcks.URANINITE_ORE.asItem()).add(Blcks.URANINITE_ORE_POOR.asItem()).add(Blcks.URANINITE_ORE_DENSE.asItem());
            tag(ITags.Items.URANINITE_ORE).add(Blcks.URANINITE_ORE.asItem());
            tag(ITags.Items.URANINITE_ORE_POOR).add(Blcks.URANINITE_ORE_POOR.asItem());
            tag(ITags.Items.URANINITE_ORE_DENSE).add(Blcks.URANINITE_ORE_DENSE.asItem());

            tag(Tags.Items.STORAGE_BLOCKS).add(Blcks.URANINITE.asItem());
            tag(ITags.Items.URANINITE_BLOCK).add(Blcks.URANINITE.asItem());
        }
    }
}
