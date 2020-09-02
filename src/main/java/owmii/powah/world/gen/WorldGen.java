package owmii.powah.world.gen;

import net.minecraft.world.biome.BiomeGenerationSettings;
import owmii.powah.config.Configs;

import java.util.ArrayList;
import java.util.List;

public class WorldGen {
    public static final List<BiomeGenerationSettings.Builder> BUILDERS = new ArrayList<>();

    public static void generateOres(BiomeGenerationSettings.Builder builder) {
        if (Configs.GENERAL.oreGen.get()) {
            BUILDERS.add(builder);
        }
    }
}
