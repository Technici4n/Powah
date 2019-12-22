package owmii.powah.config.energy;

import net.minecraftforge.common.ForgeConfigSpec;
import owmii.powah.block.generator.furnator.Furnators;

public class FurnatorConfig {
    public final ForgeConfigSpec.IntValue fuelEnergy;
    public final ForgeConfigSpec.ConfigValue[] caps = new ForgeConfigSpec.ConfigValue[Furnators.values().length];
    public final ForgeConfigSpec.ConfigValue[] transfers = new ForgeConfigSpec.ConfigValue[Furnators.values().length];
    public final ForgeConfigSpec.ConfigValue[] generations = new ForgeConfigSpec.ConfigValue[Furnators.values().length];

    public FurnatorConfig(ForgeConfigSpec.Builder builder) {
        this.fuelEnergy = builder.comment("Energy per solid fuel tick. [default: 30]", "Eg: Coal has 1600 fuel tick, so will generate 48000 FE.")
                .defineInRange("fuelEnergyBase", 30, 1, Integer.MAX_VALUE);

        builder.comment("", "Range: min = 0, max = " + Integer.MAX_VALUE);
        Furnators[] values = Furnators.values();
        for (int i = 0; i < values.length; i++) {
            Furnators furnator = values[i];
            String name = furnator.name().toLowerCase();
            String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
            builder.push(cap + " Furnator");
            this.caps[i] = builder.define("capacity", "" + furnator.capacity);
            this.transfers[i] = builder.define("transfer", "" + furnator.transfer);
            this.generations[i] = builder.define("perTick", "" + furnator.perTick);
            builder.pop();
        }
    }
}
