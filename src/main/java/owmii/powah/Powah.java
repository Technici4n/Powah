package owmii.powah;

import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tiles;
import owmii.powah.config.v2.PowahConfig;
import owmii.powah.entity.Entities;
import owmii.powah.forge.compat.curios.CuriosCompat;
import owmii.powah.forge.data.DataEvents;
import owmii.powah.inventory.Containers;
import owmii.powah.item.ItemGroups;
import owmii.powah.item.Itms;
import owmii.powah.lib.block.IBlock;
import owmii.powah.lib.util.Wrench;
import owmii.powah.network.Network;
import owmii.powah.recipe.Recipes;

@Mod(Powah.MOD_ID)
public class Powah {
    public static final String MOD_ID = "powah";
    private static final ConfigHolder<PowahConfig> CONFIG = PowahConfig.register();
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static PowahConfig config() {
        return CONFIG.getConfig();
    }

    public Powah() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Blcks.DR.register(modEventBus);
        Tiles.DR.register(modEventBus);
        setupBlockItems(modEventBus);
        Itms.DR.register(modEventBus);
        Containers.DR.register(modEventBus);
        Entities.DR.register(modEventBus);
        Recipes.DR_SERIALIZER.register(modEventBus);
        Recipes.DR_TYPE.register(modEventBus);
        ItemGroups.DR.register(modEventBus);

        EnvHandler.INSTANCE.registerWorldgen();
        EnvHandler.INSTANCE.registerTransfer();
        Network.register();

        EnvHandler.INSTANCE.scheduleCommonSetup(() -> {
            // TODO: move to config
            PowahAPI.registerSolidCoolant(Blocks.SNOW_BLOCK, 48, -3);
            PowahAPI.registerSolidCoolant(Items.SNOWBALL, 12, -3);
            PowahAPI.registerSolidCoolant(Blocks.ICE, 48, -5);
            PowahAPI.registerSolidCoolant(Blocks.PACKED_ICE, 192, -8);
            PowahAPI.registerSolidCoolant(Blocks.BLUE_ICE, 568, -17);
            PowahAPI.registerSolidCoolant(Blcks.DRY_ICE.get(), 712, -32);
        });

        modEventBus.addListener(DataEvents::gatherData);
        NeoForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickBlock event) -> {
            if (event.getUseBlock() == Event.Result.DENY) {
                return;
            }
            if (Wrench.removeWithWrench(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide));
            }
        });

        if (FMLEnvironment.dist.isClient()) {
            try {
                Class.forName("owmii.powah.client.PowahClient").getMethod("init").invoke(null);
            } catch (Exception exception) {
                throw new RuntimeException("Failed to run powah forge client init", exception);
            }
        }

        if (ModList.get().isLoaded("curios")) {
            CuriosCompat.init();
        }
    }

    private void setupBlockItems(IEventBus modEventBus) {
        modEventBus.addListener((RegisterEvent event) -> {
            if (event.getRegistryKey() == Registries.ITEM) {
                for (var block : BuiltInRegistries.BLOCK) {
                    if (block instanceof IBlock<?, ?> iBlock) {
                        var blockItem = iBlock.getBlockItem(new Item.Properties(), ItemGroups.MAIN_KEY);
                        var name = BuiltInRegistries.BLOCK.getKey(block);
                        Registry.register(BuiltInRegistries.ITEM, name, blockItem);
                    }
                }
            }
        });
    }
}
