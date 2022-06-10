package owmii.powah.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import owmii.powah.lib.config.Config;
import owmii.powah.lib.config.IConfig;
import owmii.powah.config.generator.*;
import owmii.powah.config.item.BatteryConfig;

import java.util.HashSet;
import java.util.Set;

public class Configs {
    public static final Set<IConfig> ALL = new HashSet<>();
    public static final GeneralConfig GENERAL;
    private static final ForgeConfigSpec GENERAL_SPEC;

    public static final EnergyCellConfig ENERGY_CELL;
    public static final EnderCellConfig ENDER_CELL;
    public static final CableConfig CABLE;
    public static final EnderGateConfig ENDER_GATE;
    public static final EnergizingConfig ENERGIZING;
    public static final PlayerTransmitterConfig PLAYER_TRANSMITTER;
    public static final EnergyHopperConfig ENERGY_HOPPER;
    public static final EnergyDischargerConfig ENERGY_DISCHARGER;

    private static final ForgeConfigSpec ENERGY_CELL_SPEC;
    private static final ForgeConfigSpec ENDER_CELL_SPEC;
    private static final ForgeConfigSpec ENERGY_CABLE_SPEC;
    private static final ForgeConfigSpec ENDER_GATE_SPEC;
    private static final ForgeConfigSpec ENERGIZING_SPEC;
    private static final ForgeConfigSpec PLAYER_TRANSMITTER_SPEC;
    private static final ForgeConfigSpec ENERGY_HOPPER_SPEC;
    private static final ForgeConfigSpec ENERGY_DISCHARGER_SPEC;

    public static final FurnatorConfig FURNATOR;
    public static final MagmatorConfig MAGMATOR;
    public static final ThermoConfig THERMO;
    public static final SolarConfig SOLAR_PANEL;
    public static final ReactorConfig REACTOR;

    private static final ForgeConfigSpec FURNATOR_SPEC;
    private static final ForgeConfigSpec MAGMATOR_SPEC;
    private static final ForgeConfigSpec THERMO_SPEC;
    private static final ForgeConfigSpec SOLAR_PANEL_SPEC;
    private static final ForgeConfigSpec REACTOR_SPEC;

    public static final BatteryConfig BATTERY;

    private static final ForgeConfigSpec BATTERY_SPEC;

    public static void register() {
        String path = Config.createConfigDir("powah/energy");
        Config.registerCommon(GENERAL_SPEC, "powah/general_common.toml");
        Config.registerCommon(ENERGY_CELL_SPEC, path + "/energy_cell.toml");
        Config.registerCommon(ENDER_CELL_SPEC, path + "/ender_cell.toml");
        Config.registerCommon(ENERGY_CABLE_SPEC, path + "/energy_cable.toml");
        Config.registerCommon(ENDER_GATE_SPEC, path + "/ender_gate.toml");
        Config.registerCommon(ENERGIZING_SPEC, path + "/energizing.toml");
        Config.registerCommon(PLAYER_TRANSMITTER_SPEC, path + "/player_transmitter.toml");
        Config.registerCommon(ENERGY_HOPPER_SPEC, path + "/energy_hopper.toml");
        Config.registerCommon(ENERGY_DISCHARGER_SPEC, path + "/energy_discharger.toml");

        String genPath = Config.createConfigDir("powah/energy/generators");
        Config.registerCommon(FURNATOR_SPEC, genPath + "/furnator.toml");
        Config.registerCommon(MAGMATOR_SPEC, genPath + "/magmator.toml");
        Config.registerCommon(THERMO_SPEC, genPath + "/thermo_gen.toml");
        Config.registerCommon(SOLAR_PANEL_SPEC, genPath + "/solar_panel.toml");
        Config.registerCommon(REACTOR_SPEC, genPath + "/reactor.toml");

        String itemPath = Config.createConfigDir("powah/energy/items");
        Config.registerCommon(BATTERY_SPEC, itemPath + "/battery.toml");
    }

    static <T extends IConfig> T register(T config) {
        ALL.add(config);
        return config;
    }

    static {
        Pair<GeneralConfig, ForgeConfigSpec> generalPair = Config.get(GeneralConfig::new);
        GENERAL = generalPair.getLeft();
        GENERAL_SPEC = generalPair.getRight();

        Pair<EnergyCellConfig, ForgeConfigSpec> energyCellPair = Config.get(EnergyCellConfig::new);
        ENERGY_CELL = register(energyCellPair.getLeft());
        ENERGY_CELL_SPEC = energyCellPair.getRight();
        Pair<EnderCellConfig, ForgeConfigSpec> enderCellPair = Config.get(EnderCellConfig::new);
        ENDER_CELL = register(enderCellPair.getLeft());
        ENDER_CELL_SPEC = enderCellPair.getRight();
        Pair<CableConfig, ForgeConfigSpec> energyCablePair = Config.get(CableConfig::new);
        CABLE = register(energyCablePair.getLeft());
        ENERGY_CABLE_SPEC = energyCablePair.getRight();
        Pair<EnderGateConfig, ForgeConfigSpec> enderGatePair = Config.get(EnderGateConfig::new);
        ENDER_GATE = register(enderGatePair.getLeft());
        ENDER_GATE_SPEC = enderGatePair.getRight();
        Pair<EnergizingConfig, ForgeConfigSpec> energizingPair = Config.get(EnergizingConfig::new);
        ENERGIZING = register(energizingPair.getLeft());
        ENERGIZING_SPEC = energizingPair.getRight();
        Pair<PlayerTransmitterConfig, ForgeConfigSpec> playerTransmitterPair = Config.get(PlayerTransmitterConfig::new);
        PLAYER_TRANSMITTER = register(playerTransmitterPair.getLeft());
        PLAYER_TRANSMITTER_SPEC = playerTransmitterPair.getRight();
        Pair<EnergyHopperConfig, ForgeConfigSpec> energyHopperPair = Config.get(EnergyHopperConfig::new);
        ENERGY_HOPPER = register(energyHopperPair.getLeft());
        ENERGY_HOPPER_SPEC = energyHopperPair.getRight();
        Pair<EnergyDischargerConfig, ForgeConfigSpec> energyDischargerPair = Config.get(EnergyDischargerConfig::new);
        ENERGY_DISCHARGER = register(energyDischargerPair.getLeft());
        ENERGY_DISCHARGER_SPEC = energyDischargerPair.getRight();

        Pair<FurnatorConfig, ForgeConfigSpec> furnatorPair = Config.get(FurnatorConfig::new);
        FURNATOR = register(furnatorPair.getLeft());
        FURNATOR_SPEC = furnatorPair.getRight();
        Pair<MagmatorConfig, ForgeConfigSpec> magmatorPair = Config.get(MagmatorConfig::new);
        MAGMATOR = register(magmatorPair.getLeft());
        MAGMATOR_SPEC = magmatorPair.getRight();
        Pair<ThermoConfig, ForgeConfigSpec> thermoPair = Config.get(ThermoConfig::new);
        THERMO = register(thermoPair.getLeft());
        THERMO_SPEC = thermoPair.getRight();
        Pair<SolarConfig, ForgeConfigSpec> solarPair = Config.get(SolarConfig::new);
        SOLAR_PANEL = register(solarPair.getLeft());
        SOLAR_PANEL_SPEC = solarPair.getRight();
        Pair<ReactorConfig, ForgeConfigSpec> reactorPair = Config.get(ReactorConfig::new);
        REACTOR = register(reactorPair.getLeft());
        REACTOR_SPEC = reactorPair.getRight();

        Pair<BatteryConfig, ForgeConfigSpec> batteryPair = Config.get(BatteryConfig::new);
        BATTERY = register(batteryPair.getLeft());
        BATTERY_SPEC = batteryPair.getRight();
    }
}
