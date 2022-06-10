package owmii.powah.client.screen;

import net.minecraft.client.gui.screens.MenuScreens;
import owmii.powah.lib.client.screen.wiki.WikiScreen;
import owmii.powah.client.book.PowahBook;
import owmii.powah.client.screen.container.*;
import owmii.powah.inventory.Containers;

public class Screens {
    public static void register() {
        MenuScreens.register(Containers.ENERGY_CELL.get(), EnergyCellScreen::new);
        MenuScreens.register(Containers.ENDER_CELL.get(), EnderCellScreen::new);
        MenuScreens.register(Containers.FURNATOR.get(), FurnatorScreen::new);
        MenuScreens.register(Containers.MAGMATOR.get(), MagmatorScreen::new);
        MenuScreens.register(Containers.PLAYER_TRANSMITTER.get(), PlayerTransmitterScreen::new);
        MenuScreens.register(Containers.ENERGY_HOPPER.get(), EnergyHopperScreen::new);
        MenuScreens.register(Containers.CABLE.get(), CableScreen::new);
        MenuScreens.register(Containers.REACTOR.get(), ReactorScreen::new);
        MenuScreens.register(Containers.SOLAR.get(), SolarScreen::new);
        MenuScreens.register(Containers.THERMO.get(), ThermoScreen::new);
        MenuScreens.register(Containers.DISCHARGER.get(), DischargerScreen::new);
    }

    public static void openManualScreen() {
        WikiScreen.open(PowahBook.WIKI.getCategories().get(0));
    }
}
