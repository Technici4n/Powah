package zeroneye.powah;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zeroneye.powah.api.PowahAPI;
import zeroneye.powah.api.recipe.energizing.EnergizingRecipeSorter;
import zeroneye.powah.block.energizing.EnergizingRecipes;
import zeroneye.powah.client.gui.IScreens;
import zeroneye.powah.client.renderer.entity.EntityRenderer;
import zeroneye.powah.client.renderer.tile.TileRenderer;
import zeroneye.powah.config.Config;
import zeroneye.powah.config.ConfigHandler;
import zeroneye.powah.handler.CoolingFluidHandler;
import zeroneye.powah.handler.HeatBlockHandler;
import zeroneye.powah.handler.MagmaticFluidHandler;
import zeroneye.powah.network.Packets;

import static zeroneye.lib.Lollipop.addModListener;

@Mod(Powah.MOD_ID)
public class Powah {
    public static final String MOD_ID = "powah";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Powah() {
        addModListener(this::commonSetup);
        addModListener(this::clientSetup);
        addModListener(this::loadComplete);
        Config.setup();
    }

    void commonSetup(FMLCommonSetupEvent event) {
        Packets.register();

        PowahAPI.register(EnergizingRecipes.all());
    }

    void clientSetup(FMLClientSetupEvent event) {
        TileRenderer.register();
        EntityRenderer.register();
        IScreens.register();
    }

    void loadComplete(FMLLoadCompleteEvent event) {
        MagmaticFluidHandler.post();
        CoolingFluidHandler.post();
        HeatBlockHandler.post();

        ConfigHandler.reload();

        EnergizingRecipeSorter.sort();
    }
}