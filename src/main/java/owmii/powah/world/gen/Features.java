package owmii.powah.world.gen;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;

public class Features {
    // These are defaults for data-gen
    private static final int poor_uraninite_veins_per_chunk = 8;
    private static final int uraninite_veins_per_chunk = 6;
    private static final int dense_uraninite_veins_per_chunk = 3;
    private static final int dry_ice_veins_per_chunk = 9;

    private static final TagKey<Biome> DRY_ICE_BIOME = TagKey.create(Registries.BIOME, Powah.id("has_dry_ice"));

    public static final ResourceKey<PlacedFeature> PLACED_DRY_ICE = ResourceKey.create(Registries.PLACED_FEATURE, Powah.id("dry_ice"));
    public static final ResourceKey<PlacedFeature> PLACED_URANINITE_POOR = ResourceKey.create(Registries.PLACED_FEATURE,
            Powah.id("uraninite_ore_poor"));
    public static final ResourceKey<PlacedFeature> PLACED_URANINITE = ResourceKey.create(Registries.PLACED_FEATURE, Powah.id("uraninite_ore"));
    public static final ResourceKey<PlacedFeature> PLACED_URANINITE_DENSE = ResourceKey.create(Registries.PLACED_FEATURE,
            Powah.id("uraninite_ore_dense"));

    public static final ResourceKey<ConfiguredFeature<?, ?>> DRY_ICE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Powah.id("dry_ice"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> URANINITE_POOR = ResourceKey.create(Registries.CONFIGURED_FEATURE,
            Powah.id("uraninite_ore_poor"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> URANINITE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Powah.id("uraninite_ore"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> URANINITE_DENSE = ResourceKey.create(Registries.CONFIGURED_FEATURE,
            Powah.id("uraninite_ore_dense"));

    public static void initConfiguredFeatures(BootstapContext<ConfiguredFeature<?, ?>> bootstrap) {

        registerConfiguredFeature(bootstrap, DRY_ICE, Blcks.DRY_ICE, Blcks.DRY_ICE, 17);
        registerConfiguredFeature(bootstrap, URANINITE_POOR, Blcks.URANINITE_ORE_POOR,
                Blcks.DEEPSLATE_URANINITE_ORE_POOR, 5);
        registerConfiguredFeature(bootstrap, URANINITE, Blcks.URANINITE_ORE, Blcks.DEEPSLATE_URANINITE_ORE, 4);
        registerConfiguredFeature(bootstrap, URANINITE_DENSE, Blcks.URANINITE_ORE_DENSE,
                Blcks.DEEPSLATE_URANINITE_ORE_DENSE, 3);

    }

    public static void initPlacedFeatures(BootstapContext<PlacedFeature> bootstrap) {

        registerPlacedFeature(bootstrap, PLACED_DRY_ICE, DRY_ICE, dry_ice_veins_per_chunk, 64);
        registerPlacedFeature(bootstrap, PLACED_URANINITE_POOR, URANINITE_POOR,
                poor_uraninite_veins_per_chunk, 64);
        registerPlacedFeature(bootstrap, PLACED_URANINITE, URANINITE,
                uraninite_veins_per_chunk, 20);
        registerPlacedFeature(bootstrap, PLACED_URANINITE_DENSE, URANINITE_DENSE,
                dense_uraninite_veins_per_chunk, 0);

    }

    private static void registerConfiguredFeature(BootstapContext<ConfiguredFeature<?, ?>> bootstrap,
            ResourceKey<ConfiguredFeature<?, ?>> key,
            Supplier<Block> block, Supplier<Block> deepslateBlock,
            int amountPerVein) {
        var target = List.of(
                OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), block.get().defaultBlockState()),
                OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), deepslateBlock.get().defaultBlockState()));
        var conf = new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(target, amountPerVein));
        bootstrap.register(key, conf);
    }

    private static void registerPlacedFeature(BootstapContext<PlacedFeature> bootstrap,
            ResourceKey<PlacedFeature> key,
            ResourceKey<ConfiguredFeature<?, ?>> configuredId,
            int veinsPerChunk, int maxY) {
        var configuredFeature = bootstrap.lookup(Registries.CONFIGURED_FEATURE).get(configuredId)
                .orElseThrow();

        var placed = new PlacedFeature(configuredFeature,
                OrePlacements.commonOrePlacement(
                        veinsPerChunk,
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(maxY))));
        bootstrap.register(key, placed);
    }

    public static void initBiomeModifiers(BootstapContext<BiomeModifier> bootstrap) {
        var biomes = bootstrap.lookup(Registries.BIOME);
        var overworldBiomes = biomes.getOrThrow(BiomeTags.IS_OVERWORLD);
        addFeatureToBiomes(bootstrap, overworldBiomes, PLACED_URANINITE_POOR, GenerationStep.Decoration.UNDERGROUND_ORES);
        addFeatureToBiomes(bootstrap, overworldBiomes, PLACED_URANINITE, GenerationStep.Decoration.UNDERGROUND_ORES);
        addFeatureToBiomes(bootstrap, overworldBiomes, PLACED_URANINITE_DENSE, GenerationStep.Decoration.UNDERGROUND_ORES);

        var dryIceBiomes = biomes.getOrThrow(DRY_ICE_BIOME);
        addFeatureToBiomes(bootstrap, dryIceBiomes, PLACED_DRY_ICE, GenerationStep.Decoration.UNDERGROUND_ORES);
    }

    private static void addFeatureToBiomes(BootstapContext<BiomeModifier> bootstrap,
            HolderSet<Biome> biomes,
            ResourceKey<PlacedFeature> featureKey,
            GenerationStep.Decoration step) {

        // Just name the modifier the same as the feature
        var modifierKey = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, featureKey.location());

        var features = bootstrap.lookup(Registries.PLACED_FEATURE);
        var modifier = new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes,
                HolderSet.direct(features.getOrThrow(featureKey)),
                step);

        bootstrap.register(modifierKey, modifier);
    }
}
