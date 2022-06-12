package owmii.powah.world.gen;

import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;

import static owmii.powah.world.gen.Features.*;

public class WorldGen {
    public static void init() {
        BiomeModifications.addProperties((ctx, mut) -> {
            mut.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, URANINITE_POOR);
            mut.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, URANINITE);
            mut.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, URANINITE_DENSE);

            if (mut.getCategory() == Biome.BiomeCategory.TAIGA
                    || mut.getCategory() == Biome.BiomeCategory.ICY
                    || mut.getCategory() == Biome.BiomeCategory.EXTREME_HILLS) {
                mut.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, DRY_ICE);
            }
        });
    }
}
