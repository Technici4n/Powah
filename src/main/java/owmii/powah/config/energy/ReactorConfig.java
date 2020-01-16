package owmii.powah.config.energy;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import owmii.powah.block.generator.reactor.Reactors;

import java.util.List;

public class ReactorConfig {
    public final ForgeConfigSpec.ConfigValue<List<String>> coolantFluids;
    public final ForgeConfigSpec.BooleanValue coolantFluidsAPI;

    public final ForgeConfigSpec.ConfigValue[] caps = new ForgeConfigSpec.ConfigValue[Reactors.values().length];
    public final ForgeConfigSpec.ConfigValue[] transfers = new ForgeConfigSpec.ConfigValue[Reactors.values().length];
    public final ForgeConfigSpec.ConfigValue[] generations = new ForgeConfigSpec.ConfigValue[Reactors.values().length];

    public ReactorConfig(ForgeConfigSpec.Builder builder) {
        this.coolantFluids = builder
                .comment("", "List of coolant fluids used in a Reactor.",
                        "Fluid registry name = coldness per mb, eg: [\"minecraft:water=1\", \"examplemod:fluid=-1\"]",
                        "Less number more cold, min = -100, max = 1")
                .define("coolantFluids", Lists.newArrayList("minecraft:water=1"));
        this.coolantFluidsAPI = builder.comment("Enable this to allow other mods to add their coolant fluids. [default:true]").define("coolantFluidsAPI", true);

        Reactors[] values = Reactors.values();
        for (int i = 0; i < values.length; i++) {
            Reactors reactors = values[i];
            String name = reactors.name().toLowerCase();
            String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
            if (i == 0) {
                builder.comment("Range: min = 0, max = " + Integer.MAX_VALUE);
            }
            builder.push(cap + " Reactor");
            this.caps[i] = builder.define("capacity", "" + reactors.capacity);
            this.transfers[i] = builder.define("transfer", "" + reactors.transfer);
            this.generations[i] = builder.define("genFactor", "" + reactors.perTick);
            builder.pop();
        }
    }
}
