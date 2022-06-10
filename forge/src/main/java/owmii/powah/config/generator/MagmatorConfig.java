package owmii.powah.config.generator;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class MagmatorConfig extends GeneratorConfig {
    public final ForgeConfigSpec.ConfigValue<List<String>> magmaticFluids;
    public final ForgeConfigSpec.BooleanValue magmaticFluidsAPI;

    public MagmatorConfig(ForgeConfigSpec.Builder builder) {
        super(builder,
                new long[]{10000L, 100000L, 500000L, 900000L, 2000000L, 5000000L, 10000000L},
                new long[]{80L, 200L, 700L, 1200L, 5000L, 7000L, 10000L},
                new long[]{15L, 70L, 180L, 400L, 900L, 2000L, 4800L}
        );

        this.magmaticFluids = builder
                .comment("", "List of fluids used in the Magmator.",
                        "fluid registry name = heat per 100 mb eg: [\"minecraft:lava=10000\", \"examplemod:fluid=1000\"]",
                        "Min = 1, max = 900000000")
                .define("magmaticFluids", Lists.newArrayList("minecraft:lava=10000"));
        this.magmaticFluidsAPI = builder.comment("Enable this to allow other mods to add their fluids. [default:true]").define("magmaticFluidsAPI", true);
    }
}
