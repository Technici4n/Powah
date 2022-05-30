package owmii.lib.logistics;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import owmii.lib.item.Stacks;
import owmii.lib.logistics.inventory.ItemInventory;
import owmii.lib.util.Recipe;
import owmii.lib.util.Stack;

public class Filter {
    private final ItemStack stack;
    private final CompoundNBT nbt;
    private final int size;

    public Filter(ItemStack stack, int size) {
        this.stack = stack;
        this.nbt = stack.getOrCreateChildTag("filter_tag");
        this.size = size;
    }

    public boolean checkStack(ItemStack stack) {
        ItemInventory inv = new ItemInventory(this.size, this.stack);
        Stacks stacks = inv.getStacks();
        for (ItemStack stack1 : stacks) {
            if (compareTag() && Recipe.matchTags(stack, stack1)) {
                return true;
            }
            if (!stack.isEmpty() && stack.isItemEqual(stack1)) {
                if (compareNBT() && !Stack.isNBTEqual(stack, stack1)) {
                    return false;
                }
                return !isBlackList();
            }
        }
        return inv.isEmpty() || isBlackList();
    }

    public boolean isBlackList() {
        return this.nbt.getBoolean("black_list");
    }

    public Filter setBlackList(boolean value) {
        this.nbt.putBoolean("black_list", value);
        return this;
    }

    public boolean compareNBT() {
        return this.nbt.getBoolean("compare_nbt");
    }

    public Filter setCompareNBT(boolean value) {
        this.nbt.putBoolean("compare_nbt", value);
        return this;
    }

    public boolean compareTag() {
        return this.nbt.getBoolean("compare_tag");
    }

    public Filter setCompareTag(boolean value) {
        this.nbt.putBoolean("compare_tag", value);
        return this;
    }

    public void switchTag() {
        this.nbt.putBoolean("compare_tag", !compareTag());
    }

    public void switchNBT() {
        this.nbt.putBoolean("compare_nbt", !compareNBT());
    }

    public void switchMode() {
        this.nbt.putBoolean("black_list", !isBlackList());
    }

    public void clear() {
        new ItemInventory(this.size, this.stack).clear();
    }
}
