package owmii.powah.world.gen;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.config.Configs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

// TODO: deepslate ores, one day
// The configs are probably not going to work thanks to Forge loading them too late. (Thanks Forge!)
public class Features {
    public static final Holder<PlacedFeature> DRY_ICE = register("dry_ice", Blcks.URANINITE_ORE_POOR, 17, Configs.GENERAL.poorUraniniteGenChance, 64);
    public static final Holder<PlacedFeature> URANINITE_POOR = register("uraninite_ore_poor", Blcks.URANINITE_ORE_POOR, 5, Configs.GENERAL.poorUraniniteGenChance, 64);
    public static final Holder<PlacedFeature> URANINITE = register("uraninite_ore", Blcks.URANINITE_ORE, 4, Configs.GENERAL.uraniniteGenChance, 20);
    public static final Holder<PlacedFeature> URANINITE_DENSE = register("uraninite_ore_dense", Blcks.URANINITE_ORE_DENSE, 3, Configs.GENERAL.denseUraniniteGenChance, 0);

    public static void init() {
        // init static
    }

    private static Holder<PlacedFeature> register(String name, Supplier<Block> block, int amountPerVein, ForgeConfigSpec.IntValue veinsPerChunk, int maxY) {
        var id = Powah.id(name);
        var target = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, block.get().defaultBlockState()));
        var conf = new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(target, amountPerVein));
        var confHolder = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, id, conf);
        var placed = new PlacedFeature(Holder.hackyErase(confHolder),
                OrePlacements.commonOrePlacement(
                        veinsPerChunk.get(),
                        HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(maxY))));
        return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, id, placed);
    }
}