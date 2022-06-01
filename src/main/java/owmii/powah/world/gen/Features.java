package owmii.powah.world.gen;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.config.Configs;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Features {
    public static final Map<String, ConfiguredFeature<?, ?>> FEATURES = new HashMap<>();
    public static final ConfiguredFeature<?, ?> ORE_POOR = register("uraninite_ore_poor", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, Blcks.URANINITE_ORE_POOR.defaultBlockState(), 5)).range(64).squared().func_242731_b(Configs.GENERAL.poorUraniniteGenChance.get()));
    public static final ConfiguredFeature<?, ?> ORE = register("uraninite_ore", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, Blcks.URANINITE_ORE.defaultBlockState(), 4)).range(32).squared().func_242731_b(Configs.GENERAL.uraniniteGenChance.get()));
    public static final ConfiguredFeature<?, ?> ORE_DENSE = register("uraninite_ore_dense", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, Blcks.URANINITE_ORE_DENSE.defaultBlockState(), 3)).range(16).squared().func_242731_b(Configs.GENERAL.denseUraniniteGenChance.get()));
    public static final ConfiguredFeature<?, ?> DRY_ICE = register("dry_ice", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, Blcks.DRY_ICE.defaultBlockState(), 17)).range(64).squared().func_242731_b(Configs.GENERAL.dryIceGenChance.get()));

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Feature<?>> event) {
        FEATURES.forEach((id, feature) -> Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Powah.MOD_ID + ":" + id, feature));
    }

    static ConfiguredFeature<?, ?> register(String id, ConfiguredFeature<?, ?> feature) {
        FEATURES.put(id, feature);
        return feature;
    }
}