package owmii.powah;

import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import owmii.powah.api.PowahAPI;
import owmii.powah.api.recipe.energizing.EnergizingRecipeSorter;
import owmii.powah.block.energizing.EnergizingRecipes;
import owmii.powah.book.PowahBook;
import owmii.powah.client.renderer.entity.EntityRenderer;
import owmii.powah.client.renderer.tile.TileRenderer;
import owmii.powah.client.screen.IScreens;
import owmii.powah.compat.crafttweaker.CrafttweakerCompat;
import owmii.powah.config.Config;
import owmii.powah.config.ConfigHandler;
import owmii.powah.handler.CoolingFluidHandler;
import owmii.powah.handler.HeatBlockHandler;
import owmii.powah.handler.MagmaticFluidHandler;
import owmii.powah.network.Packets;
import owmii.powah.world.gen.IFeatures;

import static owmii.lib.Lollipop.addModListener;

@Mod(Powah.MOD_ID)
public class Powah {
    public static final String MOD_ID = "powah";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Powah() {
        addModListener(this::commonSetup);
        addModListener(this::clientSetup);
        addModListener(this::loadComplete);
        Config.setup();

        CrafttweakerCompat.setup();
    }

    void commonSetup(FMLCommonSetupEvent event) {
        Packets.register();
        IFeatures.register();
        PowahAPI.register(EnergizingRecipes.all());


        PowahAPI.registerReactorCoolant(Fluids.WATER, 1);
        PowahAPI.registerReactorSolidCoolant(Blocks.ICE, 48, -7);
        PowahAPI.registerReactorSolidCoolant(Blocks.PACKED_ICE, 192, -14);
        PowahAPI.registerReactorSolidCoolant(Blocks.BLUE_ICE, 768, -20);

        PowahAPI.registerReactorSolidCoolant(Blocks.SNOW_BLOCK, 48, -4);
        PowahAPI.registerReactorSolidCoolant(Items.SNOWBALL, 12, -4);
    }

    void clientSetup(FMLClientSetupEvent event) {
        TileRenderer.register();
        EntityRenderer.register();
        IScreens.register();
        PowahBook.register();
    }

    void loadComplete(FMLLoadCompleteEvent event) {
        MagmaticFluidHandler.post();
        CoolingFluidHandler.post();
        HeatBlockHandler.post();

        ConfigHandler.reload();

        EnergizingRecipeSorter.sort();
    }

}