package owmii.powah.config.energy;

import net.minecraftforge.common.ForgeConfigSpec;
import owmii.powah.block.generator.panel.solar.SolarPanels;

public class SolarPanelConfig {
    public final ForgeConfigSpec.ConfigValue[] caps = new ForgeConfigSpec.ConfigValue[SolarPanels.values().length];
    public final ForgeConfigSpec.ConfigValue[] transfers = new ForgeConfigSpec.ConfigValue[SolarPanels.values().length];
    public final ForgeConfigSpec.ConfigValue[] generations = new ForgeConfigSpec.ConfigValue[SolarPanels.values().length];

    public SolarPanelConfig(ForgeConfigSpec.Builder builder) {
        SolarPanels[] values = SolarPanels.values();
        for (int i = 0; i < values.length; i++) {
            SolarPanels panel = values[i];
            String name = panel.name().toLowerCase();
            String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
            if (i == 0) {
                builder.comment("Range: min = 0, max = " + Integer.MAX_VALUE);
            }
            builder.push(cap + " Solar Panel");
            this.caps[i] = builder.define("capacity", "" + panel.capacity);
            this.transfers[i] = builder.define("transfer", "" + panel.transfer);
            this.generations[i] = builder.define("perTick", "" + panel.perTick);
            builder.pop();
        }
    }
}
