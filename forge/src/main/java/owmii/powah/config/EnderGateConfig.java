package owmii.powah.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class EnderGateConfig extends EnderCellConfig {
    public EnderGateConfig(ForgeConfigSpec.Builder builder) {
        super(builder,
                new long[]{0, 0, 0, 0, 0, 0, 0},
                new long[]{100, 1400, 5000, 18000, 40000, 90000, 200000},
                new int[]{1, 2, 3, 5, 7, 9, 12}
        );
    }
}
