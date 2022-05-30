package owmii.lib.logistics.inventory;

import net.minecraft.nbt.CompoundNBT;

public class NBTInventory extends Inventory {
    private CompoundNBT nbt;

    public NBTInventory(int size, CompoundNBT nbt) {
        super(size);
        this.nbt = nbt;
        deserializeNBT(nbt.getCompound(("inventory_stacks")));
    }

    @Override
    public void onContentsChanged(int slot) {
        this.nbt.put("inventory_stacks", serializeNBT());
        deserializeNBT(this.nbt.getCompound(("inventory_stacks")));
    }

    public CompoundNBT getNbt() {
        return this.nbt;
    }
}
