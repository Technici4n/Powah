package owmii.powah.world.gen;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.config.v2.PowahConfig;

public class Features {
    public static final Holder<PlacedFeature> DRY_ICE = register("dry_ice", Blcks.DRY_ICE, Blcks.DRY_ICE, 17, wg -> wg.dry_ice_veins_per_chunk, 64);
    public static final Holder<PlacedFeature> URANINITE_POOR = register("uraninite_ore_poor", Blcks.URANINITE_ORE_POOR,
            Blcks.DEEPSLATE_URANINITE_ORE_POOR, 5, wg -> wg.poor_uraninite_veins_per_chunk, 64);
    public static final Holder<PlacedFeature> URANINITE = register("uraninite_ore", Blcks.URANINITE_ORE, Blcks.DEEPSLATE_URANINITE_ORE, 4,
            wg -> wg.uraninite_veins_per_chunk, 20);
    public static final Holder<PlacedFeature> URANINITE_DENSE = register("uraninite_ore_dense", Blcks.URANINITE_ORE_DENSE,
            Blcks.DEEPSLATE_URANINITE_ORE_DENSE, 3, wg -> wg.dense_uraninite_veins_per_chunk, 0);

    public static void init() {
        // init static
    }

    private static Holder<PlacedFeature> register(String name, Supplier<Block> block, Supplier<Block> deepslateBlock, int amountPerVein,
            Function<PowahConfig.WorldGen, Integer> veinsPerChunk, int maxY) {
        var id = Powah.id(name);
        var target = List.of(
                OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, block.get().defaultBlockState()),
                OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateBlock.get().defaultBlockState()));
        var conf = new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(target, amountPerVein));
        var confHolder = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, id, conf);
        var placed = new PlacedFeature(Holder.hackyErase(confHolder),
                OrePlacements.commonOrePlacement(
                        veinsPerChunk.apply(Powah.config().worldgen),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(maxY))));
        return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, id, placed);
    }
}
