package owmii.powah.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import owmii.powah.Powah;
import owmii.powah.config.energy.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    public static final Marker MARKER = new MarkerManager.Log4jMarker("CONFIG");
    public static final General GENERAL;
    private static final ForgeConfigSpec GENERAL_CONFIG_SPEC;

    public static final CellConfig CELL_CONFIG;
    private static final ForgeConfigSpec CELL_CONFIG_SPEC;

    public static final FurnatorConfig FURNATOR_CONFIG;
    private static final ForgeConfigSpec FURNATOR_CONFIG_SPEC;

    public static final MagmaticGeneratorConfig MAGMATIC_GENERATOR;
    private static final ForgeConfigSpec MAGMATIC_GENERATOR_CONFIG_SPEC;

    public static final SolarPanelConfig SOLAR_PANEL;
    private static final ForgeConfigSpec SOLAR_PANEL_CONFIG_SPEC;

    public static final ThermoGeneratorConfig THERMO_GEN_CONFIG;
    private static final ForgeConfigSpec THERMO_GEN_CONFIG_SPEC;

    public static final CableConfig CABLE_CONFIG;
    private static final ForgeConfigSpec CABLE_CONFIG_SPEC;

    public static final EnergizingConfig ENERGIZING_CONFIG;
    private static final ForgeConfigSpec ENERGIZING_CONFIG_SPEC;

    public static final MiscEnergyConfig MISC_ENERGY;
    private static final ForgeConfigSpec MISC_ENERGY_CONFIG_SPEC;

    public static void setup() {
        Path dir = FMLPaths.CONFIGDIR.get();
        Path configDir = Paths.get(dir.toAbsolutePath().toString(), Powah.MOD_ID);
        String energyFolder = Powah.MOD_ID + "/energy";
        Path energyPath = Paths.get(configDir.toAbsolutePath().toString(), "energy");
        try {
            Files.createDirectories(energyPath);
        } catch (Exception ignored) {
        }
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GENERAL_CONFIG_SPEC, Powah.MOD_ID + "/general_common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CELL_CONFIG_SPEC, energyFolder + "/energy_cell.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FURNATOR_CONFIG_SPEC, energyFolder + "/furnator.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MAGMATIC_GENERATOR_CONFIG_SPEC, energyFolder + "/magmatic_generator.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SOLAR_PANEL_CONFIG_SPEC, energyFolder + "/solar_panel.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, THERMO_GEN_CONFIG_SPEC, energyFolder + "/thermoelectric_generator.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CABLE_CONFIG_SPEC, energyFolder + "/energy_cable.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ENERGIZING_CONFIG_SPEC, energyFolder + "/Energizing.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MISC_ENERGY_CONFIG_SPEC, energyFolder + "/misc.toml");
    }

    public static class General {
        public final ForgeConfigSpec.BooleanValue capacitor_blazing;
        public final ForgeConfigSpec.BooleanValue capacitor_niotic;
        public final ForgeConfigSpec.BooleanValue capacitor_spirited;
        public final ForgeConfigSpec.BooleanValue player_aerial_pearl;

        public General(ForgeConfigSpec.Builder builder) {
            this.capacitor_blazing = builder.comment("", "Enable this to get Blazing Capacitor by right clicking a blaze with a Large Basic Capacitor. [default:true]").define("capacitor_blazing", true);
            this.capacitor_niotic = builder.comment("", "Enable this to get Niotic Capacitor by right clicking a Diamond Ore with a Blazing Capacitor. [default:true]").define("capacitor_niotic", true);
            this.capacitor_spirited = builder.comment("", "Enable this to get Spirited Capacitor by right clicking an Emerald Ore with a Niotic Capacitor. [default:true]").define("capacitor_spirited", true);
            this.player_aerial_pearl = builder.comment("", "Enable this to get Player Aerial Pearl by right clicking a Zombie or Husk with a Aerial Pearl. [default:true]").define("player_aerial_pearl", true);
        }
    }

    static {
        final Pair<General, ForgeConfigSpec> generalForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(General::new);
        GENERAL = generalForgeConfigSpecPair.getLeft();
        GENERAL_CONFIG_SPEC = generalForgeConfigSpecPair.getRight();
        final Pair<CellConfig, ForgeConfigSpec> cellsForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(CellConfig::new);
        CELL_CONFIG = cellsForgeConfigSpecPair.getLeft();
        CELL_CONFIG_SPEC = cellsForgeConfigSpecPair.getRight();
        final Pair<FurnatorConfig, ForgeConfigSpec> FurnatorsForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(FurnatorConfig::new);
        FURNATOR_CONFIG = FurnatorsForgeConfigSpecPair.getLeft();
        FURNATOR_CONFIG_SPEC = FurnatorsForgeConfigSpecPair.getRight();
        final Pair<MagmaticGeneratorConfig, ForgeConfigSpec> MagmaticGeneratorsForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(MagmaticGeneratorConfig::new);
        MAGMATIC_GENERATOR = MagmaticGeneratorsForgeConfigSpecPair.getLeft();
        MAGMATIC_GENERATOR_CONFIG_SPEC = MagmaticGeneratorsForgeConfigSpecPair.getRight();
        final Pair<SolarPanelConfig, ForgeConfigSpec> SolarPanelsForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(SolarPanelConfig::new);
        SOLAR_PANEL = SolarPanelsForgeConfigSpecPair.getLeft();
        SOLAR_PANEL_CONFIG_SPEC = SolarPanelsForgeConfigSpecPair.getRight();
        final Pair<ThermoGeneratorConfig, ForgeConfigSpec> thermoGeneratorConfigForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(ThermoGeneratorConfig::new);
        THERMO_GEN_CONFIG = thermoGeneratorConfigForgeConfigSpecPair.getLeft();
        THERMO_GEN_CONFIG_SPEC = thermoGeneratorConfigForgeConfigSpecPair.getRight();
        final Pair<EnergizingConfig, ForgeConfigSpec> energizingConfigForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(EnergizingConfig::new);
        ENERGIZING_CONFIG = energizingConfigForgeConfigSpecPair.getLeft();
        ENERGIZING_CONFIG_SPEC = energizingConfigForgeConfigSpecPair.getRight();
        final Pair<CableConfig, ForgeConfigSpec> CablesForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(CableConfig::new);
        CABLE_CONFIG = CablesForgeConfigSpecPair.getLeft();
        CABLE_CONFIG_SPEC = CablesForgeConfigSpecPair.getRight();
        final Pair<MiscEnergyConfig, ForgeConfigSpec> miscEnergyForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(MiscEnergyConfig::new);
        MISC_ENERGY = miscEnergyForgeConfigSpecPair.getLeft();
        MISC_ENERGY_CONFIG_SPEC = miscEnergyForgeConfigSpecPair.getRight();
    }
}
