package owmii.powah.config.energy;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import owmii.powah.block.generator.thermoelectric.ThermoGenerators;

import java.util.List;

public class ThermoGeneratorConfig {
    public final ForgeConfigSpec.ConfigValue<List<String>> heatBlocks;
    public final ForgeConfigSpec.BooleanValue heatBlocksAPI;

    public final ForgeConfigSpec.ConfigValue<List<String>> coolantFluids;
    public final ForgeConfigSpec.BooleanValue coolantFluidsAPI;

    public final ForgeConfigSpec.ConfigValue[] caps = new ForgeConfigSpec.ConfigValue[ThermoGenerators.values().length];
    public final ForgeConfigSpec.ConfigValue[] transfers = new ForgeConfigSpec.ConfigValue[ThermoGenerators.values().length];
    public final ForgeConfigSpec.ConfigValue[] generations = new ForgeConfigSpec.ConfigValue[ThermoGenerators.values().length];

    public ThermoGeneratorConfig(ForgeConfigSpec.Builder builder) {
        this.heatBlocks = builder
                .comment("", "List of heat blocks used under Thermoelectric Generator.",
                        "Block registry name = heat, eg: [\"minecraft:lava=1000\", \"minecraft:magma_block=1200\"]",
                        "Min = 1, max = " + Integer.MAX_VALUE)
                .define("heatBlocks", Lists.newArrayList("minecraft:lava=1000", "minecraft:magma_block=1200"));
        this.heatBlocksAPI = builder.comment("Enable this to allow other mods to add their heat blocks. [default:true]").define("heatBlocksAPI", true);

        this.coolantFluids = builder
                .comment("", "List of coolant fluids used in Thermoelectric Generator.",
                        "Fluid registry name = cooling per mb, eg: [\"minecraft:water=1\", \"examplemod:fluid=-1\"]",
                        "Less number more cold, min = -100, max = 1")
                .define("coolantFluids", Lists.newArrayList("minecraft:water=1"));
        this.coolantFluidsAPI = builder.comment("Enable this to allow other mods to add their coolant fluids. [default:true]").define("coolantFluidsAPI", true);


        ThermoGenerators[] values = ThermoGenerators.values();
        for (int i = 0; i < values.length; i++) {
            ThermoGenerators generators = values[i];
            String name = generators.name().toLowerCase();
            String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
            if (i == 0) {
                builder.comment("Range: min = 0, max = " + Integer.MAX_VALUE);
            }
            builder.push(cap + " Thermoelectric Generator");
            this.caps[i] = builder.define("capacity", "" + generators.capacity);
            this.transfers[i] = builder.define("transfer", "" + generators.transfer);
            this.generations[i] = builder.define("perTick", "" + generators.perTick);
            builder.pop();
        }
    }
}
