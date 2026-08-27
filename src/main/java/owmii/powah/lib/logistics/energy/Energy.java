package owmii.powah.lib.logistics.energy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import owmii.powah.components.PowahComponents;
import owmii.powah.util.Util;

public class Energy extends SnapshotJournal<Long> {
    public static final Codec<Energy> STORAGE_CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.LONG.fieldOf("capacity").forGetter(e -> e.capacity),
            Codec.LONG.fieldOf("stored").forGetter(e -> e.stored)).apply(builder, (capacity, stored) -> {
                var energy = Energy.create(capacity);
                energy.stored = stored;
                return energy;
            }));

    public static final Energy EMPTY = Energy.create(0);
    public static final long MAX = 9_000_000_000_000_000_000L;
    public static final long MIN = 0L;

    private long capacity;
    private long stored;
    private long maxExtract;
    private long maxReceive;

    public Energy(Energy energy) {
        this(energy.capacity, energy.maxExtract, energy.maxReceive);
        setStored(energy.stored);
    }

    public Energy(long capacity, long maxExtract, long maxReceive) {
        this.capacity = capacity;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
    }

    public static Energy create(long capacity) {
        return create(capacity, capacity, capacity);
    }

    public static Energy create(long capacity, long transfer) {
        return create(capacity, transfer, transfer);
    }

    public static Energy from(Energy energy) {
        return new Energy(energy);
    }

    public static Energy create(long capacity, long maxExtract, long maxReceive) {
        return new Energy(capacity, maxExtract, maxReceive);
    }

    public boolean clone(Energy other) {
        boolean flag = false;
        if (this.capacity != other.capacity) {
            setCapacity(other.getCapacity());
            flag = true;
        }
        if (this.stored != other.stored) {
            setStored(other.getStored());
            flag = true;
        }
        if (this.getTransfer() != other.getTransfer()) {
            setTransfer(other.getTransfer());
            flag = true;
        }
        return flag;
    }

    public Energy read(ValueInput input, boolean capacity, boolean transfer) {
        return read(input, "main_energy", capacity, transfer);
    }

    public Energy read(ValueInput input, String key, boolean capacity, boolean transfer) {
        if (capacity) {
            this.capacity = input.getLongOr("energy_capacity_" + key, 0L);
        }
        this.stored = input.getLongOr("energy_stored_" + key, 0L);
        if (transfer) {
            this.maxExtract = input.getLongOr("max_extract_" + key, 0L);
            this.maxReceive = input.getLongOr("max_receive_" + key, 0L);
        }
        return this;
    }

    public void write(ValueOutput output, boolean capacity, boolean transfer) {
        write(output, "main_energy", capacity, transfer);
    }

    public void write(ValueOutput output, String key, boolean capacity, boolean transfer) {
        if (capacity) {
            output.putLong("energy_capacity_" + key, this.capacity);
        }
        output.putLong("energy_stored_" + key, this.stored);
        if (transfer) {
            output.putLong("max_extract_" + key, this.maxExtract);
            output.putLong("max_receive_" + key, this.maxReceive);
        }
    }

    public void readFromItem(ItemStack stack) {
        var energyStored = stack.get(PowahComponents.ENERGY_STORED);
        if (energyStored != null) {
            /*
             * This bypasses the usual clamping to max capacity because it happens before the capacity is set.
             * It is done later via setCapacity()
             */
            this.stored = energyStored;
        }
    }

    public void writeToItem(ItemStack stack) {
        /* Transfer the stored energy from the block entity state to the item form */
        stack.set(PowahComponents.ENERGY_STORED, this.stored);
        this.stored = 0;
    }

    public long insertEnergy(long maxReceive, TransactionContext tx) {
        if (!canReceive())
            return 0;
        long received = Math.min(this.capacity - this.stored, Math.min(this.maxReceive, maxReceive));
        updateSnapshots(tx);
        produce(received);
        return Util.safeInt(received);
    }

    public long extractEnergy(long maxExtract, TransactionContext tx) {
        if (!canExtract())
            return 0;
        long extracted = Math.min(this.stored, Math.min(this.maxExtract, maxExtract));
        updateSnapshots(tx);
        consume(extracted);
        return Util.safeInt(extracted);
    }

    public void addCapacity(long amount) {
        setCapacity(getCapacity() + amount);
    }

    public void removeCapacity(long amount) {
        setCapacity(getCapacity() - amount);
    }

    public long produce(long amount) {
        long min = Math.min(this.capacity - this.stored, Math.max(0, amount));
        this.stored += min;
        return min;
    }

    public long consume(long amount) {
        long min = Math.min(this.stored, Math.max(0, amount));
        this.stored -= min;
        return min;
    }

    public long getEmpty() {
        return getCapacity() - getStored();
    }

    public long getCapacity() {
        return this.capacity;
    }

    public Energy setCapacity(long capacity) {
        this.capacity = Math.max(0, Math.min(MAX, capacity));
        if (this.stored > this.capacity) {
            this.stored = this.capacity;
        }
        return this;
    }

    public Energy setAll(long value) {
        setCapacity(value);
        setTransfer(value);
        return this;
    }

    public long getStored() {
        return Math.min(this.stored, this.capacity);
    }

    public Energy setStored(long stored) {
        this.stored = Math.max(0, Math.min(this.capacity, stored));
        return this;
    }

    public long getMaxExtract() {
        return this.maxExtract;
    }

    public Energy setMaxExtract(long maxExtract) {
        this.maxExtract = maxExtract;
        return this;
    }

    public long getMaxReceive() {
        return this.maxReceive;
    }

    public Energy setMaxReceive(long maxReceive) {
        this.maxReceive = maxReceive;
        return this;
    }

    public Energy setMaxTransfer() {
        this.maxReceive = MAX;
        this.maxExtract = MAX;
        return this;
    }

    public Energy setTransfer(long transfer) {
        this.maxReceive = transfer;
        this.maxExtract = transfer;
        return this;
    }

    public long getTransfer() {
        return Math.max(this.maxExtract, this.maxReceive);
    }

    public long getEnergyStored() {
        return this.stored;
    }

    public long getMaxEnergyStored() {
        return this.capacity;
    }

    public boolean canExtract() {
        return this.maxExtract > 0 && !isEmpty();
    }

    public boolean canReceive() {
        return this.maxReceive > 0 && !isFull();
    }

    public int toComparatorPower() {
        return (int) (subSized() * 15);
    }

    public float subSized() {
        return this.capacity > 0 ? (float) this.stored / this.capacity : 0;
    }

    public boolean hasEnergy() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return this.stored <= 0;
    }

    public boolean isFull() {
        return this.stored > 0 && this.stored >= this.capacity;
    }

    public long getPercent() {
        return (long) (subSized() * 100);
    }

    @Override
    protected Long createSnapshot() {
        return stored;
    }

    @Override
    protected void revertToSnapshot(Long snapshot) {
        this.stored = snapshot;
    }
}
