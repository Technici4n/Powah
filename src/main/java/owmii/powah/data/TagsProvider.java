package owmii.powah.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;
import owmii.powah.item.Itms;
import owmii.powah.lib.registry.VarReg;

public class TagsProvider {
    public static class Blocks extends BlockTagsProvider {
        public Blocks(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Powah.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            // Remove non-dry ice if Forge handles them in the future
            tag(ITags.Blocks.ICES).addTag(ITags.Blocks.ICES_ICE).addTag(ITags.Blocks.ICES_PACKED).addTag(ITags.Blocks.ICES_BLUE);
            tag(ITags.Blocks.ICES_ICE).add(net.minecraft.world.level.block.Blocks.ICE);
            tag(ITags.Blocks.ICES_PACKED).add(net.minecraft.world.level.block.Blocks.PACKED_ICE);
            tag(ITags.Blocks.ICES_BLUE).add(net.minecraft.world.level.block.Blocks.BLUE_ICE);
            tag(ITags.Blocks.ICES).addTag(ITags.Blocks.ICES_DRY);
            tag(ITags.Blocks.ICES_DRY).add(Blcks.DRY_ICE.get());

            tag(ITags.Blocks.URANINITE_ORE).add(Blcks.URANINITE_ORE.get()).add(Blcks.URANINITE_ORE_POOR.get())
                    .add(Blcks.URANINITE_ORE_DENSE.get());
            tag(ITags.Blocks.URANINITE_ORE).add(Blcks.DEEPSLATE_URANINITE_ORE.get()).add(Blcks.DEEPSLATE_URANINITE_ORE_POOR.get())
                    .add(Blcks.DEEPSLATE_URANINITE_ORE_DENSE.get());
            tag(Tags.Blocks.ORES).addTag(ITags.Blocks.URANINITE_ORE);

            tag(ITags.Blocks.URANINITE_ORE_REGULAR).add(Blcks.URANINITE_ORE.get(), Blcks.DEEPSLATE_URANINITE_ORE.get());
            tag(Tags.Blocks.ORES).addTag(ITags.Blocks.URANINITE_ORE_REGULAR);

            tag(ITags.Blocks.URANINITE_ORE_POOR).add(Blcks.URANINITE_ORE_POOR.get(), Blcks.DEEPSLATE_URANINITE_ORE_POOR.get());
            tag(Tags.Blocks.ORES).addTag(ITags.Blocks.URANINITE_ORE_POOR);

            tag(ITags.Blocks.URANINITE_ORE_DENSE).add(Blcks.URANINITE_ORE_DENSE.get(), Blcks.DEEPSLATE_URANINITE_ORE_DENSE.get());
            tag(Tags.Blocks.ORES).addTag(ITags.Blocks.URANINITE_ORE_DENSE);

            tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(Blcks.DEEPSLATE_URANINITE_ORE.get(),
                    Blcks.DEEPSLATE_URANINITE_ORE_DENSE.get(), Blcks.DEEPSLATE_URANINITE_ORE_POOR.get());
            tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(Blcks.URANINITE.get(), Blcks.URANINITE_ORE_DENSE.get(),
                    Blcks.URANINITE_ORE_POOR.get());

            tag(Tags.Blocks.STORAGE_BLOCKS).add(Blcks.URANINITE.get());
            tag(ITags.Blocks.URANINITE_BLOCK).add(Blcks.URANINITE.get());

            varReg(ITags.Blocks.ENERGY_CELLS, Blcks.ENERGY_CELL);
            varReg(ITags.Blocks.ENDER_CELLS, Blcks.ENDER_CELL);
            varReg(ITags.Blocks.ENERGY_CABLES, Blcks.ENERGY_CABLE);
            varReg(ITags.Blocks.ENDER_GATES, Blcks.ENDER_GATE);
            varReg(ITags.Blocks.ENERGIZING_RODS, Blcks.ENERGIZING_ROD);
            varReg(ITags.Blocks.FURNATORS, Blcks.FURNATOR);
            varReg(ITags.Blocks.MAGMATORS, Blcks.MAGMATOR);
            varReg(ITags.Blocks.THERMO_GENERATORS, Blcks.THERMO_GENERATOR);
            varReg(ITags.Blocks.SOLAR_PANELS, Blcks.SOLAR_PANEL);
            varReg(ITags.Blocks.REACTORS, Blcks.REACTOR);
            varReg(ITags.Blocks.PLAYER_TRANSMITTERS, Blcks.PLAYER_TRANSMITTER);
            varReg(ITags.Blocks.ENERGY_HOPPERS, Blcks.ENERGY_HOPPER);
            varReg(ITags.Blocks.ENERGY_DISCHARGERS, Blcks.ENERGY_DISCHARGER);

            // All of our blocks are mineable with a pickaxe
            for (var block : BuiltInRegistries.BLOCK) {
                if (BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(Powah.MOD_ID)) {
                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                }
            }
            // However the uraninite ores require at least an iron pickaxe
            tag(BlockTags.NEEDS_IRON_TOOL).addTag(ITags.Blocks.URANINITE_ORE);
        }

        private void varReg(TagKey<Block> tagKey, VarReg<Tier, Block> varReg) {
            tag(tagKey).add(varReg.getArr(Block[]::new));
        }
    }

    public static class Items extends ItemTagsProvider {
        public Items(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockTagProvider,
                ExistingFileHelper existingFileHelper) {
            super(output, provider, blockTagProvider, Powah.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider arg) {
            tag(ITags.Items.URANINITE_RAW).add(Itms.URANINITE_RAW.get());
            tag(Tags.Items.RAW_MATERIALS).addTag(ITags.Items.URANINITE_RAW);

            tag(Tags.Items.INGOTS).add(Itms.ENERGIZED_STEEL.get());
            tag(Tags.Items.GEMS).add(Itms.BLAZING_CRYSTAL.get(), Itms.NIOTIC_CRYSTAL.get(), Itms.SPIRITED_CRYSTAL.get(), Itms.NITRO_CRYSTAL.get());

            tag(ITags.Items.WRENCHES).add(Itms.WRENCH.get());

            tag(ITags.Items.QUARTZ_BLOCKS).add(net.minecraft.world.item.Items.QUARTZ_BLOCK);

            // BlockItem tags below

            // Remove non-dry ice if Forge handles them in the future
            copy(ITags.Blocks.ICES, ITags.Items.ICES);
            copy(ITags.Blocks.ICES_ICE, ITags.Items.ICES_ICE);
            copy(ITags.Blocks.ICES_PACKED, ITags.Items.ICES_PACKED);
            copy(ITags.Blocks.ICES_BLUE, ITags.Items.ICES_BLUE);
            copy(ITags.Blocks.ICES_DRY, ITags.Items.ICES_DRY);

            copy(ITags.Blocks.URANINITE_ORE, ITags.Items.URANINITE_ORE);
            copy(ITags.Blocks.URANINITE_ORE_REGULAR, ITags.Items.URANINITE_ORE_REGULAR);
            copy(ITags.Blocks.URANINITE_ORE_DENSE, ITags.Items.URANINITE_ORE_DENSE);
            copy(ITags.Blocks.URANINITE_ORE_POOR, ITags.Items.URANINITE_ORE_POOR);
            copy(Tags.Blocks.ORES, Tags.Items.ORES);
            copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE);
            copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE);

            copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
            copy(ITags.Blocks.URANINITE_BLOCK, ITags.Items.URANINITE_BLOCK);

            copy(ITags.Blocks.ENERGY_CELLS, ITags.Items.ENERGY_CELLS);
            copy(ITags.Blocks.ENDER_CELLS, ITags.Items.ENDER_CELLS);
            copy(ITags.Blocks.ENERGY_CABLES, ITags.Items.ENERGY_CABLES);
            copy(ITags.Blocks.ENDER_GATES, ITags.Items.ENDER_GATES);
            copy(ITags.Blocks.ENERGIZING_RODS, ITags.Items.ENERGIZING_RODS);
            copy(ITags.Blocks.FURNATORS, ITags.Items.FURNATORS);
            copy(ITags.Blocks.MAGMATORS, ITags.Items.MAGMATORS);
            copy(ITags.Blocks.THERMO_GENERATORS, ITags.Items.THERMO_GENERATORS);
            copy(ITags.Blocks.SOLAR_PANELS, ITags.Items.SOLAR_PANELS);
            copy(ITags.Blocks.REACTORS, ITags.Items.REACTORS);
            copy(ITags.Blocks.PLAYER_TRANSMITTERS, ITags.Items.PLAYER_TRANSMITTERS);
            copy(ITags.Blocks.ENERGY_HOPPERS, ITags.Items.ENERGY_HOPPERS);
            copy(ITags.Blocks.ENERGY_DISCHARGERS, ITags.Items.ENERGY_DISCHARGERS);
        }
    }
}
