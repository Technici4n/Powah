package owmii.powah.world.gen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.powah.block.IBlocks;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IFeatures {
    public static final List<Feature<?>> FEATURES = new ArrayList<>();

    public static void register() {
        Biome.BIOMES.forEach(biome -> {
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, IBlocks.URANINITE_ORE_POOR.getDefaultState(), 9), Placement.COUNT_RANGE, new CountRangeConfig(11, 0, 0, 64)));
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, IBlocks.URANINITE_ORE.getDefaultState(), 7), Placement.COUNT_RANGE, new CountRangeConfig(7, 0, 0, 32)));
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, IBlocks.URANINITE_ORE_DENSE.getDefaultState(), 4), Placement.COUNT_RANGE, new CountRangeConfig(3, 0, 0, 16)));
        });
    }

    static Feature<NoFeatureConfig> register(String id, Feature<NoFeatureConfig> feature) {
        feature.setRegistryName(id);
        FEATURES.add(feature);
        return feature;
    }

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Feature<?>> event) {
        FEATURES.forEach(feature -> event.getRegistry().register(feature));
    }
}