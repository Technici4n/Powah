package owmii.powah.world.gen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.powah.block.IBlocks;
import owmii.powah.config.Configs;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Features {
    public static final List<Feature<?>> FEATURES = new ArrayList<>();

    public static void register() {
        if (Configs.GENERAL.oreGen.get()) {
            ForgeRegistries.BIOMES.forEach(biome -> {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, IBlocks.URANINITE_ORE_POOR.getDefaultState(), 5)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(Configs.GENERAL.uraniniteGenChance.get(), 0, 0, 64))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, IBlocks.URANINITE_ORE.getDefaultState(), 4)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(Configs.GENERAL.uraniniteGenChance.get(), 0, 0, 32))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, IBlocks.URANINITE_ORE_DENSE.getDefaultState(), 3)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(Configs.GENERAL.denseUraniniteGenChance.get(), 0, 0, 16))));
                if (biome.getTempCategory().equals(Biome.TempCategory.COLD) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.COLD) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY)) {
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, IBlocks.DRY_ICE.getDefaultState(), 15)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(Configs.GENERAL.dryIceGenChance.get(), 0, 0, 64))));
                }
            });
        }
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