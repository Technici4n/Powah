package owmii.powah.config;

import net.minecraftforge.common.ForgeConfigSpec;
import owmii.powah.block.Tier;

public class EnergyCableConfig extends EnergyConfig {
    public EnergyCableConfig(ForgeConfigSpec.Builder builder) {
        super(builder,
                new long[]{0, 0, 0, 0, 0, 0, 0},
                new long[]{100, 1400, 5000, 18000, 40000, 90000, 200000}
        );
    }

    @Override
    public long getCapacity(Tier variant) {
        return 0;
    }
}
