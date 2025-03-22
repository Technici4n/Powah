package owmii.powah.client.screen;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import owmii.powah.client.screen.container.CableScreen;
import owmii.powah.client.screen.container.DischargerScreen;
import owmii.powah.client.screen.container.EnderCellScreen;
import owmii.powah.client.screen.container.EnergyCellScreen;
import owmii.powah.client.screen.container.EnergyHopperScreen;
import owmii.powah.client.screen.container.FurnatorScreen;
import owmii.powah.client.screen.container.MagmatorScreen;
import owmii.powah.client.screen.container.PlayerTransmitterScreen;
import owmii.powah.client.screen.container.ReactorScreen;
import owmii.powah.client.screen.container.SolarScreen;
import owmii.powah.client.screen.container.ThermoScreen;
import owmii.powah.inventory.Containers;

public class Screens {
    public static void register(RegisterMenuScreensEvent event) {
        event.register(Containers.ENERGY_CELL.get(), EnergyCellScreen::new);
        event.register(Containers.ENDER_CELL.get(), EnderCellScreen::new);
        event.register(Containers.FURNATOR.get(), FurnatorScreen::new);
        event.register(Containers.MAGMATOR.get(), MagmatorScreen::new);
        event.register(Containers.PLAYER_TRANSMITTER.get(), PlayerTransmitterScreen::new);
        event.register(Containers.ENERGY_HOPPER.get(), EnergyHopperScreen::new);
        event.register(Containers.CABLE.get(), CableScreen::new);
        event.register(Containers.REACTOR.get(), ReactorScreen::new);
        event.register(Containers.SOLAR.get(), SolarScreen::new);
        event.register(Containers.THERMO.get(), ThermoScreen::new);
        event.register(Containers.DISCHARGER.get(), DischargerScreen::new);
    }
}
