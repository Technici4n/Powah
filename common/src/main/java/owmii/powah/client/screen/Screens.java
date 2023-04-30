package owmii.powah.client.screen;

import dev.architectury.registry.menu.MenuRegistry;
import owmii.powah.client.book.PowahBook;
import owmii.powah.client.screen.container.*;
import owmii.powah.inventory.Containers;
import owmii.powah.lib.client.screen.wiki.WikiScreen;

public class Screens {
    public static void register() {
        MenuRegistry.registerScreenFactory(Containers.ENERGY_CELL.get(), EnergyCellScreen::new);
        MenuRegistry.registerScreenFactory(Containers.ENDER_CELL.get(), EnderCellScreen::new);
        MenuRegistry.registerScreenFactory(Containers.FURNATOR.get(), FurnatorScreen::new);
        MenuRegistry.registerScreenFactory(Containers.MAGMATOR.get(), MagmatorScreen::new);
        MenuRegistry.registerScreenFactory(Containers.PLAYER_TRANSMITTER.get(), PlayerTransmitterScreen::new);
        MenuRegistry.registerScreenFactory(Containers.ENERGY_HOPPER.get(), EnergyHopperScreen::new);
        MenuRegistry.registerScreenFactory(Containers.CABLE.get(), CableScreen::new);
        MenuRegistry.registerScreenFactory(Containers.REACTOR.get(), ReactorScreen::new);
        MenuRegistry.registerScreenFactory(Containers.SOLAR.get(), SolarScreen::new);
        MenuRegistry.registerScreenFactory(Containers.THERMO.get(), ThermoScreen::new);
        MenuRegistry.registerScreenFactory(Containers.DISCHARGER.get(), DischargerScreen::new);
    }

    public static void openManualScreen() {
        WikiScreen.open(PowahBook.WIKI.getCategories().get(0));
    }
}
