package zeroneye.powah.energy;

import net.minecraft.nbt.CompoundNBT;

public class EnergyStorage_old extends net.minecraftforge.energy.EnergyStorage {
    protected PowerMode powerMode = PowerMode.ALL;
    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;
    protected boolean canReceive;
    protected boolean canExtract;
    protected boolean isCreative;
    protected boolean checkRedstone = true;

    public EnergyStorage_old() {
        super(0);
    }

    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("PowerMode", this.powerMode.ordinal());
        compound.putInt("FECapacity", this.capacity);
        compound.putInt("FEMaxReceive", this.maxReceive);
        compound.putInt("FEMaxExtract", this.maxExtract);
        compound.putInt("EnergyStored", this.energy);
        compound.putBoolean("CanExtractFE", this.canExtract);
        compound.putBoolean("CanReceiveFE", this.canReceive);
        compound.putBoolean("IsCreativeFE", this.isCreative);
        return compound;
    }

    public void read(CompoundNBT compound) {
        this.powerMode = PowerMode.values()[compound.getInt("PowerMode")];
        this.capacity = compound.getInt("FECapacity");
        this.maxReceive = compound.getInt("FEMaxReceive");
        this.maxExtract = compound.getInt("FEMaxExtract");
        this.energy = Math.max(0, Math.min(this.capacity, compound.getInt("EnergyStored")));
        this.canExtract = compound.getBoolean("CanExtractFE");
        this.canReceive = compound.getBoolean("CanReceiveFE");
        this.isCreative = compound.getBoolean("IsCreativeFE");
    }

    public CompoundNBT serialize() {
        return write(new CompoundNBT());
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive() || !getPowerMode().isIn() || !isCheckRedstone())
            return 0;

        int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(getMaxReceive(), maxReceive));
        if (!simulate)
            setEnergy(getEnergyStored() + energyReceived);
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract() || !getPowerMode().isOut() || !isCheckRedstone())
            return 0;

        int energyExtracted = Math.min(getEnergyStored(), Math.min(getMaxExtract(), maxExtract));
        if (!simulate && !this.isCreative)
            setEnergy(getEnergyStored() - energyExtracted);
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return this.isCreative ? Integer.MAX_VALUE : this.energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.isCreative ? Integer.MAX_VALUE : this.capacity;
    }

    @Override
    public boolean canExtract() {
        return this.canExtract && this.maxExtract > 0 || this.isCreative;
    }

    @Override
    public boolean canReceive() {
        return this.canReceive && this.maxReceive > 0 && !this.isCreative;
    }

    public PowerMode getPowerMode() {
        return powerMode;
    }

    public void setPowerMode(PowerMode powerMode) {
        this.powerMode = powerMode;
    }

    public boolean hasEnergy() {
        return getEnergyStored() > 0;
    }

    public void setEnergy(int energy) {
        if (!this.isCreative)
            this.energy = energy;
    }

    public void setCapacity(int capacity) {
        if (!this.isCreative)
            this.capacity = capacity;
    }

    public int getMaxReceive() {
        return this.isCreative ? Integer.MAX_VALUE : this.maxReceive;
    }

    public void setMaxReceive(int maxReceive) {
        if (!this.isCreative)
            this.maxReceive = maxReceive;
    }

    public int getMaxExtract() {
        return this.isCreative ? Integer.MAX_VALUE : this.maxExtract;
    }

    public void setMaxExtract(int maxExtract) {
        if (!this.isCreative)
            this.maxExtract = maxExtract;
    }

    public void setCanReceive(boolean canReceive) {
        this.canReceive = canReceive;
    }

    public void setCanExtract(boolean canExtract) {
        this.canExtract = canExtract;
    }

    public boolean isCreative() {
        return isCreative;
    }

    public void setCreative(boolean creative) {
        isCreative = creative;
    }

    public boolean isCheckRedstone() {
        return checkRedstone;
    }

    public void setCheckRedstone(boolean checkRedstone) {
        this.checkRedstone = checkRedstone;
    }
}
