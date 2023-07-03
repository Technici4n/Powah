package owmii.powah.forge;

import com.google.common.primitives.Ints;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import owmii.powah.EnvHandler;
import owmii.powah.Powah;
import owmii.powah.api.forge.ChargeableItemsEvent;
import owmii.powah.block.Tier;
import owmii.powah.block.cable.CableTile;
import owmii.powah.block.reactor.ReactorPartTile;
import owmii.powah.client.render.tile.ReactorItemRenderer;
import owmii.powah.forge.block.ForgeCableTile;
import owmii.powah.forge.data.ITags;
import owmii.powah.item.ItemGroups;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.lib.block.IBlock;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.ITankHolder;
import owmii.powah.lib.item.IEnergyContainingItem;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.logistics.fluid.Tank;
import owmii.powah.lib.logistics.inventory.Inventory;
import owmii.powah.lib.util.Util;
import owmii.powah.world.gen.Features;

public class ForgeEnvHandler implements EnvHandler {
    private final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    @Override
    public void setupBlockItems() {
        modEventBus.addListener((RegisterEvent event) -> {
            if (event.getRegistryKey() == Registries.ITEM) {
                var registry = event.getForgeRegistry();
                for (var block : ForgeRegistries.BLOCKS.getValues()) {
                    if (block instanceof IBlock<?, ?>iBlock) {
                        var blockItem = iBlock.getBlockItem(new Item.Properties(), ItemGroups.MAIN_KEY);
                        var name = BuiltInRegistries.BLOCK.getKey(block);
                        registry.register(name, blockItem);
                    }
                }
            }
        });
    }

    @Override
    public void registerWorldgen() {
        EnvHandler.super.registerWorldgen();
    }

    @Override
    public void handleReactorInitClient(Consumer<?> consumer) {
        // :help_me:
        ((Consumer<IClientItemExtensions>) consumer).accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new ReactorItemRenderer();
            }
        });
    }

    @Override
    public TagKey<Biome> getOverworldBiomeTag() {
        return BiomeTags.IS_OVERWORLD;
    }

    @Override
    public void scheduleCommonSetup(Runnable runnable) {
        modEventBus.addListener((FMLCommonSetupEvent event) -> runnable.run());
    }

    @Override
    public void registerTransfer() {
        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, (AttachCapabilitiesEvent<BlockEntity> event) -> {
            if (event.getObject() instanceof ReactorPartTile reactorPart) {
                event.addCapability(Powah.id("reactor_part"), new ICapabilityProvider() {
                    @NotNull
                    @Override
                    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                        if (reactorPart.core().isPresent()) {
                            if (cap != ForgeCapabilities.ENERGY || reactorPart.isExtractor()) {
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
                        if (capability == ForgeCapabilities.ENERGY) {
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
                        if (capability == ForgeCapabilities.ITEM_HANDLER) {
                            var inv = holder.getInventory();
                            if (!inv.isBlank()) {
                                return LazyOptional.of(inv::getPlatformWrapper).cast();
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
                        if (capability == ForgeCapabilities.FLUID_HANDLER) {
                            return LazyOptional.of(holder.getTank()::getPlatformWrapper).cast();
                        }
                        return LazyOptional.empty();
                    }
                });
            }
        });
        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, (AttachCapabilitiesEvent<ItemStack> event) -> {
            if (event.getObject().getItem() instanceof IEnergyContainingItem eci) {
                event.addCapability(Powah.id("energy"), new ICapabilityProvider() {
                    @NotNull
                    @Override
                    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction arg) {
                        if (cap == ForgeCapabilities.ENERGY) {
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

    @Override
    public Object createInvWrapper(Inventory inventory) {
        return new IItemHandler() {
            @Override
            public int getSlots() {
                return inventory.getSlots();
            }

            @NotNull
            @Override
            public ItemStack getStackInSlot(int i) {
                return inventory.getStackInSlot(i);
            }

            @NotNull
            @Override
            public ItemStack insertItem(int i, @NotNull ItemStack arg, boolean bl) {
                return inventory.insertItem(i, arg, bl);
            }

            @NotNull
            @Override
            public ItemStack extractItem(int i, int j, boolean bl) {
                return inventory.extractItem(i, j, bl);
            }

            @Override
            public int getSlotLimit(int i) {
                return inventory.getSlotLimit(i);
            }

            @Override
            public boolean isItemValid(int i, @NotNull ItemStack arg) {
                return inventory.isItemValid(i, arg);
            }
        };
    }

    @Override
    public boolean isWrench(ItemStack stack) {
        return stack.is(ITags.Items.WRENCHES);
    }

    @Override
    public IFluidHandler createTankWrapper(Tank tank) {
        return new IFluidHandler() {
            @Override
            public int getTanks() {
                return tank.getTanks();
            }

            @NotNull
            @Override
            public FluidStack getFluidInTank(int i) {
                return FluidStackHooksForge.toForge(tank.getFluidInTank(i));
            }

            @Override
            public int getTankCapacity(int i) {
                return (int) tank.getTankCapacity(i);
            }

            @Override
            public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
                return tank.isFluidValid(i, FluidStackHooksForge.fromForge(fluidStack));
            }

            @Override
            public int fill(FluidStack fluidStack, FluidAction fluidAction) {
                return (int) tank.fill(FluidStackHooksForge.fromForge(fluidStack), fluidAction.simulate());
            }

            @NotNull
            @Override
            public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
                return FluidStackHooksForge.toForge(tank.drain(FluidStackHooksForge.fromForge(fluidStack), fluidAction.simulate()));
            }

            @NotNull
            @Override
            public FluidStack drain(int i, FluidAction fluidAction) {
                return FluidStackHooksForge.toForge(tank.drain(i, fluidAction.simulate()));
            }
        };
    }

    @Override
    public boolean interactWithTank(Player player, InteractionHand hand, Tank tank) {
        return FluidUtil.interactWithFluidHandler(player, hand, createTankWrapper(tank));
    }

    @Override
    public boolean canDischarge(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).map(s -> s.canExtract() && s.getEnergyStored() > 0).orElse(false);
    }

    @Override
    public boolean hasEnergy(Level level, BlockPos pos, Direction side) {
        var be = level.getBlockEntity(pos);
        return be != null && be.getCapability(ForgeCapabilities.ENERGY, side).isPresent();
    }

    @Override
    public long pushEnergy(Level level, BlockPos pos, Direction side, long howMuch) {
        var be = level.getBlockEntity(pos);
        var handler = be != null ? be.getCapability(ForgeCapabilities.ENERGY, side).orElse(null) : null;
        return handler != null ? handler.receiveEnergy(Ints.saturatedCast(howMuch), false) : 0;
    }

    @Override
    public CableTile createCable(BlockPos pos, BlockState state, Tier variant) {
        return new ForgeCableTile(pos, state, variant);
    }

    @Override
    public long chargeItemsInPlayerInv(Player player, long maxPerSlot, long maxTotal, Predicate<ItemStack> allowStack) {
        var stacks = new ArrayList<>(owmii.powah.lib.util.Player.invStacks(player).stream().toList());
        var event = new ChargeableItemsEvent(player);
        MinecraftForge.EVENT_BUS.post(event);
        stacks.addAll(event.getItems());
        stacks.removeIf(allowStack.negate());
        return transferSlotList(IEnergyStorage::receiveEnergy, stacks, maxPerSlot, maxTotal);
    }

    @Override
    public long chargeItemsInContainer(Container container, long maxPerSlot, long maxTotal) {
        var ret = transferSlotList(IEnergyStorage::receiveEnergy,
                IntStream.range(0, container.getContainerSize()).mapToObj(container::getItem).toList(), maxPerSlot, maxTotal);
        container.setChanged();
        return ret;
    }

    @Override
    public long chargeItemsInInventory(Inventory inv, int slotFrom, int slotTo, long maxPerSlot, long maxTotal) {
        // maybe call setChanged?
        return transferSlotList(IEnergyStorage::receiveEnergy, IntStream.range(slotFrom, slotTo).mapToObj(inv::getStackInSlot).toList(), maxPerSlot,
                maxTotal);
    }

    @Override
    public long dischargeItemsInInventory(Inventory inv, long maxPerSlot, long maxTotal) {
        return transferSlotList(IEnergyStorage::extractEnergy, IntStream.range(0, inv.getSlots()).mapToObj(inv::getStackInSlot).toList(), maxPerSlot,
                maxTotal);
    }

    private long transferSlotList(EnergyTransferOperation op, Iterable<ItemStack> stacks, long maxPerStack, long maxTotal) {
        long charged = 0;
        for (ItemStack stack : stacks) {
            if (stack.isEmpty())
                continue;
            var cap = stack.getCapability(ForgeCapabilities.ENERGY).orElse(EmptyEnergyStorage.INSTANCE);
            charged += op.perform(cap, Ints.saturatedCast(Math.min(maxPerStack, maxTotal - charged)), false);
        }
        return charged;
    }

    private interface EnergyTransferOperation {
        int perform(IEnergyStorage storage, int maxAmount, boolean simulate);
    }
}
