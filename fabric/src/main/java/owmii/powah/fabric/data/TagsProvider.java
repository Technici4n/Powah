package owmii.powah.fabric.data;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import org.jetbrains.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.item.Itms;

public class TagsProvider {
    public static class Blocks extends FabricTagProvider.BlockTagProvider {

        public Blocks(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            // Remove non-dry ice if Forge handles them in the future
            getOrCreateTagBuilder(ITags.Blocks.ICES).addTag(ITags.Blocks.ICES_ICE).addTag(ITags.Blocks.ICES_PACKED).addTag(ITags.Blocks.ICES_BLUE);
            getOrCreateTagBuilder(ITags.Blocks.ICES_ICE).add(net.minecraft.world.level.block.Blocks.ICE);
            getOrCreateTagBuilder(ITags.Blocks.ICES_PACKED).add(net.minecraft.world.level.block.Blocks.PACKED_ICE);
            getOrCreateTagBuilder(ITags.Blocks.ICES_BLUE).add(net.minecraft.world.level.block.Blocks.BLUE_ICE);
            getOrCreateTagBuilder(ITags.Blocks.ICES).addTag(ITags.Blocks.ICES_DRY);
            getOrCreateTagBuilder(ITags.Blocks.ICES_DRY).add(Blcks.DRY_ICE.get());

            getOrCreateTagBuilder(ITags.Blocks.URANINITE_ORE).add(Blcks.URANINITE_ORE.get()).add(Blcks.URANINITE_ORE_POOR.get())
                    .add(Blcks.URANINITE_ORE_DENSE.get());
            getOrCreateTagBuilder(ITags.Blocks.URANINITE_ORE).add(Blcks.DEEPSLATE_URANINITE_ORE.get()).add(Blcks.DEEPSLATE_URANINITE_ORE_POOR.get())
                    .add(Blcks.DEEPSLATE_URANINITE_ORE_DENSE.get());
            getOrCreateTagBuilder(ConventionalBlockTags.ORES).addTag(ITags.Blocks.URANINITE_ORE);

            // TODO PR TO FABRIC
            // getOrCreateTagBuilder(STORAGE_BLOCKS).add(Blcks.URANINITE.get());
            getOrCreateTagBuilder(ITags.Blocks.URANINITE_BLOCK).add(Blcks.URANINITE.get());

            // All of our blocks are mineable with a pickaxe
            for (var block : BuiltInRegistries.BLOCK) {
                if (BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(Powah.MOD_ID)) {
                    getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                }
            }
            // However the uraninite ores require at least an iron pickaxe
            getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL).addTag(ITags.Blocks.URANINITE_ORE);
        }
    }

    public static class Items extends FabricTagProvider.ItemTagProvider {
        public Items(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture,
                @Nullable BlockTagProvider blockTagProvider) {
            super(output, completableFuture, blockTagProvider);
        }

        @Override
        protected void addTags(HolderLookup.Provider arg) {
            // Remove non-dry ice if Forge handles them in the future
            getOrCreateTagBuilder(ITags.Items.ICES).addTag(ITags.Items.ICES_ICE).addTag(ITags.Items.ICES_PACKED).addTag(ITags.Items.ICES_BLUE);
            getOrCreateTagBuilder(ITags.Items.ICES_ICE).add(net.minecraft.world.item.Items.ICE);
            getOrCreateTagBuilder(ITags.Items.ICES_PACKED).add(net.minecraft.world.item.Items.PACKED_ICE);
            getOrCreateTagBuilder(ITags.Items.ICES_BLUE).add(net.minecraft.world.item.Items.BLUE_ICE);
            getOrCreateTagBuilder(ITags.Items.ICES).addTag(ITags.Items.ICES_DRY);
            getOrCreateTagBuilder(ITags.Items.ICES_DRY).add(Blcks.DRY_ICE.get().asItem());

            getOrCreateTagBuilder(ITags.Items.URANINITE_ORE).add(Blcks.URANINITE_ORE.get().asItem()).add(Blcks.URANINITE_ORE_POOR.get().asItem())
                    .add(Blcks.URANINITE_ORE_DENSE.get().asItem());
            getOrCreateTagBuilder(ITags.Items.URANINITE_ORE).add(Blcks.DEEPSLATE_URANINITE_ORE.get().asItem())
                    .add(Blcks.DEEPSLATE_URANINITE_ORE_POOR.get().asItem())
                    .add(Blcks.DEEPSLATE_URANINITE_ORE_DENSE.get().asItem());
            getOrCreateTagBuilder(ConventionalItemTags.ORES).addTag(ITags.Items.URANINITE_ORE);

            // TODO PR TO FABRIC
            // getOrCreateTagBuilder(ConventionalItemTags.STORAGE_BLOCKS).add(Blcks.URANINITE.get().asItem());
            getOrCreateTagBuilder(ITags.Items.URANINITE_BLOCK).add(Blcks.URANINITE.get().asItem());
            // TODO PR TO FABRIC
            getOrCreateTagBuilder(ITags.Items.QUARTZ_BLOCKS).add(net.minecraft.world.item.Items.QUARTZ);

            getOrCreateTagBuilder(ITags.Items.URANINITE_RAW).add(Itms.URANINITE_RAW.get());
            // TODO PR TO FABRIC
            // getOrCreateTagBuilder(ConventionalItemTags.RAW_ORES).addTag(ITags.Items.URANINITE_RAW);

            // TODO PR TO FABRIC
            // getOrCreateTagBuilder(ConventionalItemTags.INGOTS).add(Itms.ENERGIZED_STEEL.get());
            // getOrCreateTagBuilder(ConventionalItemTags.GEMS).add(Itms.BLAZING_CRYSTAL.get(), Itms.NIOTIC_CRYSTAL.get(),
            // Itms.SPIRITED_CRYSTAL.get(),
            // Itms.NITRO_CRYSTAL.get());

            getOrCreateTagBuilder(ITags.Items.WRENCHES).add(Itms.WRENCH.get());
        }
    }
}
