package owmii.powah;

import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import owmii.powah.api.FluidCoolantConfig;
import owmii.powah.api.MagmatorFuelValue;
import owmii.powah.api.PassiveHeatSourceConfig;
import owmii.powah.api.SolidCoolantConfig;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tiles;
import owmii.powah.block.cable.CableNet;
import owmii.powah.compat.curios.CuriosCompat;
import owmii.powah.components.PowahComponents;
import owmii.powah.config.v2.PowahConfig;
import owmii.powah.data.PowahDataGenerator;
import owmii.powah.entity.Entities;
import owmii.powah.inventory.Containers;
import owmii.powah.item.CreativeTabs;
import owmii.powah.item.Itms;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.lib.block.IBlock;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.ITankHolder;
import owmii.powah.lib.item.IEnergyContainingItem;
import owmii.powah.lib.item.ItemBlock;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.network.Network;
import owmii.powah.recipe.ReactorFuel;
import owmii.powah.recipe.Recipes;
import owmii.powah.util.Wrench;

@Mod(Powah.MOD_ID)
public class Powah {
    public static final String MOD_ID = "powah";
    private static final ConfigHolder<PowahConfig> CONFIG = PowahConfig.register();
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
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
        PowahComponents.DR.register(modEventBus);
        modEventBus.addListener(RegisterCapabilitiesEvent.class, this::registerTransfer);
        modEventBus.addListener(Network::register);
        modEventBus.addListener(this::registerDataTypeMaps);

        modEventBus.addListener(PowahDataGenerator::gatherData);
        NeoForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickBlock event) -> {
            if (event.getUseBlock() == TriState.FALSE) {
                return;
            }
            if (Wrench.removeWithWrench(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide));
            }
        });
        NeoForge.EVENT_BUS.addListener((ChunkEvent.Unload event) -> {
            if (event.getLevel() instanceof Level level) {
                CableNet.removeChunk(level, event.getChunk());
            }
        });
        if (ModList.get().isLoaded("curios")) {
            CuriosCompat.init();
        }
    }

    private void registerDataTypeMaps(RegisterDataMapTypesEvent event) {
        event.register(ReactorFuel.DATA_MAP_TYPE);
        event.register(SolidCoolantConfig.DATA_MAP_TYPE);
        event.register(PassiveHeatSourceConfig.BLOCK_DATA_MAP);
        event.register(PassiveHeatSourceConfig.FLUID_DATA_MAP);
        event.register(FluidCoolantConfig.DATA_MAP_TYPE);
        event.register(MagmatorFuelValue.DATA_MAP_TYPE);
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
            var validBlock = entry.get().getValidBlocks().stream().iterator().next();
            var be = entry.get().create(BlockPos.ZERO, validBlock.defaultBlockState());
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
                return energyStorage.getExternalStorage(side);
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
                for (var entry : BuiltInRegistries.BLOCK.entrySet()) {
                    var id = entry.getKey();
                    if (id.location().getNamespace().equals(MOD_ID)) {
                        var block = entry.getValue();
                        BlockItem blockItem;
                        if (block instanceof IBlock<?, ?> iBlock) {
                            blockItem = iBlock.getBlockItem(new Item.Properties(), CreativeTabs.MAIN_KEY);
                        } else {
                            blockItem = new ItemBlock<>(block, new Item.Properties(), CreativeTabs.MAIN_KEY);
                        }
                        var name = BuiltInRegistries.BLOCK.getKey(block);
                        Registry.register(BuiltInRegistries.ITEM, name, blockItem);
                    }
                }
            }
        });
    }

}
