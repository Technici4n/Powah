package owmii.powah;

import com.google.common.primitives.Ints;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tiles;
import owmii.powah.compat.curios.CuriosCompat;
import owmii.powah.config.v2.PowahConfig;
import owmii.powah.data.DataEvents;
import owmii.powah.entity.Entities;
import owmii.powah.inventory.Containers;
import owmii.powah.item.CreativeTabs;
import owmii.powah.item.Itms;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.lib.block.IBlock;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.ITankHolder;
import owmii.powah.lib.item.IEnergyContainingItem;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.network.Network;
import owmii.powah.recipe.Recipes;
import owmii.powah.util.Util;
import owmii.powah.util.Wrench;

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

    public Powah(IEventBus modEventBus) {

        Blcks.DR.register(modEventBus);
        Tiles.DR.register(modEventBus);
        setupBlockItems(modEventBus);
        Itms.DR.register(modEventBus);
        Containers.DR.register(modEventBus);
        Entities.DR.register(modEventBus);
        Recipes.DR_SERIALIZER.register(modEventBus);
        Recipes.DR_TYPE.register(modEventBus);
        CreativeTabs.DR.register(modEventBus);
        modEventBus.addListener(RegisterCapabilitiesEvent.class, this::registerTransfer);

        Network.register();

        modEventBus.addListener((FMLCommonSetupEvent event) -> {
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

    private void registerTransfer(RegisterCapabilitiesEvent event) {
        // Special handling, since reactor parts delegate to their core
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, Tiles.REACTOR_PART.get(), (reactorPart, unused) -> {
            if (reactorPart.isExtractor()) {
                return reactorPart.getCoreEnergyStorage();
            }
            return null;
        });
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, Tiles.REACTOR_PART.get(), (reactorPart, unused) -> {
            return reactorPart.getCoreItemHandler();
        });
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, Tiles.REACTOR_PART.get(), (reactorPart, unused) -> {
            return reactorPart.getCoreFluidHandler();
        });

        for (var entry : Tiles.DR.getEntries()) {
            var be = entry.get().create(BlockPos.ZERO, Blocks.AIR.defaultBlockState());
            if (be == null) {
                throw new IllegalStateException("Failed to create a dummy BE for " + entry.getId());
            }

            registerBlockEntityCapability(event, entry.get(), be.getClass());
        }

        for (var entry : Itms.DR.getEntries()) {
            if (entry.get() instanceof IEnergyContainingItem eci) {
                event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, unused) -> {
                    var info = eci.getEnergyInfo();
                    if (info == null) {
                        return null;
                    }

                    var energyItem = new Energy.Item(stack, info);
                    return energyItem.createItemCapability();
                }, entry.get());
            }
        }
    }

    private static void registerBlockEntityCapability(RegisterCapabilitiesEvent event, BlockEntityType<?> beType, Class<?> beClass) {
        if (AbstractEnergyStorage.class.isAssignableFrom(beClass)) {
            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, beType, (o, side) -> {
                var energyStorage = (AbstractEnergyStorage<?, ?>) o;
                if (!energyStorage.isEnergyPresent(side)) {
                    return null;
                }

                return new IEnergyStorage() {
                    @Override
                    public int extractEnergy(int maxExtract, boolean simulate) {
                        return Util.safeInt(energyStorage.extractEnergy(maxExtract, simulate, side));
                    }

                    @Override
                    public int getEnergyStored() {
                        return Util.safeInt(energyStorage.getEnergy().getStored());
                    }

                    @Override
                    public int getMaxEnergyStored() {
                        return Ints.saturatedCast(energyStorage.getEnergy().getMaxEnergyStored());
                    }

                    @Override
                    public int receiveEnergy(int maxReceive, boolean simulate) {
                        return Util.safeInt(energyStorage.receiveEnergy(maxReceive, simulate, side));
                    }

                    @Override
                    public boolean canReceive() {
                        return energyStorage.canReceiveEnergy(side);
                    }

                    @Override
                    public boolean canExtract() {
                        return energyStorage.canExtractEnergy(side);
                    }
                };
            });
        }
        if (IInventoryHolder.class.isAssignableFrom(beClass)) {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, beType, (o, direction) -> {
                var inv = ((IInventoryHolder) o).getInventory();
                if (!inv.isBlank()) {
                    return inv;
                }
                return null;
            });
        }
        if (ITankHolder.class.isAssignableFrom(beClass)) {
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, beType, (o, direction) -> {
                return ((ITankHolder) o).getTank();
            });
        }
    }

    private void setupBlockItems(IEventBus modEventBus) {
        modEventBus.addListener((RegisterEvent event) -> {
            if (event.getRegistryKey() == Registries.ITEM) {
                for (var block : BuiltInRegistries.BLOCK) {
                    if (block instanceof IBlock<?, ?>iBlock) {
                        var blockItem = iBlock.getBlockItem(new Item.Properties(), CreativeTabs.MAIN_KEY);
                        var name = BuiltInRegistries.BLOCK.getKey(block);
                        Registry.register(BuiltInRegistries.ITEM, name, blockItem);
                    }
                }
            }
        });
    }
}
