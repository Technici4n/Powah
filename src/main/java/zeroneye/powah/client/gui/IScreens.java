package zeroneye.powah.client.gui;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zeroneye.powah.client.gui.container.*;
import zeroneye.powah.inventory.IContainers;

@OnlyIn(Dist.CLIENT)
public class IScreens {
    public static void register() {
        ScreenManager.registerFactory(IContainers.ENERGY_CELL_BASIC, EnergyCellScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_CELL_HARDENED, EnergyCellScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_CELL_BLAZING, EnergyCellScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_CELL_NIOTIC, EnergyCellScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_CELL_SPIRITED, EnergyCellScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_CELL_CREATIVE, EnergyCellScreen::new);

        ScreenManager.registerFactory(IContainers.MAGMATIC_GENERATOR_BASIC, MagmaticGenScreen::new);
        ScreenManager.registerFactory(IContainers.MAGMATIC_GENERATOR_HARDENED, MagmaticGenScreen::new);
        ScreenManager.registerFactory(IContainers.MAGMATIC_GENERATOR_BLAZING, MagmaticGenScreen::new);
        ScreenManager.registerFactory(IContainers.MAGMATIC_GENERATOR_NIOTIC, MagmaticGenScreen::new);
        ScreenManager.registerFactory(IContainers.MAGMATIC_GENERATOR_SPIRITED, MagmaticGenScreen::new);

        ScreenManager.registerFactory(IContainers.FURNATOR_BASIC, FurnatorScreen::new);
        ScreenManager.registerFactory(IContainers.FURNATOR_HARDENED, FurnatorScreen::new);
        ScreenManager.registerFactory(IContainers.FURNATOR_BLAZING, FurnatorScreen::new);
        ScreenManager.registerFactory(IContainers.FURNATOR_NIOTIC, FurnatorScreen::new);
        ScreenManager.registerFactory(IContainers.FURNATOR_SPIRITED, FurnatorScreen::new);

        ScreenManager.registerFactory(IContainers.SOLAR_PANEL_BASIC, SolarPanelScreen::new);
        ScreenManager.registerFactory(IContainers.SOLAR_PANEL_HARDENED, SolarPanelScreen::new);
        ScreenManager.registerFactory(IContainers.SOLAR_PANEL_BLAZING, SolarPanelScreen::new);
        ScreenManager.registerFactory(IContainers.SOLAR_PANEL_NIOTIC, SolarPanelScreen::new);
        ScreenManager.registerFactory(IContainers.SOLAR_PANEL_SPIRITED, SolarPanelScreen::new);

        ScreenManager.registerFactory(IContainers.THERMO_GENERATOR_BASIC, ThermoGenScreen::new);
        ScreenManager.registerFactory(IContainers.THERMO_GENERATOR_HARDENED, ThermoGenScreen::new);
        ScreenManager.registerFactory(IContainers.THERMO_GENERATOR_BLAZING, ThermoGenScreen::new);
        ScreenManager.registerFactory(IContainers.THERMO_GENERATOR_NIOTIC, ThermoGenScreen::new);
        ScreenManager.registerFactory(IContainers.THERMO_GENERATOR_SPIRITED, ThermoGenScreen::new);

        ScreenManager.registerFactory(IContainers.PLAYER_TRANSMITTER, PlayerTransmitterScreen::new);
        ScreenManager.registerFactory(IContainers.PLAYER_TRANSMITTER_DIM, PlayerTransmitterScreen::new);

        ScreenManager.registerFactory(IContainers.CABLE_BASIC, CableScreen::new);
        ScreenManager.registerFactory(IContainers.CABLE_HARDENED, CableScreen::new);
        ScreenManager.registerFactory(IContainers.CABLE_BLAZING, CableScreen::new);
        ScreenManager.registerFactory(IContainers.CABLE_NIOTIC, CableScreen::new);
        ScreenManager.registerFactory(IContainers.CABLE_SPIRITED, CableScreen::new);

        ScreenManager.registerFactory(IContainers.DISCHARGER, DischargerScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_HOPPER, EnergyHopperScreen::new);
    }
}
