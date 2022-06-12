package owmii.powah.lib.logistics.energy;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.item.IEnergyContainingItem;
import owmii.powah.lib.util.NBT;
import owmii.powah.lib.util.Player;
import owmii.powah.lib.util.Stack;
import owmii.powah.lib.util.Util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Energy {
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

    public Energy read(CompoundTag nbt, boolean capacity, boolean transfer) {
        return read(nbt, "main_energy", capacity, transfer);
    }

    public Energy read(CompoundTag nbt, String key, boolean capacity, boolean transfer) {
        if (capacity) {
            this.capacity = nbt.getLong("energy_capacity_" + key);
        }
        this.stored = nbt.getLong("energy_stored_" + key);
        if (transfer) {
            this.maxExtract = nbt.getLong("max_extract_" + key);
            this.maxReceive = nbt.getLong("max_receive_" + key);
        }
        return this;
    }

    public CompoundTag write(boolean capacity, boolean transfer) {
        return write("main_energy", capacity, transfer);
    }

    public CompoundTag write(String key, boolean capacity, boolean transfer) {
        return write(new CompoundTag(), key, capacity, transfer);
    }

    public CompoundTag write(CompoundTag nbt, boolean capacity, boolean transfer) {
        return write(nbt, "main_energy", capacity, transfer);
    }

    public CompoundTag write(CompoundTag nbt, String key, boolean capacity, boolean transfer) {
        if (capacity) {
            nbt.putLong("energy_capacity_" + key, this.capacity);
        }
        nbt.putLong("energy_stored_" + key, this.stored);
        if (transfer) {
            nbt.putLong("max_extract_" + key, this.maxExtract);
            nbt.putLong("max_receive_" + key, this.maxReceive);
        }
        return nbt;
    }

    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;
        long received = Math.min(this.capacity - this.stored, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            produce(received);
        return Util.safeInt(received);
    }

    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;
        long extracted = Math.min(this.stored, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
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

    public long chargeInventory(net.minecraft.world.entity.player.Player player, Predicate<ItemStack> checker) {
        long l = 0L;
        for (ItemStack stack1 : Player.invStacks(player)) {
            if (stack1.isEmpty() || !isPresent(stack1) || !checker.test(stack1)) continue;
            long amount = Math.min(getMaxExtract(), getEnergyStored());
            if (amount <= 0) break;
            int received = Energy.receive(stack1, amount, false);
            l += extractEnergy(received, false);
        }
        /* TODO ARCH
        if (CuriosCompat.isLoaded()) {
            for (ItemStack stack1 : CuriosCompat.getAllStacks(player)) {
                if (stack1.isEmpty() || !isPresent(stack1) || !checker.test(stack1)) continue;
                long amount = Math.min(getMaxExtract(), getEnergyStored());
                if (amount <= 0) break;
                int received = Energy.receive(stack1, amount, false);
                l += extractEnergy(received, false);
            }
        }
         */
        return l;
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

    public int getEnergyStored() {
        return Util.safeInt(this.stored);
    }

    public int getMaxEnergyStored() {
        return Util.safeInt(this.capacity);
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

    public static class Item extends Energy {
        private final ItemStack stack;

        public Item(ItemStack stack, Item energy) {
            super(energy);
            this.stack = stack;
        }

        public Item(ItemStack stack, IEnergyContainingItem.Info info) {
            this(stack, info.capacity(), info.maxExtract(), info.maxInsert());
        }

        public Item(ItemStack stack, long capacity, long maxExtract, long maxReceive) {
            super(capacity, maxExtract, maxReceive);
            this.stack = stack;
            read(Stack.getTagOrEmpty(stack).getCompound(NBT.TAG_STORABLE_STACK), false, false);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int energy = super.receiveEnergy(maxReceive, simulate);
            if (!simulate) {
                write(this.stack.getOrCreateTagElement(NBT.TAG_STORABLE_STACK), false, false);
            }
            return energy;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int energy = super.extractEnergy(maxExtract, simulate);
            if (!simulate) {
                write(this.stack.getOrCreateTagElement(NBT.TAG_STORABLE_STACK), false, false);
            }
            return energy;
        }
    }

    public static int extract(ItemStack stack, long energy, boolean simulate) {
        return getEnergy(stack).orElse(EMPTY).extractEnergy(Util.safeInt(energy), simulate);
    }

    public static int receive(ItemStack stack, long energy, boolean simulate) {
        return getEnergy(stack).orElse(EMPTY).receiveEnergy(Util.safeInt(energy), simulate);
    }

    public static int getStored(ItemStack stack) {
        return getEnergy(stack).orElse(EMPTY).getEnergyStored();
    }

    public static void ifPresent(ItemStack stack, Consumer<Energy.Item> energyItem) {
        get(stack).ifPresent(energyItem);
    }

    public static Optional<Energy.Item> get(ItemStack stack) {
        if (stack.getItem() instanceof IEnergyContainingItem eci) {
            return Optional.ofNullable(eci.getEnergyInfo()).map(info -> new Item(stack, info));
        }
        return Optional.empty();
    }

    public static Optional<Energy> getEnergy(ItemStack stack) {
        return get(stack).map(x -> x);
    }

    public static boolean chargeable(ItemStack stack) {
        if (stack.getItem() instanceof EnergyBlockItem) {
            EnergyBlockItem item = (EnergyBlockItem) stack.getItem();
            if (!item.isChargeable(stack)) {
                return false;
            }
        }
        return isPresent(stack);
    }

    public static boolean isPresent(ItemStack stack) {
        return get(stack).isPresent();
    }
}
