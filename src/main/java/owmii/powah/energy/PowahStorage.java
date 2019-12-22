package owmii.powah.energy;

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

    public static PowahStorage fromNBT(CompoundNBT compound) {
        return new PowahStorage(0).read(compound);
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

    public PowahStorage read(CompoundNBT compound) {
        this.capacity = compound.getInt("FECapacity");
        this.energy = Math.max(0, Math.min(this.capacity, compound.getInt("FEStored")));
        this.maxReceive = compound.getInt("FEMaxReceive");
        this.maxExtract = compound.getInt("FEMaxExtract");
        return this;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        if (getEnergyStored() > capacity) {
            setEnergy(capacity);
        }
    }

    public int getDif() {
        return getMaxEnergyStored() - getEnergyStored();
    }

    public int getMaxReceive() {
        return this.maxReceive;
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    public int getMaxExtract() {
        return this.maxExtract;
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    public int consume(int amount) {
        return setEnergy(getEnergyStored() - amount);
    }

    public int setEnergy(int i) {
        int e = Math.max(0, Math.min(this.capacity, i));
        this.energy = e;
        return e;
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
