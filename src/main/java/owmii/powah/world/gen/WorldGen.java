package owmii.powah.world.gen;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.checkerframework.checker.signature.qual.SignatureUnknown;
import owmii.powah.config.Configs;

import static owmii.powah.world.gen.Features.*;

@Mod.EventBusSubscriber
public class WorldGen {
    public static void register(RegistryEvent.Register<Feature<?>> event) {
        Features.init();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void gen(BiomeLoadingEvent event) {
        if (Configs.GENERAL.oreGen.get()) {
            BiomeGenerationSettingsBuilder generation = event.getGeneration();
            generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, URANINITE_POOR);
            generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, URANINITE);
            generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, URANINITE_DENSE);
            if (event.getCategory().equals(Biome.BiomeCategory.TAIGA)
                    || event.getCategory().equals(Biome.BiomeCategory.ICY)
                    || event.getCategory().equals(Biome.BiomeCategory.EXTREME_HILLS)) {
                generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, DRY_ICE);
            }
        }
    }
}
