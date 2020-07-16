package owmii.powah;

import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import owmii.lib.api.IMod;
import owmii.lib.config.IConfig;
import owmii.lib.network.Network;
import owmii.lib.util.FML;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.IBlocks;
import owmii.powah.book.PowahBook;
import owmii.powah.client.ItemModelProperties;
import owmii.powah.client.render.entity.EntityRenderer;
import owmii.powah.client.render.tile.TileRenderer;
import owmii.powah.client.screen.Screens;
import owmii.powah.config.ConfigHandler;
import owmii.powah.config.Configs;
import owmii.powah.network.Packets;
import owmii.powah.recipe.Recipes;
import owmii.powah.world.gen.Features;

@Mod(Powah.MOD_ID)
public class Powah implements IMod {
    public static final String MOD_ID = "powah";
    public static final Network NET = new Network(MOD_ID);
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Powah() {
        loadListeners();
        Configs.register();
        Recipes.init();
    }

    @Override
    public void setup(FMLCommonSetupEvent event) {
        Packets.register();
        Features.register();

        PowahAPI.registerSolidCoolant(Blocks.SNOW_BLOCK, 48, -3);
        PowahAPI.registerSolidCoolant(Items.SNOWBALL, 12, -3);
        PowahAPI.registerSolidCoolant(Blocks.ICE, 48, -5);
        PowahAPI.registerSolidCoolant(Blocks.PACKED_ICE, 192, -8);
        PowahAPI.registerSolidCoolant(Blocks.BLUE_ICE, 568, -17);
        PowahAPI.registerSolidCoolant(IBlocks.DRY_ICE, 712, -32);
    }

    @Override
    public void client(FMLClientSetupEvent event) {
        if (FML.isClient()) {
            TileRenderer.register();
            EntityRenderer.register();
            Screens.register();
            ItemModelProperties.register();
            PowahBook.register();
        }
    }

    @Override
    public void loadComplete(FMLLoadCompleteEvent event) {
        Configs.ENERGY.forEach(IConfig::reload);
        ConfigHandler.post();
    }
}