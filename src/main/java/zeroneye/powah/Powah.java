package zeroneye.powah;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import zeroneye.powah.client.gui.container.EnergyCellScreen;
import zeroneye.powah.inventory.IContainers;
import zeroneye.powah.network.Packets;

import static zeroneye.lib.Lollipop.addModListener;

@Mod(Powah.MOD_ID)
public class Powah {
    public static final String MOD_ID = "powah";

    public Powah() {
        addModListener(this::commonSetup);
        addModListener(this::clientSetup);
    }

    void commonSetup(FMLCommonSetupEvent event) {
        Packets.register();
    }

    void clientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(IContainers.ENERGY_CELL_BASIC, EnergyCellScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_CELL_HARDENED, EnergyCellScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_CELL_BLAZING, EnergyCellScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_CELL_NIOTIC, EnergyCellScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_CELL_SPIRITED, EnergyCellScreen::new);
        ScreenManager.registerFactory(IContainers.ENERGY_CELL_CREATIVE, EnergyCellScreen::new);
    }
}