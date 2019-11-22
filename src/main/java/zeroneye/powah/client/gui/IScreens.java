package zeroneye.powah.client.gui;

import net.minecraft.client.gui.ScreenManager;
import zeroneye.powah.client.gui.container.EnergyCellScreen;
import zeroneye.powah.client.gui.container.MagmaticGenScreen;
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
    }
}
