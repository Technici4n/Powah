package owmii.powah;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import owmii.lib.config.IConfig;
import owmii.powah.api.PowahAPI;
import owmii.powah.api.recipe.energizing.EnergizingRecipeSorter;
import owmii.powah.block.energizing.EnergizingRecipes;
import owmii.powah.client.render.BlockRenderTypes;
import owmii.powah.client.render.entity.EntityRenderer;
import owmii.powah.client.render.tile.TileRenderer;
import owmii.powah.client.screen.Screens;
import owmii.powah.config.ConfigHandler;
import owmii.powah.config.Configs;
import owmii.powah.network.Packets;

import static owmii.lib.Lollipop.addModListener;

@Mod(Powah.MOD_ID)
public class Powah {
    public static final String MOD_ID = "powah";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Powah() {
        addModListener(this::commonSetup);
        addModListener(this::clientSetup);
        addModListener(this::loadComplete);
        Configs.register();
    }

    void commonSetup(final FMLCommonSetupEvent event) {
        PowahAPI.register(EnergizingRecipes.all());
        Packets.register();
    }

    @OnlyIn(Dist.CLIENT)
    void clientSetup(final FMLClientSetupEvent event) {
        BlockRenderTypes.register();
        EntityRenderer.register();
        TileRenderer.register();
        Screens.register();
    }

    void loadComplete(final FMLLoadCompleteEvent event) {
        Configs.ENERGY.forEach(IConfig::reload);
        EnergizingRecipeSorter.sort();
        ConfigHandler.post();
    }
}