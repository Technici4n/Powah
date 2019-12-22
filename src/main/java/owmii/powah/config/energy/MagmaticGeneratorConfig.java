package owmii.powah.config.energy;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import owmii.powah.block.generator.magmatic.MagmaticGenerators;

import java.util.List;

public class MagmaticGeneratorConfig {
    public final ForgeConfigSpec.ConfigValue<List<String>> magmaticFluids;
    public final ForgeConfigSpec.BooleanValue magmaticFluidsAPI;


    public final ForgeConfigSpec.ConfigValue[] caps = new ForgeConfigSpec.ConfigValue[MagmaticGenerators.values().length];
    public final ForgeConfigSpec.ConfigValue[] transfers = new ForgeConfigSpec.ConfigValue[MagmaticGenerators.values().length];
    public final ForgeConfigSpec.ConfigValue[] generations = new ForgeConfigSpec.ConfigValue[MagmaticGenerators.values().length];

    public MagmaticGeneratorConfig(ForgeConfigSpec.Builder builder) {
        this.magmaticFluids = builder
                .comment("", "List of fluids used in Magmatic Generator.",
                        "fluid registry name = heat per 100 mb eg: [\"minecraft:lava=10000\", \"examplemod:fluid=1000\"]",
                        "Min = 1, max = " + Integer.MAX_VALUE)
                .define("magmaticFluids", Lists.newArrayList("minecraft:lava=10000"));
        this.magmaticFluidsAPI = builder.comment("Enable this to allow other mods to add their fluids. [default:true]").define("magmaticFluidsAPI", true);

        MagmaticGenerators[] values = MagmaticGenerators.values();
        for (int i = 0; i < values.length; i++) {
            MagmaticGenerators magmaticGenerator = values[i];
            String name = magmaticGenerator.name().toLowerCase();
            String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
            if (i == 0) {
                builder.comment("Range: min = 0, max = " + Integer.MAX_VALUE);
            }
            builder.push(cap + " Magmatic Generator");
            this.caps[i] = builder.define("capacity", "" + magmaticGenerator.capacity);
            this.transfers[i] = builder.define("transfer", "" + magmaticGenerator.transfer);
            this.generations[i] = builder.define("perTick", "" + magmaticGenerator.perTick);
            builder.pop();
        }
    }
}
