package owmii.powah.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class EnergizingConfig extends EnergyConfig {
    public final ForgeConfigSpec.IntValue range;

    public EnergizingConfig(ForgeConfigSpec.Builder builder) {
        super(builder,
                new long[]{10000, 100000, 250000, 800000, 1500000, 4000000, 10000000},
                new long[]{10, 50, 120, 300, 700, 1200, 3000}
        );

        this.range = builder.comment("").defineInRange("range", 4, 1, 32);
    }
}
