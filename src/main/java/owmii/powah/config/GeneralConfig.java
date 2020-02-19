package owmii.powah.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class GeneralConfig {
    public final ForgeConfigSpec.BooleanValue player_aerial_pearl;
    public final ForgeConfigSpec.BooleanValue binding_card_dim;
    public final ForgeConfigSpec.BooleanValue lens_of_ender;


    public final ForgeConfigSpec.ConfigValue<List<String>> heatBlocks;
    public final ForgeConfigSpec.BooleanValue heatBlocksAPI;

    public final ForgeConfigSpec.ConfigValue<List<String>> coolantFluids;
    public final ForgeConfigSpec.BooleanValue coolantFluidsAPI;

    public final ForgeConfigSpec.LongValue fuelTicks;

    public final ForgeConfigSpec.BooleanValue oreGen;
    public final ForgeConfigSpec.IntValue poorUraniniteGenChance;
    public final ForgeConfigSpec.IntValue uraniniteGenChance;
    public final ForgeConfigSpec.IntValue denseUraniniteGenChance;
    public final ForgeConfigSpec.IntValue dryIceGenChance;

    public GeneralConfig(ForgeConfigSpec.Builder builder) {
        this.player_aerial_pearl = builder.comment("", "Enable this to get Player Aerial Pearl by right clicking a Zombie or Husk with a Aerial Pearl. [default:true]")
                .define("player_aerial_pearl", true);
        this.binding_card_dim = builder.comment("", "Enable this to get Dimensional Binding card by right clicking an Enderman or Endermite with a Binding card. [default:true]")
                .define("binding_card_dim", true);
        this.lens_of_ender = builder.comment("", "Enable this to get Lens Of Ender by right clicking an Enderman or Endermite with a Photoelectric Pane. [default:true]")
                .define("lens_of_ender", true);

        builder.push("World Gen");
        this.oreGen = builder.comment("Enable/Disable ore generation. [default:true]").define("oreGen", true);
        this.poorUraniniteGenChance = builder.comment("Poor Uraninite Ore generation chance").defineInRange("poorUraniniteGenChance", 8, 1, 64);
        this.uraniniteGenChance = builder.comment("Uraninite Ore generation chance").defineInRange("uraniniteGenChance", 6, 1, 64);
        this.denseUraniniteGenChance = builder.comment("Dense Uraninite Ore generation chance").defineInRange("denseUraniniteGenChance", 3, 1, 64);
        this.dryIceGenChance = builder.comment("Dry Ice Ore generation chance").defineInRange("dryIceGenChance", 9, 1, 64);
        builder.pop();

        builder.push("Materials");
        this.heatBlocks = builder
                .comment("", "List of heat source blocks used under Thermo Generator.",
                        "Block registry name = heat, eg: [\"minecraft:lava=1000\", \"minecraft:magma_block=800\"]",
                        "Min = 1, max = 900000000")
                .define("heatSources", Lists.newArrayList("minecraft:lava=1000", "minecraft:magma_block=800", "powah:blazing_crystal_block=2800"));
        this.heatBlocksAPI = builder.comment("Enable this to allow other mods to add their heat source blocks. [default:true]").define("heatBlocksAPI", true);

        this.coolantFluids = builder
                .comment("", "List of coolant fluids used in the Reactor and the Thermo Generator.",
                        "Fluid registry name = cooling per mb, eg: [\"minecraft:water=1\", \"examplemod:fluid=-1\"]",
                        "Less number more cold, min = -100, max = 1")
                .define("coolantFluids", Lists.newArrayList("minecraft:water=1"));
        this.coolantFluidsAPI = builder.comment("Enable this to allow other mods to add their coolant fluids. [default:true]").define("coolantFluidsAPI", true);

        this.fuelTicks = builder.comment("Energy per solid fuel tick. [default: 30]", "Eg: Coal has 1600 fuel tick, so will generate 48000 FE.")
                .defineInRange("solidFuelEnergyBase", 30L, 1L, 1000000L);
        builder.pop();
    }
}
