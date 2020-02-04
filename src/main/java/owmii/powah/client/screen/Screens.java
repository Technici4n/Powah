package owmii.powah.client.screen;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.powah.client.screen.inventory.EnergyCableScreen;
import owmii.powah.client.screen.inventory.EnergyCellScreen;
import owmii.powah.client.screen.inventory.FurnatorScreen;
import owmii.powah.client.screen.inventory.MagmatorScreen;
import owmii.powah.inventory.IContainers;

@OnlyIn(Dist.CLIENT)
public class Screens {
    public static void register() {
        ScreenManager.registerFactory(IContainers.ENERGY_CELL, EnergyCellScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_CABLE, EnergyCableScreen::new);
        ScreenManager.registerFactory(IContainers.FURNATOR, FurnatorScreen::new);
        ScreenManager.registerFactory(IContainers.MAGMATOR, MagmatorScreen::new);
    }
}
