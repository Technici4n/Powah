package owmii.powah.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class GeneralConfig {
    public final ForgeConfigSpec.ConfigValue<List<String>> heatBlocks;
    public final ForgeConfigSpec.BooleanValue heatBlocksAPI;

    public final ForgeConfigSpec.ConfigValue<List<String>> coolantFluids;
    public final ForgeConfigSpec.BooleanValue coolantFluidsAPI;

    public GeneralConfig(ForgeConfigSpec.Builder builder) {
        this.heatBlocks = builder
                .comment("", "List of heat source blocks used under Thermo Generator.",
                        "Block registry name = heat, eg: [\"minecraft:lava=1000\", \"minecraft:magma_block=800\"]",
                        "Min = 1, max = 900000000")
                .define("heatSources", Lists.newArrayList("minecraft:lava=1000", "minecraft:magma_block=800"));
        this.heatBlocksAPI = builder.comment("Enable this to allow other mods to add their heat source blocks. [default:true]").define("heatBlocksAPI", true);

        this.coolantFluids = builder
                .comment("", "List of coolant fluids used in Thermoelectric Generator.",
                        "Fluid registry name = cooling per mb, eg: [\"minecraft:water=1\", \"examplemod:fluid=-1\"]",
                        "Less number more cold, min = -100, max = 1")
                .define("coolantFluids", Lists.newArrayList("minecraft:water=1"));
        this.coolantFluidsAPI = builder.comment("Enable this to allow other mods to add their coolant fluids. [default:true]").define("coolantFluidsAPI", true);
    }
}
