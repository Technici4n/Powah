package owmii.powah.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import owmii.powah.client.screen.inventory.*;
import owmii.powah.inventory.IContainers;

public class IScreens {
    public static void register() {
        ScreenManager.registerFactory(IContainers.ENERGY_CELL, EnergyCellScreen::new);
        ScreenManager.registerFactory(IContainers.ENDER_CELL, EnderCellScreen::new);
        ScreenManager.registerFactory(IContainers.MAGMATIC_GENERATOR, MagmaticGenScreen::new);
        ScreenManager.registerFactory(IContainers.FURNATOR, FurnatorScreen::new);
        ScreenManager.registerFactory(IContainers.SOLAR_PANEL, SolarPanelScreen::new);
        ScreenManager.registerFactory(IContainers.THERMO_GENERATOR, ThermoGenScreen::new);
        ScreenManager.registerFactory(IContainers.PLAYER_TRANSMITTER, PlayerTransmitterScreen::new);
        ScreenManager.registerFactory(IContainers.PLAYER_TRANSMITTER_DIM, PlayerTransmitterScreen::new);
        ScreenManager.registerFactory(IContainers.CABLE, CableScreen::new);
        ScreenManager.registerFactory(IContainers.DISCHARGER, DischargerScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_HOPPER, EnergyHopperScreen::new);
    }

    public static void openEnderNetScreen(ItemStack stack, CompoundNBT nbt) {
        Minecraft.getInstance().displayGuiScreen(new EnderNetScreen(stack.getDisplayName(), nbt));
    }
}
