package owmii.lib.block;

import net.minecraft.world.item.ItemStack;
import owmii.lib.logistics.inventory.Inventory;

public interface IInventoryHolder {
    int getSlotLimit(int slot);

    boolean canInsert(int slot, ItemStack stack);

    boolean canExtract(int slot, ItemStack stack);

    default void onSlotChanged(int slot) {
    }

    Inventory getInventory();

    default void shrink(int[] slots, int amount) {
        for (int i : slots) shrink(i, amount);
    }

    default ItemStack shrink(int slot, int amount) {
        ItemStack stack = getStack(slot);
        stack.shrink(amount);
        return stack;
    }

    default ItemStack grow(int slot, int amount) {
        ItemStack stack = getStack(slot);
        stack.grow(amount);
        return stack;
    }

    default ItemStack getStack(int slot) {
        return getInventory().getStackInSlot(slot);
    }

    default void setStackInSlot(int slot, ItemStack stack) {
        getInventory().setStackInSlot(slot, stack);
    }
}
