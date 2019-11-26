package zeroneye.powah;

import net.minecraft.fluid.Fluids;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import zeroneye.powah.api.PowahAPI;
import zeroneye.powah.block.generator.magmatic.MagmaticGenTile;
import zeroneye.powah.client.gui.IScreens;
import zeroneye.powah.client.renderer.tile.MagmaticGenRenderer;
import zeroneye.powah.config.Config;
import zeroneye.powah.network.Packets;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static zeroneye.lib.Lollipop.addModListener;

@Mod(Powah.MOD_ID)
public class Powah {
    public static final String MOD_ID = "powah";

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
    }

    void commonSetup(FMLCommonSetupEvent event) {
        PowahAPI.registerMagmaticFluid(Fluids.LAVA, 10000);
        Packets.register();
    }

    void clientSetup(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(MagmaticGenTile.class, new MagmaticGenRenderer());
        IScreens.register();
    }
}