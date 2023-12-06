package owmii.powah;

import com.google.common.primitives.Ints;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.AttachCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tiles;
import owmii.powah.block.reactor.ReactorPartTile;
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
        CreativeTabs.DR.register(modEventBus);

        registerTransfer();
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

    private void registerTransfer() {
        NeoForge.EVENT_BUS.addGenericListener(BlockEntity.class, (AttachCapabilitiesEvent<BlockEntity> event) -> {
            if (event.getObject() instanceof ReactorPartTile reactorPart) {
                event.addCapability(Powah.id("reactor_part"), new ICapabilityProvider() {
                    @NotNull
                    @Override
                    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                        if (reactorPart.core().isPresent()) {
                            if (cap != Capabilities.ENERGY || reactorPart.isExtractor()) {
                                return reactorPart.core().get().getCapability(cap, side);
                            }
                        }
                        return LazyOptional.empty();
                    }
                });
            }
            if (event.getObject() instanceof AbstractEnergyStorage<?, ?>energyStorage) {
                event.addCapability(Powah.id("energy"), new ICapabilityProvider() {
                    @NotNull
                    @Override
                    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
                        if (capability == Capabilities.ENERGY) {
                            if (energyStorage.isEnergyPresent(side)) {
                                return LazyOptional.of(() -> new IEnergyStorage() {
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
                                }).cast();
                            }
                        }
                        return LazyOptional.empty();
                    }
                });
            }
            if (event.getObject() instanceof IInventoryHolder holder) {
                event.addCapability(Powah.id("inv"), new ICapabilityProvider() {
                    @NotNull
                    @Override
                    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
                        if (capability == Capabilities.ITEM_HANDLER) {
                            var inv = holder.getInventory();
                            if (!inv.isBlank()) {
                                return LazyOptional.of(() -> inv).cast();
                            }
                        }
                        return LazyOptional.empty();
                    }
                });
            }
            if (event.getObject() instanceof ITankHolder holder) {
                event.addCapability(Powah.id("tank"), new ICapabilityProvider() {
                    @NotNull
                    @Override
                    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
                        if (capability == Capabilities.FLUID_HANDLER) {
                            return LazyOptional.of(holder::getTank).cast();
                        }
                        return LazyOptional.empty();
                    }
                });
            }
        });
        NeoForge.EVENT_BUS.addGenericListener(ItemStack.class, (AttachCapabilitiesEvent<ItemStack> event) -> {
            if (event.getObject().getItem() instanceof IEnergyContainingItem eci) {
                event.addCapability(Powah.id("energy"), new ICapabilityProvider() {
                    @NotNull
                    @Override
                    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction arg) {
                        if (cap == Capabilities.ENERGY) {
                            var info = eci.getEnergyInfo();
                            if (info != null) {
                                return LazyOptional.of(() -> {
                                    var energyItem = new Energy.Item(event.getObject(), info);
                                    return new IEnergyStorage() {
                                        @Override
                                        public int receiveEnergy(int i, boolean bl) {
                                            if (info.capacity() == 0 || !energyItem.canReceive()) {
                                                return 0;
                                            }
                                            return Ints.saturatedCast(energyItem.receiveEnergy(i, bl));
                                        }

                                        @Override
                                        public int extractEnergy(int i, boolean bl) {
                                            if (!energyItem.canExtract()) {
                                                return 0;
                                            }
                                            return Ints.saturatedCast(energyItem.extractEnergy(i, bl));
                                        }

                                        @Override
                                        public int getEnergyStored() {
                                            return Ints.saturatedCast(energyItem.getEnergyStored());
                                        }

                                        @Override
                                        public int getMaxEnergyStored() {
                                            return Ints.saturatedCast(energyItem.getMaxEnergyStored());
                                        }

                                        @Override
                                        public boolean canExtract() {
                                            return energyItem.canExtract();
                                        }

                                        @Override
                                        public boolean canReceive() {
                                            return energyItem.canReceive();
                                        }
                                    };
                                }).cast();
                            }
                        }
                        return LazyOptional.empty();
                    }
                });
            }
        });
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
