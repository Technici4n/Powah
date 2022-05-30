package owmii.lib.logistics.inventory;

import net.minecraft.item.ItemStack;

public class ItemInventory extends NBTInventory {
    private final ItemStack stack;

    public ItemInventory(int size, ItemStack stack) {
        super(size, stack.getOrCreateTag());
        this.stack = stack;
    }

    public ItemStack getStack() {
        return this.stack;
    }
}
