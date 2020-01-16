package owmii.powah.energy;

import net.minecraft.item.ItemStack;
import owmii.lib.util.Data;

public class ItemPowahStorage extends PowahStorage {
    private final ItemStack stack;
    private boolean isCreative;

    public ItemPowahStorage(ItemStack stack, int capacity, int maxReceive, int maxExtract, int energy, boolean isCreative) {
        super(capacity, maxReceive, maxExtract, energy);
        this.stack = stack;
        this.isCreative = isCreative;
        if (stack.getTag() != null) {
            read(stack.getTag().getCompound(Data.TAG_STORABLE));
            if (isCreative) {
                setEnergy(getMaxEnergyStored());
            }
        }
        setCapacity(capacity);
        setMaxExtract(maxExtract);
        setMaxReceive(maxReceive);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energy = super.receiveEnergy(maxReceive, simulate);
        if (energy > 0 && !simulate && !this.isCreative) {
            write(this.stack.getOrCreateChildTag(Data.TAG_STORABLE));
        }
        return energy;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energy = super.extractEnergy(maxExtract, simulate);
        if (energy > 0 && !simulate && !this.isCreative) {
            write(this.stack.getOrCreateChildTag(Data.TAG_STORABLE));
        }
        return energy;
    }

    @Override
    public int setEnergy(int i) {
        if (this.isCreative && i < getEnergyStored()) return 0;
        return super.setEnergy(i);
    }

    @Override
    public int getEnergyStored() {
        return this.isCreative ? getMaxEnergyStored() : super.getEnergyStored();
    }

    @Override
    public boolean canReceive() {
        return !this.isCreative && super.canReceive();
    }
}
