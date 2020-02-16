package owmii.powah.client.screen;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.powah.client.screen.inventory.*;
import owmii.powah.inventory.IContainers;

@OnlyIn(Dist.CLIENT)
public class Screens {
    public static void register() {
        ScreenManager.registerFactory(IContainers.ENERGY_CELL, EnergyCellScreen::new);
        ScreenManager.registerFactory(IContainers.ENDER_CELL, EnderCellScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_CABLE, EnergyCableScreen::new);
        ScreenManager.registerFactory(IContainers.FURNATOR, FurnatorScreen::new);
        ScreenManager.registerFactory(IContainers.MAGMATOR, MagmatorScreen::new);
        ScreenManager.registerFactory(IContainers.THERMO_GEN, ThermoGenScreen::new);
        ScreenManager.registerFactory(IContainers.SOLAR_PANEL, SolarPanelScreen::new);
        ScreenManager.registerFactory(IContainers.PLAYER_TRANSMITTER, PlayerTransmitterScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_HOPPER, EnergyHopperScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_DISCHARGER, EnergyDischargerScreen::new);
        ScreenManager.registerFactory(IContainers.REACTOR, ReactorScreen::new);
    }
}
