package owmii.powah.lib.block;

import com.google.common.primitives.Ints;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;
import owmii.powah.block.Tier;
import owmii.powah.lib.logistics.IRedstoneInteract;
import owmii.powah.lib.logistics.Transfer;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.logistics.energy.SideConfig;
import owmii.powah.util.ChargeUtil;
import owmii.powah.util.Util;

public abstract class PowahBaseEnergyStorageBlockEntity<B extends PowahBaseEnergyBlock<B>>
        extends PowahBaseTickingBlockEntity<B>
        implements IRedstoneInteract {
    protected final SideConfig sideConfig = new SideConfig(this);
    private final Energy energy = Energy.create(0);
    private final @Nullable EnergyHandler[] externalAdapters = new EnergyHandler[Direction.values().length + 1];
    @SuppressWarnings("unchecked")
    private final BlockCapabilityCache<EnergyHandler, @Nullable Direction>[] capabilityCaches = new BlockCapabilityCache[6];

    public PowahBaseEnergyStorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void readSync(ValueInput input) {
        this.sideConfig.read(input);
        if (!keepEnergy()) {
            this.energy.read(input, true, false);
        }
        super.readSync(input);
    }

    @Override
    public void writeSync(ValueOutput output) {
        this.sideConfig.write(output);
        if (!keepEnergy()) {
            this.energy.write(output, true, false);
        }
        super.writeSync(output);
    }

    @Override
    public void readStorable(ValueInput input) {
        if (keepEnergy()) {
            this.energy.read(input, false, false);
        }
        super.readStorable(input);
    }

    @Override
    public void writeStorable(ValueOutput output) {
        if (keepEnergy()) {
            this.energy.write(output, false, false);
        }
        super.writeStorable(output);
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

    @Override
    public void onPlaced(Level level, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onPlaced(level, state, placer, stack);
        /* Overwrite the energy stored from the saved block entity state */
        this.energy.readFromItem(stack);
    }

    @Override
    public ItemStack storeToStack(ItemStack stack) {
        if (keepStorable()) {
            /* Transfer the energy from the block entity state before writing it */
            this.energy.writeToItem(stack);
        }
        return super.storeToStack(stack);
    }

    protected long extractFromSides(Level world) {
        long extracted = 0;
        if (!isRemote()) {
            for (Direction side : Direction.values()) {
                long amount = Math.min(getEnergyTransfer(), getEnergy().getStored());
                if (amount == 0) {
                    return extracted;
                }

                if (canExtractEnergy(side)) {
                    if (capabilityCaches[side.ordinal()] == null) {
                        capabilityCaches[side.ordinal()] = BlockCapabilityCache.create(Capabilities.Energy.BLOCK, (ServerLevel) world,
                                worldPosition.relative(side), side.getOpposite());
                    }
                    var cap = capabilityCaches[side.ordinal()].getCapability();
                    if (cap == null) {
                        continue;
                    }
                    try (var tx = Transaction.openRoot()) {
                        var toExtract = cap.insert(Ints.saturatedCast(amount), tx);
                        if (toExtract > 0) {
                            extracted += extractEnergy(Util.safeInt(toExtract), tx, side);
                        }
                        tx.commit();
                    }
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
        try (var tx = Transaction.openRoot()) {
            long charged = ChargeUtil.chargeItemsInInventory(inv, i, j, getEnergyTransfer(), energy.getStored(), tx);
            energy.extractEnergy(charged, tx);
            tx.commit();
            return charged;
        }
    }

    public long extractEnergy(long maxExtract, TransactionContext tx, @Nullable Direction side) {
        if (!canExtractEnergy(side))
            return 0;
        var extracted = getEnergy().extractEnergy(maxExtract, tx);
        if (extracted > 0) {
            sync(10, tx);
        }
        return extracted;
    }

    public long insertEnergy(long maxReceive, TransactionContext tx, @Nullable Direction side) {
        if (!canReceiveEnergy(side))
            return 0;
        var result = getEnergy().insertEnergy(maxReceive, tx);
        if (result > 0) {
            sync(10, tx);
        }
        return result;
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

    protected final long getEnergyCapacity() {
        return getBlock().getEnergyCapacity();
    }

    protected final long getEnergyTransfer() {
        return getBlock().getEnergyTransfer();
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
    public EnergyHandler getExternalStorage(@Nullable Direction side) {
        if (!isEnergyPresent(side)) {
            return null;
        }

        int index = side != null ? side.ordinal() : Direction.values().length;
        if (externalAdapters[index] == null) {
            externalAdapters[index] = new ExternalAdapter(side);
        }

        return externalAdapters[index];
    }

    public Tier getTier() {
        return getBlock().getTier();
    }

    private final class ExternalAdapter implements EnergyHandler {
        private final @Nullable Direction side;

        public ExternalAdapter(@Nullable Direction side) {
            this.side = side;
        }

        @Override
        public long getAmountAsLong() {
            return getEnergy().getStored();
        }

        @Override
        public long getCapacityAsLong() {
            return getEnergy().getMaxEnergyStored();
        }

        @Override
        public int insert(int amount, TransactionContext tx) {
            return Util.safeInt(PowahBaseEnergyStorageBlockEntity.this.insertEnergy(amount, tx, side));
        }

        @Override
        public int extract(int amount, TransactionContext tx) {
            return Util.safeInt(PowahBaseEnergyStorageBlockEntity.this.extractEnergy(amount, tx, side));
        }
    }

}
