package owmii.powah.config.generator;

import net.minecraftforge.common.ForgeConfigSpec;

public class ThermoConfig extends GeneratorConfig {
    public ThermoConfig(ForgeConfigSpec.Builder builder) {
        super(builder,
                new long[]{5000L, 25000L, 100000L, 500000L, 1000000L, 2500000L, 8000000L},
                new long[]{50L, 150L, 300L, 700L, 1000L, 2000L, 4000L},
                new long[]{3L, 8L, 18L, 42L, 90L, 200L, 800L}
        );
    }
}