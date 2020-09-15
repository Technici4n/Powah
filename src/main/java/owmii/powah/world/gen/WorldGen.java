package owmii.powah.world.gen;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.config.Configs;

@Mod.EventBusSubscriber
public class WorldGen {
    public static final ConfiguredFeature<?, ?> URANINITE_ORE_POOR = register("uraninite_ore_poor", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, Blcks.URANINITE_ORE_POOR.getDefaultState(), 5)).func_242733_d(64).func_242728_a().func_242731_b(Configs.GENERAL.poorUraniniteGenChance.get()));
    public static final ConfiguredFeature<?, ?> URANINITE_ORE = register("uraninite_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, Blcks.URANINITE_ORE.getDefaultState(), 4)).func_242733_d(32).func_242728_a().func_242731_b(Configs.GENERAL.uraniniteGenChance.get()));
    public static final ConfiguredFeature<?, ?> URANINITE_ORE_DENSE = register("uraninite_ore_dense", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, Blcks.URANINITE_ORE_DENSE.getDefaultState(), 3)).func_242733_d(16).func_242728_a().func_242731_b(Configs.GENERAL.denseUraniniteGenChance.get()));
    public static final ConfiguredFeature<?, ?> DRY_ICE = register("dry_ice", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, Blcks.DRY_ICE.getDefaultState(), 17)).func_242733_d(64).func_242728_a().func_242731_b(Configs.GENERAL.dryIceGenChance.get()));


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void gen(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        generation.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, URANINITE_ORE_POOR);
        generation.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, URANINITE_ORE);
        generation.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, URANINITE_ORE_DENSE);
        if (event.getCategory().equals(Biome.Category.TAIGA)
                || event.getCategory().equals(Biome.Category.ICY)
                || event.getCategory().equals(Biome.Category.EXTREME_HILLS)) {
            generation.func_242513_a(GenerationStage.Decoration.UNDERGROUND_ORES, DRY_ICE);
        }
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.field_243653_e, Powah.MOD_ID + ":" + name, configuredFeature);
    }
}
