package owmii.powah.forge;

import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.AttachCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import owmii.powah.EnvHandler;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.block.cable.CableTile;
import owmii.powah.block.reactor.ReactorPartTile;
import owmii.powah.forge.block.ForgeCableTile;
import owmii.powah.forge.data.ITags;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.ITankHolder;
import owmii.powah.lib.item.IEnergyContainingItem;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.logistics.fluid.Tank;
import owmii.powah.lib.logistics.inventory.Inventory;
import owmii.powah.lib.util.Util;

public class NeoForgeEnvHandler implements EnvHandler {
    private final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    @Override
    public void setupBlockItems() {
    }

    @Override
    public void scheduleCommonSetup(Runnable runnable) {
        modEventBus.addListener((FMLCommonSetupEvent event) -> runnable.run());
    }

    @Override
    public void registerTransfer() {
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
                        if (capability == Capabilities.FLUID_HANDLER) {
                            return LazyOptional.of(holder.getTank()::getPlatformWrapper).cast();
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
                return tank.getFluidInTank(i);
            }

            @Override
            public int getTankCapacity(int i) {
                return (int) tank.getTankCapacity(i);
            }

            @Override
            public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
                return tank.isFluidValid(i, fluidStack);
            }

            @Override
            public int fill(FluidStack fluidStack, FluidAction fluidAction) {
                return (int) tank.fill(fluidStack, fluidAction.simulate());
            }

            @NotNull
            @Override
            public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
                return tank.drain(fluidStack, fluidAction.simulate());
            }

            @NotNull
            @Override
            public FluidStack drain(int i, FluidAction fluidAction) {
                return tank.drain(i, fluidAction.simulate());
            }
        };
    }

    @Override
    public boolean interactWithTank(Player player, InteractionHand hand, Tank tank) {
        return FluidUtil.interactWithFluidHandler(player, hand, createTankWrapper(tank));
    }

    @Override
    public boolean canDischarge(ItemStack stack) {
        return stack.getCapability(Capabilities.ENERGY).map(s -> s.canExtract() && s.getEnergyStored() > 0).orElse(false);
    }

    @Override
    public boolean hasEnergy(Level level, BlockPos pos, Direction side) {
        var be = level.getBlockEntity(pos);
        return be != null && be.getCapability(Capabilities.ENERGY, side).isPresent();
    }

    @Override
    public long pushEnergy(Level level, BlockPos pos, Direction side, long howMuch) {
        var be = level.getBlockEntity(pos);
        var handler = be != null ? be.getCapability(Capabilities.ENERGY, side).orElse(null) : null;
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
        NeoForge.EVENT_BUS.post(event);
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
            var cap = stack.getCapability(Capabilities.ENERGY).orElse(EmptyEnergyStorage.INSTANCE);
            charged += op.perform(cap, Ints.saturatedCast(Math.min(maxPerStack, maxTotal - charged)), false);
        }
        return charged;
    }

    private interface EnergyTransferOperation {
        int perform(IEnergyStorage storage, int maxAmount, boolean simulate);
    }
}
