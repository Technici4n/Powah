package owmii.lib.block;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.logistics.IRedstoneInteract;
import owmii.lib.logistics.SidedStorage;
import owmii.lib.logistics.Transfer;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.logistics.energy.SideConfig;
import owmii.lib.registry.IVariant;
import owmii.lib.util.Util;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AbstractEnergyStorage<V extends Enum<V> & IVariant<V>, C extends IEnergyConfig<V>, B extends AbstractEnergyBlock<V, C, B>> extends AbstractTickableTile<V, B> implements IRedstoneInteract {
    @CapabilityInject(IEnergyStorage.class)
    public static Capability<IEnergyStorage> ENERGY_CAPABILITY = CapabilityEnergy.ENERGY;
    protected final SideConfig sideConfig = new SideConfig(this);
    protected final Energy energy = Energy.create(0);
    private final SidedStorage<LazyOptional<IEnergyStorage>> energyProxies = SidedStorage.create(this::createEnergyProxy);

    public AbstractEnergyStorage(TileEntityType<?> type) {
        this(type, IVariant.getEmpty());
    }

    public AbstractEnergyStorage(TileEntityType<?> type, V variant) {
        super(type, variant);
    }

    private LazyOptional<IEnergyStorage> createEnergyProxy(@Nullable Direction side) {
        return LazyOptional.of(() -> new IEnergyStorage() {
            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                return Util.safeInt(AbstractEnergyStorage.this.extractEnergy(maxExtract, simulate, side));
            }

            @Override
            public int getEnergyStored() {
                return Util.safeInt(AbstractEnergyStorage.this.getEnergy().getStored());
            }

            @Override
            public int getMaxEnergyStored() {
                return AbstractEnergyStorage.this.getEnergy().getMaxEnergyStored();
            }

            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return Util.safeInt(AbstractEnergyStorage.this.receiveEnergy(maxReceive, simulate, side));
            }

            @Override
            public boolean canReceive() {
                return AbstractEnergyStorage.this.canReceiveEnergy(side);
            }

            @Override
            public boolean canExtract() {
                return AbstractEnergyStorage.this.canExtractEnergy(side);
            }
        });
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        this.sideConfig.read(nbt);
        if (!keepEnergy()) {
            this.energy.read(nbt, true, false);
        }
        super.readSync(nbt);
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        this.sideConfig.write(nbt);
        if (!keepEnergy()) {
            this.energy.write(nbt, true, false);
        }
        return super.writeSync(nbt);
    }

    @Override
    public void readStorable(CompoundNBT nbt) {
        if (keepEnergy()) {
            this.energy.read(nbt, false, false);
        }
        super.readStorable(nbt);
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT nbt) {
        if (keepEnergy()) {
            this.energy.write(nbt, false, false);
        }
        return super.writeStorable(nbt);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        this.energyProxies.stream().forEach(LazyOptional::invalidate);
    }

    public boolean keepEnergy() {
        return false;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == ENERGY_CAPABILITY && isEnergyPresent(side)) {
            return this.energyProxies.get(side).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void onFirstTick(World world) {
        super.onFirstTick(world);
        this.energy.setCapacity(getEnergyCapacity());
        this.energy.setTransfer(getEnergyTransfer());
        getSideConfig().init();
        sync();
    }

    protected long extractFromSides(World world) {
        long extracted = 0;
        if (!isRemote()) {
            for (Direction side : Direction.values()) {
                if (canExtractEnergy(side)) {
                    long amount = Math.min(getEnergyTransfer(), getEnergy().getStored());
                    long toExtract = Energy.receive(world.getTileEntity(this.pos.offset(side, getExtractSidesOffsets()[side.ordinal()])), side, Util.safeInt(amount), false);
                    extracted += extractEnergy(Util.safeInt(toExtract), false, side);
                }
            }
        }
        return extracted;
    }

    protected int[] getExtractSidesOffsets() {
        return new int[]{1, 1, 1, 1, 1, 1};
    }

    protected long chargeItems(int i) {
        return chargeItems(0, i);
    }

    protected long chargeItems(int i, int j) {
        long extracted = 0;
        if (!isRemote()) {
            for (ItemStack stack : getChargingInv(i, j)) {
                extracted += chargeItem(stack, getEnergyTransfer());
            }
        }
        return extracted;
    }

    public List<ItemStack> getChargingInv(int i, int j) {
        return IntStream.range(i, j)
                .mapToObj(value -> this.inv.getStacks().get(value))
                .collect(Collectors.toList());
    }

    protected long chargeItem(ItemStack stack, long transfer) {
        if (!stack.isEmpty()) {
            long amount = Math.min(transfer, getEnergy().getStored());
            int received = Energy.receive(stack, amount, false);
            return extractEnergy(received, false, null);
        }
        return 0;
    }

    protected long extractEnergy(int maxExtract, boolean simulate, @Nullable Direction side) {
        if (!canExtractEnergy(side)) return 0;
        final Energy energy = getEnergy();
        long extracted = Math.min(energy.getStored(), Math.min(energy.getMaxExtract(), maxExtract));
        if (!simulate && extracted > 0) {
            energy.consume(extracted);
            sync(10);
        }
        return extracted;
    }

    protected long receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction side) {
        if (!canReceiveEnergy(side)) return 0;
        final Energy energy = getEnergy();
        long received = Math.min(energy.getEmpty(), Math.min(energy.getMaxReceive(), maxReceive));
        if (!simulate && received > 0) {
            energy.produce(received);
            sync(10);
        }
        return received;
    }

    public boolean canExtractEnergy(@Nullable Direction side) {
        return side == null || isEnergyPresent(side) && this.sideConfig.getType(side).canExtract;
    }

    public boolean canReceiveEnergy(@Nullable Direction side) {
        return side == null || isEnergyPresent(side) && this.sideConfig.getType(side).canReceive;
    }

    public boolean isEnergyPresent(@Nullable Direction side) {
        return true;
    }

    @Override
    public void onAdded(World world, BlockState state, BlockState oldState, boolean isMoving) {
        super.onAdded(world, state, oldState, isMoving);
        if (state.getBlock() != oldState.getBlock()) {
            getSideConfig().init();
        }
    }

    protected long getEnergyCapacity() {
        return getConfig().getCapacity(getVariant());
    }

    protected long getEnergyTransfer() {
        return getConfig().getTransfer(getVariant());
    }

    protected C getConfig() {
        return getBlock().getConfig();
    }

    public Energy getEnergy() {
        return this.energy;
    }

    public Transfer getTransferType() {
        return Transfer.ALL;
    }

    public SideConfig getSideConfig() {
        return this.sideConfig;
    }
}
