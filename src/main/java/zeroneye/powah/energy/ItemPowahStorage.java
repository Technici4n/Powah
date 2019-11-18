package zeroneye.powah.energy;

import net.minecraft.item.ItemStack;
import zeroneye.lib.util.NBT;

public class ItemPowahStorage extends PowahStorage {
    private final ItemStack stack;
    private boolean isCreative;

    public ItemPowahStorage(ItemStack stack, int capacity, int maxReceive, int maxExtract, int energy, boolean isCreative) {
        super(capacity, maxReceive, maxExtract, energy);
        this.stack = stack;
        this.isCreative = isCreative;
        if (stack.getTag() != null) {
            read(stack.getTag().getCompound(NBT.TAG_STACK));
        }
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energy = super.receiveEnergy(maxReceive, simulate);
        if (energy > 0 && !simulate) {
            write(this.stack.getOrCreateChildTag(NBT.TAG_STACK));
        }
        return energy;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energy = super.extractEnergy(maxExtract, simulate);
        if (energy > 0 && !simulate && !this.isCreative) {
            write(this.stack.getOrCreateChildTag(NBT.TAG_STACK));
        }
        return energy;
    }

    @Override
    public int getEnergyStored() {
        return this.isCreative ? getMaxEnergyStored() : super.getEnergyStored();
    }
}
