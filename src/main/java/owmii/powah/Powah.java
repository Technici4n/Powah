package owmii.powah;

import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TriState;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
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
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.ITankHolder;
import owmii.powah.lib.block.PowahBaseEnergyStorageBlockEntity;
import owmii.powah.lib.item.IEnergyContainingItem;
import owmii.powah.lib.logistics.energy.EnergyItemHandler;
import owmii.powah.network.Network;
import owmii.powah.recipe.ReactorFuel;
import owmii.powah.recipe.Recipes;
import owmii.powah.util.Wrench;

@Mod(Powah.MOD_ID)
public class Powah {
    public static final String MOD_ID = "powah";
    private static final ConfigHolder<PowahConfig> CONFIG = PowahConfig.register();

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static PowahConfig config() {
        return CONFIG.getConfig();
    }

    public Powah(IEventBus modEventBus) {

        Blcks.DR.register(modEventBus);
        Tiles.DR.register(modEventBus);
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
        modEventBus.addListener(PowahDataGenerator::gatherRegistryEntries);

        NeoForge.EVENT_BUS.addListener((OnDatapackSyncEvent event) -> {
            event.sendRecipes(Recipes.ENERGIZING.get());
        });
        NeoForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickBlock event) -> {
            if (event.getUseBlock() == TriState.FALSE) {
                return;
            }
            if (Wrench.removeWithWrench(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        });
        NeoForge.EVENT_BUS.addListener((ChunkEvent.Unload event) -> {
            if (event.getLevel() instanceof ServerLevel level) {
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
        event.registerBlockEntity(Capabilities.Energy.BLOCK, Tiles.REACTOR_PART.get(), (reactorPart, unused) -> {
            if (reactorPart.isExtractor()) {
                return reactorPart.getCoreEnergyStorage();
            }
            return null;
        });
        event.registerBlockEntity(Capabilities.Item.BLOCK, Tiles.REACTOR_PART.get(), (reactorPart, unused) -> {
            return reactorPart.getCoreItemHandler();
        });
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, Tiles.REACTOR_PART.get(), (reactorPart, unused) -> {
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
            if (entry.get() instanceof IEnergyContainingItem energyContainingItem) {
                event.registerItem(Capabilities.Energy.ITEM, (_, itemAccess) -> {
                    var energyInfo = energyContainingItem.getEnergyInfo();
                    return new EnergyItemHandler(itemAccess, energyInfo);
                }, entry.get());
            }
        }
    }

    private static void registerBlockEntityCapability(RegisterCapabilitiesEvent event, BlockEntityType<?> beType, Class<?> beClass) {
        if (PowahBaseEnergyStorageBlockEntity.class.isAssignableFrom(beClass)) {
            event.registerBlockEntity(Capabilities.Energy.BLOCK, beType, (o, side) -> {
                var energyStorage = (PowahBaseEnergyStorageBlockEntity<?>) o;
                return energyStorage.getExternalStorage(side);
            });
        }
        if (IInventoryHolder.class.isAssignableFrom(beClass)) {
            event.registerBlockEntity(Capabilities.Item.BLOCK, beType, (o, direction) -> {
                var inv = ((IInventoryHolder) o).getInventory();
                if (inv.size() > 0) {
                    return inv;
                }
                return null;
            });
        }
        if (ITankHolder.class.isAssignableFrom(beClass)) {
            event.registerBlockEntity(Capabilities.Fluid.BLOCK, beType, (o, direction) -> {
                return ((ITankHolder) o).getTank();
            });
        }
    }

}
