package zeroneye.powah.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    public static final Config GENERAL;
    public static final ForgeConfigSpec CONFIG_SPEC;

    public final ForgeConfigSpec.BooleanValue capacitor_blazing;
    public final ForgeConfigSpec.BooleanValue capacitor_niotic;
    public final ForgeConfigSpec.BooleanValue capacitor_spirited;
    public final ForgeConfigSpec.BooleanValue player_aerial_pearl;

    public Config(ForgeConfigSpec.Builder builder) {
        this.capacitor_blazing = builder.comment("", "Enable this to get Blazing Capacitor by right clicking a blaze with a Large Basic Capacitor. [default:true]").define("capacitor_blazing", true);
        this.capacitor_niotic = builder.comment("", "Enable this to get Niotic Capacitor by right clicking a Diamond Ore with a Blazing Capacitor. [default:true]").define("capacitor_niotic", true);
        this.capacitor_spirited = builder.comment("", "Enable this to get Spirited Capacitor by right clicking an Emerald Ore with a Niotic Capacitor. [default:true]").define("capacitor_spirited", true);
        this.player_aerial_pearl = builder.comment("", "Enable this to get Player Aerial Pearl by right clicking a Zombie or Husk with a Aerial Pearl. [default:true]").define("player_aerial_pearl", true);
    }

    static {
        final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
        CONFIG_SPEC = specPair.getRight();
        GENERAL = specPair.getLeft();
    }
}
