package owmii.powah.config.generator;

import net.minecraftforge.common.ForgeConfigSpec;

public class ReactorConfig extends GeneratorConfig {
    public ReactorConfig(ForgeConfigSpec.Builder builder) {
        super(builder,
                new long[]{1000000L, 4000000L, 10000000L, 18000000L, 30000000L, 50000000L, 80000000L},
                new long[]{10000L, 50000L, 160000L, 100000L, 140000L, 400000L, 800000L},
                new long[]{100L, 400L, 840L, 1200L, 2700L, 3800L, 5400L}
        );
    }
}
