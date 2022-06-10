package owmii.powah.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class EnergyCellConfig extends EnergyConfig {
    public EnergyCellConfig(ForgeConfigSpec.Builder builder) {
        super(builder,
                new long[]{100_000L, 1000_000L, 4000_000L, 10_000_000L, 25_000_000L, 60_000_000L, 140_000_000L},
                new long[]{200L, 2500L, 8000L, 20_000L, 50_000L, 100_000L, 400_000L}
        );
    }
}
