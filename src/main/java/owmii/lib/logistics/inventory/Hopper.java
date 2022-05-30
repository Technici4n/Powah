package owmii.lib.logistics.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import owmii.lib.util.Util;

import java.util.function.Predicate;

public class Hopper {
    private final Inventory inv;
    private boolean push;
    private boolean pull;

    public Hopper(Inventory inv) {
        this.inv = inv;
    }

    public void read(CompoundNBT nbt, String key) {
        this.push = nbt.getBoolean("push_" + key);
        this.pull = nbt.getBoolean("pull_" + key);
    }

    public CompoundNBT write(CompoundNBT nbt, String key) {
        nbt.putBoolean("push_" + key, this.push);
        nbt.putBoolean("pull_" + key, this.pull);
        return nbt;
    }

    public void transfer(IItemHandler to, int max, Predicate<ItemStack> pull, Predicate<ItemStack> push, int... ex) {
        pull(to, max, pull, ex);
        push(to, max, push, ex);
    }

    protected void push(IItemHandler to, int max, Predicate<ItemStack> predicate, int... ex) {
        if (this.push) {
            for (int i = 0; i < this.inv.getSlots(); i++) {
                if (Util.anyMatch(ex, i)) continue;
                ItemStack stack = this.inv.extractItem(i, max, true);
                if (!stack.isEmpty() && predicate.test(stack)) {
                    ItemStack insert = ItemHandlerHelper.insertItem(to, stack.copy(), false);
                    if (!ItemStack.areItemStacksEqual(stack, insert)) {
                        this.inv.extractItem(i, stack.getCount() - insert.getCount(), false);
                        break;
                    }
                }
            }
        }
    }

    protected void pull(IItemHandler from, int max, Predicate<ItemStack> predicate, int... ex) {
        if (this.pull) {
            for (int i = 0; i < from.getSlots(); i++) {
                if (Util.anyMatch(ex, i)) continue;
                ItemStack stack = from.extractItem(i, max, true);
                if (!stack.isEmpty() && predicate.test(stack)) {
                    ItemStack insert = this.inv.insertItem(stack.copy(), false, ex);
                    if (!ItemStack.areItemStacksEqual(stack, insert)) {
                        from.extractItem(i, stack.getCount() - insert.getCount(), false);
                        break;
                    }
                }
            }
        }
    }

    public void switchPull() {
        setPull(!canPull());
    }

    public void switchPush() {
        setPush(!canPush());
    }

    public boolean canPull() {
        return this.pull;
    }

    public Hopper setPull(boolean pull) {
        this.pull = pull;
        return this;
    }

    public boolean canPush() {
        return this.push;
    }

    public Hopper setPush(boolean push) {
        this.push = push;
        return this;
    }

    public boolean isActive() {
        return this.push || this.pull;
    }
}
