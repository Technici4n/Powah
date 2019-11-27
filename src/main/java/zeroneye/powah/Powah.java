package zeroneye.powah;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zeroneye.powah.client.gui.IScreens;
import zeroneye.powah.client.renderer.tile.ITileRnderers;
import zeroneye.powah.config.Config;
import zeroneye.powah.handler.FluidHandler;
import zeroneye.powah.network.Packets;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static zeroneye.lib.Lollipop.addModListener;

@Mod(Powah.MOD_ID)
public class Powah {
    public static final String MOD_ID = "powah";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Powah() {
        Path dir = FMLPaths.CONFIGDIR.get();
        Path configDir = Paths.get(dir.toAbsolutePath().toString(), MOD_ID);
        try {
            Files.createDirectory(configDir);
        } catch (Exception ignored) {
        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.CONFIG_SPEC, MOD_ID + "/general_common.toml");
        addModListener(this::commonSetup);
        addModListener(this::clientSetup);
        addModListener(this::loadComplete);
    }

    void commonSetup(FMLCommonSetupEvent event) {
        Packets.register();
    }

    void clientSetup(FMLClientSetupEvent event) {
        ITileRnderers.register();
        IScreens.register();
    }

    void loadComplete(FMLLoadCompleteEvent event) {
        FluidHandler.post();
    }
}