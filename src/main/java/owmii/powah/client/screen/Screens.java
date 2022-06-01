package owmii.powah.client.screen;

import net.minecraft.client.gui.screens.MenuScreens;
import owmii.lib.client.screen.wiki.WikiScreen;
import owmii.powah.client.book.PowahBook;
import owmii.powah.client.screen.container.*;
import owmii.powah.inventory.Containers;

public class Screens {
    public static void register() {
        MenuScreens.register(Containers.ENERGY_CELL, EnergyCellScreen::new);
        MenuScreens.register(Containers.ENDER_CELL, EnderCellScreen::new);
        MenuScreens.register(Containers.FURNATOR, FurnatorScreen::new);
        MenuScreens.register(Containers.MAGMATOR, MagmatorScreen::new);
        MenuScreens.register(Containers.PLAYER_TRANSMITTER, PlayerTransmitterScreen::new);
        MenuScreens.register(Containers.ENERGY_HOPPER, EnergyHopperScreen::new);
        MenuScreens.register(Containers.CABLE, CableScreen::new);
        MenuScreens.register(Containers.REACTOR, ReactorScreen::new);
        MenuScreens.register(Containers.SOLAR, SolarScreen::new);
        MenuScreens.register(Containers.THERMO, ThermoScreen::new);
        MenuScreens.register(Containers.DISCHARGER, DischargerScreen::new);
    }

    public static void openManualScreen() {
        WikiScreen.open(PowahBook.WIKI.getCategories().get(0));
    }
}
