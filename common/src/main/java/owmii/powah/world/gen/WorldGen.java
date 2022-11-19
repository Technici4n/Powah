package owmii.powah.world.gen;

import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import owmii.powah.EnvHandler;
import owmii.powah.Powah;

import static owmii.powah.world.gen.Features.*;

public class WorldGen {

    private static final TagKey<Biome> DRY_ICE_BIOME = TagKey.create(Registry.BIOME_REGISTRY, Powah.id("has_dry_ice"));

    public static void init() {
        BiomeModifications.addProperties((ctx, mut) -> {
            if (Powah.config().worldgen.disable_all || !ctx.hasTag(EnvHandler.INSTANCE.getOverworldBiomeTag())) {
                return;
            }

            mut.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, URANINITE_POOR);
            mut.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, URANINITE);
            mut.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, URANINITE_DENSE);

            if (ctx.hasTag(DRY_ICE_BIOME)) {
                mut.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, DRY_ICE);
            }
        });
    }
}
