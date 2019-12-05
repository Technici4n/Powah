package zeroneye.powah.config.energy;

import net.minecraftforge.common.ForgeConfigSpec;
import zeroneye.powah.block.cable.Cables;

public class CableConfig {
    public final ForgeConfigSpec.ConfigValue[] transfers = new ForgeConfigSpec.ConfigValue[Cables.values().length];

    public CableConfig(ForgeConfigSpec.Builder builder) {
        Cables[] values = Cables.values();
        for (int i = 0; i < values.length; i++) {
            Cables cable = values[i];
            String name = cable.name().toLowerCase();
            String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
            if (i == 0) {
                builder.comment("Range: min = 0, max = " + Integer.MAX_VALUE);
            }
            builder.push(cap + " Energy Cable");
            this.transfers[i] = builder.define("transfer", "" + cable.transfer);
            builder.pop();
        }
    }
}
