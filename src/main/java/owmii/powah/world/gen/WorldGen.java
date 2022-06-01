package owmii.powah.world.gen;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.powah.config.Configs;

import static owmii.powah.world.gen.Features.*;

@Mod.EventBusSubscriber
public class WorldGen {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void gen(BiomeLoadingEvent event) {
        if (Configs.GENERAL.oreGen.get()) {
            BiomeGenerationSettingsBuilder generation = event.getGeneration();
            generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_POOR);
            generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE);
            generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_DENSE);
            if (event.getCategory().equals(Biome.BiomeCategory.TAIGA)
                    || event.getCategory().equals(Biome.BiomeCategory.ICY)
                    || event.getCategory().equals(Biome.BiomeCategory.EXTREME_HILLS)) {
                generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, DRY_ICE);
            }
        }
    }
}
