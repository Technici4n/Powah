package owmii.powah.world.gen;

import static owmii.powah.world.gen.Features.*;

import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import owmii.powah.EnvHandler;
import owmii.powah.Powah;

public class WorldGen {

    private static final TagKey<Biome> DRY_ICE_BIOME = TagKey.create(Registries.BIOME, Powah.id("has_dry_ice"));

    public static void init() {
        BiomeModifications.addProperties((ctx, mut) -> {
            if (Powah.config().worldgen.disable_all || !ctx.hasTag(EnvHandler.INSTANCE.getOverworldBiomeTag())) {
                return;
            }

            mut.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_URANINITE_POOR);
            mut.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_URANINITE);
            mut.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_URANINITE_DENSE);

            if (ctx.hasTag(DRY_ICE_BIOME)) {
                mut.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_DRY_ICE);
            }
        });
    }
}
