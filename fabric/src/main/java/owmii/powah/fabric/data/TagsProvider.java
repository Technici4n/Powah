package owmii.powah.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.item.Itms;

public class TagsProvider {
    public static class Blocks extends FabricTagProvider.BlockTagProvider {
        public Blocks(FabricDataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        protected void generateTags() {
            // Remove non-dry ice if Forge handles them in the future
            tag(ITags.Blocks.ICES).addTag(ITags.Blocks.ICES_ICE).addTag(ITags.Blocks.ICES_PACKED).addTag(ITags.Blocks.ICES_BLUE);
            tag(ITags.Blocks.ICES_ICE).add(net.minecraft.world.level.block.Blocks.ICE);
            tag(ITags.Blocks.ICES_PACKED).add(net.minecraft.world.level.block.Blocks.PACKED_ICE);
            tag(ITags.Blocks.ICES_BLUE).add(net.minecraft.world.level.block.Blocks.BLUE_ICE);
            tag(ITags.Blocks.ICES).addTag(ITags.Blocks.ICES_DRY);
            tag(ITags.Blocks.ICES_DRY).add(Blcks.DRY_ICE.get());

            tag(ITags.Blocks.URANINITE_ORE).add(Blcks.URANINITE_ORE.get()).add(Blcks.URANINITE_ORE_POOR.get()).add(Blcks.URANINITE_ORE_DENSE.get());
            tag(ITags.Blocks.URANINITE_ORE).add(Blcks.DEEPSLATE_URANINITE_ORE.get()).add(Blcks.DEEPSLATE_URANINITE_ORE_POOR.get()).add(Blcks.DEEPSLATE_URANINITE_ORE_DENSE.get());
            tag(ConventionalBlockTags.ORES).addTag(ITags.Blocks.URANINITE_ORE);

            // TODO PR TO FABRIC
            //tag(STORAGE_BLOCKS).add(Blcks.URANINITE.get());
            tag(ITags.Blocks.URANINITE_BLOCK).add(Blcks.URANINITE.get());

            // All of our blocks are mineable with a pickaxe
            for (var block : Registry.BLOCK) {
                if (Registry.BLOCK.getKey(block).getNamespace().equals(Powah.MOD_ID)) {
                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                }
            }
            // However the uraninite ores require at least an iron pickaxe
            tag(BlockTags.NEEDS_IRON_TOOL).addTag(ITags.Blocks.URANINITE_ORE);
        }
    }

    public static class Items extends FabricTagProvider.ItemTagProvider {
        public Items(FabricDataGenerator dataGenerator, BlockTagProvider blockTagProvider) {
            super(dataGenerator, blockTagProvider);
        }

        @Override
        protected void generateTags() {
            // Remove non-dry ice if Forge handles them in the future
            tag(ITags.Items.ICES).addTag(ITags.Items.ICES_ICE).addTag(ITags.Items.ICES_PACKED).addTag(ITags.Items.ICES_BLUE);
            tag(ITags.Items.ICES_ICE).add(net.minecraft.world.item.Items.ICE);
            tag(ITags.Items.ICES_PACKED).add(net.minecraft.world.item.Items.PACKED_ICE);
            tag(ITags.Items.ICES_BLUE).add(net.minecraft.world.item.Items.BLUE_ICE);
            tag(ITags.Items.ICES).addTag(ITags.Items.ICES_DRY);
            tag(ITags.Items.ICES_DRY).add(Blcks.DRY_ICE.get().asItem());

            tag(ITags.Items.URANINITE_ORE).add(Blcks.URANINITE_ORE.get().asItem()).add(Blcks.URANINITE_ORE_POOR.get().asItem()).add(Blcks.URANINITE_ORE_DENSE.get().asItem());
            tag(ITags.Items.URANINITE_ORE).add(Blcks.DEEPSLATE_URANINITE_ORE.get().asItem()).add(Blcks.DEEPSLATE_URANINITE_ORE_POOR.get().asItem()).add(Blcks.DEEPSLATE_URANINITE_ORE_DENSE.get().asItem());
            tag(ConventionalItemTags.ORES).addTag(ITags.Items.URANINITE_ORE);

            // TODO PR TO FABRIC
            //tag(ConventionalItemTags.STORAGE_BLOCKS).add(Blcks.URANINITE.get().asItem());
            tag(ITags.Items.URANINITE_BLOCK).add(Blcks.URANINITE.get().asItem());
            // TODO PR TO FABRIC
            tag(ITags.Items.QUARTZ_BLOCKS).add(net.minecraft.world.item.Items.QUARTZ);

            tag(ITags.Items.URANINITE_RAW).add(Itms.URANINITE_RAW.get());
            // TODO PR TO FABRIC
            //tag(ConventionalItemTags.RAW_ORES).addTag(ITags.Items.URANINITE_RAW);

            // TODO PR TO FABRIC
            //tag(ConventionalItemTags.INGOTS).add(Itms.ENERGIZED_STEEL.get());
            //tag(ConventionalItemTags.GEMS).add(Itms.BLAZING_CRYSTAL.get(), Itms.NIOTIC_CRYSTAL.get(), Itms.SPIRITED_CRYSTAL.get(), Itms.NITRO_CRYSTAL.get());
        }
    }
}
