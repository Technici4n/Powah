package zeroneye.powah.config.energy;

import net.minecraftforge.common.ForgeConfigSpec;
import zeroneye.powah.block.energizing.EnergizingRods;

public class EnergizingConfig {
    public final ForgeConfigSpec.IntValue range;

    public final ForgeConfigSpec.ConfigValue[] caps = new ForgeConfigSpec.ConfigValue[EnergizingRods.values().length];
    public final ForgeConfigSpec.ConfigValue[] transfers = new ForgeConfigSpec.ConfigValue[EnergizingRods.values().length];
    public final ForgeConfigSpec.ConfigValue[] energizingSpeed = new ForgeConfigSpec.ConfigValue[EnergizingRods.values().length];

    public EnergizingConfig(ForgeConfigSpec.Builder builder) {
        EnergizingRods[] values = EnergizingRods.values();

        this.range = builder.comment("Range for every direction, Eg: range of 4 = 9X9, range of 1 = 3X3 etc..., [default: 4]").defineInRange("range", 4, 1, 16);

        builder.comment("", "Range: min = 0, max = " + Integer.MAX_VALUE);
        for (int i = 0; i < values.length; i++) {
            EnergizingRods rods = values[i];
            String name = rods.name().toLowerCase();
            String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
            builder.push(cap + " Energizing Rod");
            this.caps[i] = builder.define("capacity", "" + rods.capacity);
            this.transfers[i] = builder.define("transfer", "" + rods.transfer);
            this.energizingSpeed[i] = builder.define("energizing", "" + rods.energizingSpeed);
            builder.pop();
        }
    }

}
