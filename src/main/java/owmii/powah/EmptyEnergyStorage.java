package owmii.powah;

import net.neoforged.neoforge.energy.IEnergyStorage;

public class EmptyEnergyStorage implements IEnergyStorage {
    public static final IEnergyStorage INSTANCE = new EmptyEnergyStorage();

    @Override
    public int receiveEnergy(int i, boolean bl) {
        return 0;
    }

    @Override
    public int extractEnergy(int i, boolean bl) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return 0;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
