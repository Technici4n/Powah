package owmii.powah.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemStack;
import owmii.powah.client.screen.book.BookScreen;
import owmii.powah.client.screen.inventory.*;
import owmii.powah.inventory.IContainers;

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

    public static void openManualScreen() {
        Minecraft.getInstance().displayGuiScreen(BookScreen.instance);
    }

    public static void openEnderNetScreen(ItemStack stack, int totalChannels) {
        Minecraft.getInstance().displayGuiScreen(new EnderNetScreen(stack, totalChannels));
    }
}
