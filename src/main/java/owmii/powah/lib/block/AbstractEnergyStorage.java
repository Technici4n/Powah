package owmii.powah.lib.block;

import com.google.common.primitives.Ints;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;
import owmii.powah.block.Tier;
import owmii.powah.config.IEnergyConfig;
import owmii.powah.lib.logistics.IRedstoneInteract;
import owmii.powah.lib.logistics.Transfer;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.logistics.energy.SideConfig;
import owmii.powah.lib.registry.IVariant;
import owmii.powah.util.ChargeUtil;
import owmii.powah.util.Util;

public abstract class AbstractEnergyStorage<C extends IEnergyConfig<Tier>, B extends AbstractEnergyBlock<C, B>> extends AbstractTickableTile<Tier, B>
        implements IRedstoneInteract {
    protected final SideConfig sideConfig = new SideConfig(this);
    protected final Energy energy = Energy.create(0);
    private final @Nullable IEnergyStorage[] externalAdapters = new IEnergyStorage[Direction.values().length + 1];
    @SuppressWarnings("unchecked")
    private final BlockCapabilityCache<IEnergyStorage, @Nullable Direction>[] capabilityCaches = new BlockCapabilityCache[6];

    public AbstractEnergyStorage(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        this(type, pos, state, IVariant.getEmpty());
    }

    public AbstractEnergyStorage(BlockEntityType<?> type, BlockPos pos, BlockState state, Tier variant) {
        super(type, pos, state, variant);
    }

    @Override
    public void readSync(CompoundTag nbt, HolderLookup.Provider registries) {
        this.sideConfig.read(nbt);
        if (!keepEnergy()) {
            this.energy.read(nbt, true, false);
        }
        super.readSync(nbt, registries);
    }

    @Override
    public CompoundTag writeSync(CompoundTag nbt, HolderLookup.Provider registries) {
        this.sideConfig.write(nbt);
        if (!keepEnergy()) {
            this.energy.write(nbt, true, false);
        }
        return super.writeSync(nbt, registries);
    }

    @Override
    public void readStorable(CompoundTag nbt, HolderLookup.Provider registries) {
        if (keepEnergy()) {
            this.energy.read(nbt, false, false);
        }
        super.readStorable(nbt, registries);
    }

    @Override
    public CompoundTag writeStorable(CompoundTag nbt, HolderLookup.Provider registries) {
        if (keepEnergy()) {
            this.energy.write(nbt, false, false);
        }
        return super.writeStorable(nbt, registries);
    }

    public boolean keepEnergy() {
        return false;
    }

    @Override
    protected void onFirstTick(Level world) {
        super.onFirstTick(world);
        this.energy.setCapacity(getEnergyCapacity());
        this.energy.setTransfer(getEnergyTransfer());
        getSideConfig().init();
        sync();
    }

    protected long extractFromSides(Level world) {
        long extracted = 0;
        if (!isRemote()) {
            for (Direction side : Direction.values()) {
                if (canExtractEnergy(side)) {
                    if (capabilityCaches[side.ordinal()] == null) {
                        capabilityCaches[side.ordinal()] = BlockCapabilityCache.create(Capabilities.EnergyStorage.BLOCK, (ServerLevel) world,
                                worldPosition.relative(side), side.getOpposite());
                    }
                    long amount = Math.min(getEnergyTransfer(), getEnergy().getStored());
                    var cap = capabilityCaches[side.ordinal()].getCapability();
                    long toExtract = cap == null ? 0 : cap.receiveEnergy(Ints.saturatedCast(amount), false);
                    extracted += extractEnergy(Util.safeInt(toExtract), false, side);
                }
            }
        }
        return extracted;
    }

    protected long chargeItems(int i) {
        return chargeItems(0, i);
    }

    protected long chargeItems(int i, int j) {
        final Energy energy = getEnergy();
        long charged = ChargeUtil.chargeItemsInInventory(inv, i, j, getEnergyTransfer(), energy.getStored());
        energy.consume(charged);
        return charged;
    }

    public long extractEnergy(long maxExtract, boolean simulate, @Nullable Direction side) {
        if (!canExtractEnergy(side))
            return 0;
        final Energy energy = getEnergy();
        long extracted = Math.min(energy.getStored(), Math.min(energy.getMaxExtract(), maxExtract));
        if (!simulate && extracted > 0) {
            energy.consume(extracted);
            sync(10);
        }
        return extracted;
    }

    public long receiveEnergy(long maxReceive, boolean simulate, @Nullable Direction side) {
        if (!canReceiveEnergy(side))
            return 0;
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
    public void onAdded(Level world, BlockState state, BlockState oldState, boolean isMoving) {
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

    @Nullable
    public IEnergyStorage getExternalStorage(@Nullable Direction side) {
        if (!isEnergyPresent(side)) {
            return null;
        }

        int index = side != null ? side.ordinal() : Direction.values().length;
        if (externalAdapters[index] == null) {
            externalAdapters[index] = new ExternalAdapter(side);
        }

        return externalAdapters[index];
    }

    private final class ExternalAdapter implements IEnergyStorage {
        private final @Nullable Direction side;

        public ExternalAdapter(@Nullable Direction side) {
            this.side = side;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return Util.safeInt(AbstractEnergyStorage.this.extractEnergy(maxExtract, simulate, side));
        }

        @Override
        public int getEnergyStored() {
            return Util.safeInt(getEnergy().getStored());
        }

        @Override
        public int getMaxEnergyStored() {
            return Ints.saturatedCast(getEnergy().getMaxEnergyStored());
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
    }

}
