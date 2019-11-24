package zeroneye.powah.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.EnergyStorage;

public class PowahStorage extends EnergyStorage {
    protected int maxReceive;
    protected int maxExtract;

    public PowahStorage(int capacity) {
        this(capacity, capacity);
    }

    public PowahStorage(PowahStorage storage) {
        this(storage.capacity, storage.maxReceive, storage.maxExtract, storage.energy);
    }

    public PowahStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public PowahStorage(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public PowahStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    public CompoundNBT serialize() {
        return write(new CompoundNBT());
    }

    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("FECapacity", this.capacity);
        compound.putInt("FEStored", this.energy);
        compound.putInt("FEMaxReceive", this.maxReceive);
        compound.putInt("FEMaxExtract", this.maxExtract);
        return compound;
    }

    @SuppressWarnings("unchecked")
    public <T extends PowahStorage> T read(CompoundNBT compound) {
        this.capacity = compound.getInt("FECapacity");
        this.energy = Math.max(0, Math.min(this.capacity, compound.getInt("FEStored")));
        this.maxReceive = compound.getInt("FEMaxReceive");
        this.maxExtract = compound.getInt("FEMaxExtract");
        return (T) this;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    public int getMaxExtract() {
        return maxExtract;
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    public void setEnergy(int i) {
        this.energy = Math.max(0, Math.min(this.capacity, i));
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    public boolean hasEnergy() {
        return this.energy > 0;
    }

    public boolean isFull() {
        return this.energy >= this.capacity;
    }
}
