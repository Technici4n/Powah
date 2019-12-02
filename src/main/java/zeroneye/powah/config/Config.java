package zeroneye.powah.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import zeroneye.powah.Powah;
import zeroneye.powah.block.IBlocks;
import zeroneye.powah.block.cable.Cables;
import zeroneye.powah.block.generator.furnator.Furnators;
import zeroneye.powah.block.generator.magmatic.MagmaticGenerators;
import zeroneye.powah.block.generator.panel.solar.SolarPanels;
import zeroneye.powah.block.storage.EnergyCells;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Config {
    public static final Marker MARKER = new MarkerManager.Log4jMarker("CONFIG");
    public static final General GENERAL;
    public static final ForgeConfigSpec GENERAL_CONFIG_SPEC;

    public static final Cell CELL;
    public static final ForgeConfigSpec CELL_CONFIG_SPEC;

    public static final Furnator FURNATOR;
    public static final ForgeConfigSpec FURNATOR_CONFIG_SPEC;

    public static final MagmaticGenerator MAGMATIC_GENERATOR;
    public static final ForgeConfigSpec MAGMATIC_GENERATOR_CONFIG_SPEC;

    public static final SolarPanel SOLAR_PANEL;
    public static final ForgeConfigSpec SOLAR_PANEL_CONFIG_SPEC;

    public static final Cable CABLE;
    public static final ForgeConfigSpec CABLE_CONFIG_SPEC;

    public static final MiscEnergy MISC_ENERGY;
    public static final ForgeConfigSpec MISC_ENERGY_CONFIG_SPEC;

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
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MAGMATIC_GENERATOR_CONFIG_SPEC, energyFolder + "/Magmatic_Generator.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SOLAR_PANEL_CONFIG_SPEC, energyFolder + "/Solar_Panel.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CABLE_CONFIG_SPEC, energyFolder + "/Energy_cable.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MISC_ENERGY_CONFIG_SPEC, energyFolder + "/Misc.toml");
    }

    public static class General {
        public final ForgeConfigSpec.BooleanValue capacitor_blazing;
        public final ForgeConfigSpec.BooleanValue capacitor_niotic;
        public final ForgeConfigSpec.BooleanValue capacitor_spirited;
        public final ForgeConfigSpec.BooleanValue player_aerial_pearl;

        public final ForgeConfigSpec.ConfigValue<List<String>> magmaticFluids;
        public final ForgeConfigSpec.BooleanValue magmaticFluidsAPI;

        public General(ForgeConfigSpec.Builder builder) {
            this.capacitor_blazing = builder.comment("", "Enable this to get Blazing Capacitor by right clicking a blaze with a Large Basic Capacitor. [default:true]").define("capacitor_blazing", true);
            this.capacitor_niotic = builder.comment("", "Enable this to get Niotic Capacitor by right clicking a Diamond Ore with a Blazing Capacitor. [default:true]").define("capacitor_niotic", true);
            this.capacitor_spirited = builder.comment("", "Enable this to get Spirited Capacitor by right clicking an Emerald Ore with a Niotic Capacitor. [default:true]").define("capacitor_spirited", true);
            this.player_aerial_pearl = builder.comment("", "Enable this to get Player Aerial Pearl by right clicking a Zombie or Husk with a Aerial Pearl. [default:true]").define("player_aerial_pearl", true);

            this.magmaticFluids = builder
                    .comment("", "List of fluids used in Magmatic Generator.", "fluid registry name = heat per 100 mb eg: [\"minecraft:lava=10000\", \"examplemod:fluid=1000\"]")
                    .define("magmaticFluids", Lists.newArrayList("minecraft:lava=10000"));
            this.magmaticFluidsAPI = builder.comment("Enable this to allow other mods to add their fluids. [default:true]").define("magmaticFluidsAPI", true);
        }
    }

    public static class MiscEnergy {
        public final ForgeConfigSpec.ConfigValue playerTransmitterCap;
        public final ForgeConfigSpec.ConfigValue playerTransmitterTransfer;

        public final ForgeConfigSpec.ConfigValue playerTransmitterDimCap;
        public final ForgeConfigSpec.ConfigValue playerTransmitterDimTransfer;

        public final ForgeConfigSpec.ConfigValue dischargerCap;
        public final ForgeConfigSpec.ConfigValue dischargerTransfer;

        public MiscEnergy(ForgeConfigSpec.Builder builder) {
            builder.comment("Range: min = 0, max = " + Integer.MAX_VALUE);

            builder.push("Player Transmeter");
            this.playerTransmitterCap = builder.define("capacity", "" + IBlocks.PLAYER_TRANSMITTER.getCapacity());
            this.playerTransmitterTransfer = builder.define("transfer", "" + IBlocks.PLAYER_TRANSMITTER.getMaxExtract());
            builder.pop();

            builder.push("Dimensional Player Transmeter");
            this.playerTransmitterDimCap = builder.define("capacity", "" + IBlocks.PLAYER_TRANSMITTER_DIM.getCapacity());
            this.playerTransmitterDimTransfer = builder.define("transfer", "" + IBlocks.PLAYER_TRANSMITTER_DIM.getMaxExtract());
            builder.pop();

            builder.push("Energy Discharger");
            this.dischargerCap = builder.define("capacity", "" + IBlocks.DISCHARGER.getCapacity());
            this.dischargerTransfer = builder.define("transfer", "" + IBlocks.DISCHARGER.getMaxExtract());
            builder.pop();
        }
    }

    public static class Cell {
        public final ForgeConfigSpec.ConfigValue[] caps = new ForgeConfigSpec.ConfigValue[EnergyCells.values().length - 1];
        public final ForgeConfigSpec.ConfigValue[] transfers = new ForgeConfigSpec.ConfigValue[EnergyCells.values().length - 1];

        public Cell(ForgeConfigSpec.Builder builder) {
            EnergyCells[] values = EnergyCells.values();
            for (int i = 0; i < values.length; i++) {
                EnergyCells cell = values[i];
                if (cell.equals(EnergyCells.CREATIVE)) continue;
                String name = cell.name().toLowerCase();
                String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
                if (i == 0) {
                    builder.comment("Range: min = 0, max = " + Integer.MAX_VALUE);
                }
                builder.push(cap + " Enregy Cell");
                this.caps[i] = builder.define("capacity", "" + cell.capacity);
                this.transfers[i] = builder.define("transfer", "" + cell.transfer);
                builder.pop();
            }
        }
    }

    public static class Furnator {
        public final ForgeConfigSpec.ConfigValue[] caps = new ForgeConfigSpec.ConfigValue[Furnators.values().length];
        public final ForgeConfigSpec.ConfigValue[] transfers = new ForgeConfigSpec.ConfigValue[Furnators.values().length];
        public final ForgeConfigSpec.ConfigValue[] generations = new ForgeConfigSpec.ConfigValue[Furnators.values().length];

        public Furnator(ForgeConfigSpec.Builder builder) {
            Furnators[] values = Furnators.values();
            for (int i = 0; i < values.length; i++) {
                Furnators furnator = values[i];
                String name = furnator.name().toLowerCase();
                String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
                if (i == 0) {
                    builder.comment("Range: min = 0, max = " + Integer.MAX_VALUE);
                }
                builder.push(cap + " Furnator");
                this.caps[i] = builder.define("capacity", "" + furnator.capacity);
                this.transfers[i] = builder.define("transfer", "" + furnator.transfer);
                this.generations[i] = builder.define("perTick", "" + furnator.perTick);
                builder.pop();
            }
        }
    }

    public static class MagmaticGenerator {
        public final ForgeConfigSpec.ConfigValue[] caps = new ForgeConfigSpec.ConfigValue[MagmaticGenerators.values().length];
        public final ForgeConfigSpec.ConfigValue[] transfers = new ForgeConfigSpec.ConfigValue[MagmaticGenerators.values().length];
        public final ForgeConfigSpec.ConfigValue[] generations = new ForgeConfigSpec.ConfigValue[MagmaticGenerators.values().length];

        public MagmaticGenerator(ForgeConfigSpec.Builder builder) {
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

    public static class SolarPanel {
        public final ForgeConfigSpec.ConfigValue[] caps = new ForgeConfigSpec.ConfigValue[SolarPanels.values().length];
        public final ForgeConfigSpec.ConfigValue[] transfers = new ForgeConfigSpec.ConfigValue[SolarPanels.values().length];
        public final ForgeConfigSpec.ConfigValue[] generations = new ForgeConfigSpec.ConfigValue[SolarPanels.values().length];

        public SolarPanel(ForgeConfigSpec.Builder builder) {
            SolarPanels[] values = SolarPanels.values();
            for (int i = 0; i < values.length; i++) {
                SolarPanels panel = values[i];
                String name = panel.name().toLowerCase();
                String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
                if (i == 0) {
                    builder.comment("Range: min = 0, max = " + Integer.MAX_VALUE);
                }
                builder.push(cap + " Solar Panel");
                this.caps[i] = builder.define("capacity", "" + panel.capacity);
                this.transfers[i] = builder.define("transfer", "" + panel.transfer);
                this.generations[i] = builder.define("perTick", "" + panel.perTick);
                builder.pop();
            }
        }
    }

    public static class Cable {
        public final ForgeConfigSpec.ConfigValue[] transfers = new ForgeConfigSpec.ConfigValue[Cables.values().length];

        public Cable(ForgeConfigSpec.Builder builder) {
            Cables[] values = Cables.values();
            for (int i = 0; i < values.length; i++) {
                Cables cable = values[i];
                String name = cable.name().toLowerCase();
                String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
                if (i == 0) {
                    builder.comment("Range: min = 0, max = " + Integer.MAX_VALUE);
                }
                builder.push(cap + " Energy Cable");
                this.transfers[i] = builder.define("transfer", "" + cable.transfer);
                builder.pop();
            }
        }
    }

    static {
        final Pair<General, ForgeConfigSpec> generalForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(General::new);
        GENERAL = generalForgeConfigSpecPair.getLeft();
        GENERAL_CONFIG_SPEC = generalForgeConfigSpecPair.getRight();
        final Pair<Cell, ForgeConfigSpec> cellsForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(Cell::new);
        CELL = cellsForgeConfigSpecPair.getLeft();
        CELL_CONFIG_SPEC = cellsForgeConfigSpecPair.getRight();
        final Pair<Furnator, ForgeConfigSpec> FurnatorsForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(Furnator::new);
        FURNATOR = FurnatorsForgeConfigSpecPair.getLeft();
        FURNATOR_CONFIG_SPEC = FurnatorsForgeConfigSpecPair.getRight();
        final Pair<MagmaticGenerator, ForgeConfigSpec> MagmaticGeneratorsForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(MagmaticGenerator::new);
        MAGMATIC_GENERATOR = MagmaticGeneratorsForgeConfigSpecPair.getLeft();
        MAGMATIC_GENERATOR_CONFIG_SPEC = MagmaticGeneratorsForgeConfigSpecPair.getRight();
        final Pair<SolarPanel, ForgeConfigSpec> SolarPanelsForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(SolarPanel::new);
        SOLAR_PANEL = SolarPanelsForgeConfigSpecPair.getLeft();
        SOLAR_PANEL_CONFIG_SPEC = SolarPanelsForgeConfigSpecPair.getRight();
        final Pair<Cable, ForgeConfigSpec> CablesForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(Cable::new);
        CABLE = CablesForgeConfigSpecPair.getLeft();
        CABLE_CONFIG_SPEC = CablesForgeConfigSpecPair.getRight();
        final Pair<MiscEnergy, ForgeConfigSpec> miscEnergyForgeConfigSpecPair = new ForgeConfigSpec.Builder().configure(MiscEnergy::new);
        MISC_ENERGY = miscEnergyForgeConfigSpecPair.getLeft();
        MISC_ENERGY_CONFIG_SPEC = miscEnergyForgeConfigSpecPair.getRight();
    }
}
