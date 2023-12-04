package owmii.powah.lib.logistics.inventory;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemHandlerHelper {
    @NotNull
    public static ItemStack insertItem(ItemStackHandler dest, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return stack;

        for (int i = 0; i < dest.getSlots(); i++) {
            stack = dest.insertItem(i, stack, simulate);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }

        return stack;
    }

    public static boolean canItemStacksStack(@NotNull ItemStack a, @NotNull ItemStack b) {
        return ItemStack.isSameItemSameTags(a, b);
    }

    @NotNull
    public static ItemStack copyStackWithSize(@NotNull ItemStack itemStack, int size) {
        if (size == 0)
            return ItemStack.EMPTY;
        ItemStack copy = itemStack.copy();
        copy.setCount(size);
        return copy;
    }
}
