package zeroneye.powah.client.gui;

import net.minecraft.client.gui.ScreenManager;
import zeroneye.powah.client.gui.container.EnergyCellScreen;
import zeroneye.powah.client.gui.container.FurnatorScreen;
import zeroneye.powah.client.gui.container.MagmaticGenScreen;
import zeroneye.powah.client.gui.container.PlayerTransmitterScreen;
import zeroneye.powah.inventory.IContainers;

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

        ScreenManager.registerFactory(IContainers.PLAYER_TRANSMITTER, PlayerTransmitterScreen::new);
        ScreenManager.registerFactory(IContainers.PLAYER_TRANSMITTER_DIM, PlayerTransmitterScreen::new);
    }
}
