package owmii.powah.lib.logistics.energy;

import com.google.common.primitives.Ints;
import net.neoforged.neoforge.energy.IEnergyStorage;

final class ItemEnergyStorageAdapter implements IEnergyStorage {
    private final Energy.Item energyItem;

    public ItemEnergyStorageAdapter(Energy.Item energyItem) {
        this.energyItem = energyItem;
    }

    @Override
    public int receiveEnergy(int i, boolean bl) {
        if (energyItem.getCapacity() == 0 || !canReceive()) {
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
}
