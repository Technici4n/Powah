package owmii.powah;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import owmii.lib.Lollipop;
import owmii.lib.api.IClient;
import owmii.lib.api.IMod;
import owmii.lib.block.IBlock;
import owmii.lib.config.IConfig;
import owmii.lib.network.Network;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tiles;
import owmii.powah.client.Client;
import owmii.powah.config.ConfigHandler;
import owmii.powah.config.Configs;
import owmii.powah.entity.Entities;
import owmii.powah.inventory.Containers;
import owmii.powah.item.ItemGroups;
import owmii.powah.item.Itms;
import owmii.powah.network.Packets;
import owmii.powah.recipe.Recipes;
import owmii.powah.world.gen.WorldGen;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mod(Powah.MOD_ID)
public class Powah implements IMod {
    public static final String MOD_ID = "powah";
    public static final Network NET = new Network(MOD_ID);
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public Powah() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(Lollipop::setup);

        Blcks.DR.register(modEventBus);
        Tiles.DR.register(modEventBus);
        modEventBus.addGenericListener(Item.class, (RegistryEvent.Register<Item> event) -> {
            for (var block : ForgeRegistries.BLOCKS.getValues()) {
                if (block instanceof IBlock<?,?> iBlock) {
                    var blockItem = iBlock.getBlockItem(new Item.Properties(), ItemGroups.MAIN);
                    blockItem.setRegistryName(block.getRegistryName());
                    event.getRegistry().register(blockItem);
                }
            }
        });
        Itms.DR.register(modEventBus);
        Containers.DR.register(modEventBus);
        Entities.DR.register(modEventBus);
        Recipes.DR_SERIALIZER.register(modEventBus);
        Recipes.DR_TYPE.register(modEventBus);
        modEventBus.addGenericListener(Feature.class, WorldGen::register);

        loadListeners();
        Configs.register();
    }

    @Override
    public void setup(FMLCommonSetupEvent event) {
        PowahAPI.registerSolidCoolant(Blocks.SNOW_BLOCK, 48, -3);
        PowahAPI.registerSolidCoolant(Items.SNOWBALL, 12, -3);
        PowahAPI.registerSolidCoolant(Blocks.ICE, 48, -5);
        PowahAPI.registerSolidCoolant(Blocks.PACKED_ICE, 192, -8);
        PowahAPI.registerSolidCoolant(Blocks.BLUE_ICE, 568, -17);
        PowahAPI.registerSolidCoolant(Blcks.DRY_ICE.get(), 712, -32);

        Packets.register();
    }

    @Override
    public void loadComplete(FMLLoadCompleteEvent event) {
        Configs.ALL.forEach(IConfig::reload);
        ConfigHandler.post();
    }

    @Nullable
    @Override
    public IClient getClient() {
        return Client.INSTANCE;
    }
}