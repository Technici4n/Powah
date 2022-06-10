package owmii.powah.config.generator;

import net.minecraftforge.common.ForgeConfigSpec;

public class FurnatorConfig extends GeneratorConfig {
    public FurnatorConfig(ForgeConfigSpec.Builder builder) {
        super(builder,
                new long[]{10000L, 100000L, 500000L, 900000L, 2000000L, 5000000L, 10000000L},
                new long[]{80L, 200L, 700L, 1200L, 5000L, 7000L, 10000L},
                new long[]{15L, 70L, 180L, 400L, 900L, 2000L, 4800L}
        );
    }
}
