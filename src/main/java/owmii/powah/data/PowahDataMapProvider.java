package owmii.powah.data;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DataMapProvider;
import owmii.powah.api.FluidCoolantConfig;
import owmii.powah.api.MagmatorFuelValue;
import owmii.powah.api.PassiveHeatSourceConfig;
import owmii.powah.api.SolidCoolantConfig;
import owmii.powah.block.Blcks;
import owmii.powah.item.Itms;
import owmii.powah.recipe.ReactorFuel;

class PowahDataMapProvider extends DataMapProvider {
    public PowahDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void gather() {
        builder(ReactorFuel.DATA_MAP_TYPE)
                .add(Itms.URANINITE, new ReactorFuel(100, 700), false)
                .build();
        builder(FluidCoolantConfig.DATA_MAP_TYPE)
                .add(Tags.Fluids.WATER, new FluidCoolantConfig(1), false)
                .build();

        builder(SolidCoolantConfig.DATA_MAP_TYPE)
                .add(item(Blocks.SNOW_BLOCK), new SolidCoolantConfig(48, -3), false)
                .add(item(Items.SNOWBALL), new SolidCoolantConfig(12, -3), false)
                .add(item(Blocks.ICE), new SolidCoolantConfig(48, -5), false)
                .add(item(Blocks.PACKED_ICE), new SolidCoolantConfig(192, -8), false)
                .add(item(Blocks.BLUE_ICE), new SolidCoolantConfig(568, -17), false)
                .add(item(Blcks.DRY_ICE), new SolidCoolantConfig(712, -32), false)
                .build();

        builder(MagmatorFuelValue.DATA_MAP_TYPE)
                .add(Tags.Fluids.LAVA, new MagmatorFuelValue(10000), false)
                .build();

        builder(PassiveHeatSourceConfig.FLUID_DATA_MAP)
                .add(Tags.Fluids.LAVA, new PassiveHeatSourceConfig(1000), false)
                .build();

        builder(PassiveHeatSourceConfig.BLOCK_DATA_MAP)
                .add(block(Blocks.MAGMA_BLOCK), new PassiveHeatSourceConfig(800), false)
                .add(block(Blcks.BLAZING_CRYSTAL), new PassiveHeatSourceConfig(2800), false)
                .build();
    }

    private static Holder<Item> item(ItemLike item) {
        return BuiltInRegistries.ITEM.wrapAsHolder(item.asItem());
    }

    private static Holder<Block> block(Supplier<Block> block) {
        return BuiltInRegistries.BLOCK.wrapAsHolder(block.get());
    }

    private static Holder<Block> block(Block block) {
        return BuiltInRegistries.BLOCK.wrapAsHolder(block);
    }
}
