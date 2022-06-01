package owmii.lib.logistics.inventory;

import net.minecraft.nbt.CompoundTag;

public class NBTInventory extends Inventory {
    private CompoundTag nbt;

    public NBTInventory(int size, CompoundTag nbt) {
        super(size);
        this.nbt = nbt;
        deserializeNBT(nbt.getCompound(("inventory_stacks")));
    }

    @Override
    public void onContentsChanged(int slot) {
        this.nbt.put("inventory_stacks", serializeNBT());
        deserializeNBT(this.nbt.getCompound(("inventory_stacks")));
    }

    public CompoundTag getNbt() {
        return this.nbt;
    }
}
