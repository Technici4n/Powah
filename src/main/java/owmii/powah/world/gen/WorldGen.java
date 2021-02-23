package owmii.powah.world.gen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
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
            generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ORE_POOR);
            generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ORE);
            generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ORE_DENSE);
            if (event.getCategory().equals(Biome.Category.TAIGA)
                    || event.getCategory().equals(Biome.Category.ICY)
                    || event.getCategory().equals(Biome.Category.EXTREME_HILLS)) {
                generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, DRY_ICE);
            }
        }
    }
}
