package owmii.powah.client.screen;

import net.minecraft.client.gui.ScreenManager;
import owmii.lib.client.screen.wiki.WikiScreen;
import owmii.powah.client.book.PowahBook;
import owmii.powah.client.screen.container.*;
import owmii.powah.inventory.Containers;

public class Screens {
    public static void register() {
        ScreenManager.registerFactory(Containers.ENERGY_CELL, EnergyCellScreen::new);
        ScreenManager.registerFactory(Containers.ENDER_CELL, EnderCellScreen::new);
        ScreenManager.registerFactory(Containers.FURNATOR, FurnatorScreen::new);
        ScreenManager.registerFactory(Containers.MAGMATOR, MagmatorScreen::new);
        ScreenManager.registerFactory(Containers.PLAYER_TRANSMITTER, PlayerTransmitterScreen::new);
        ScreenManager.registerFactory(Containers.ENERGY_HOPPER, EnergyHopperScreen::new);
        ScreenManager.registerFactory(Containers.CABLE, CableScreen::new);
        ScreenManager.registerFactory(Containers.REACTOR, ReactorScreen::new);
        ScreenManager.registerFactory(Containers.SOLAR, SolarScreen::new);
        ScreenManager.registerFactory(Containers.THERMO, ThermoScreen::new);
        ScreenManager.registerFactory(Containers.DISCHARGER, DischargerScreen::new);
    }

    public static void openManualScreen() {
        BookScreen.open();
    }
}
