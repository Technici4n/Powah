package owmii.powah.lib.logistics.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import owmii.powah.lib.logistics.inventory.ItemStackHandler;

public class SlotItemHandler extends Slot {
    private static Container emptyInventory = new SimpleContainer(0);
    private final ItemStackHandler itemHandler;

    public SlotItemHandler(ItemStackHandler itemHandler, int index, int xPosition, int yPosition) {
        super(emptyInventory, index, xPosition, yPosition);
        this.itemHandler = itemHandler;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        if (stack.isEmpty())
            return false;
        return itemHandler.isItemValid(index, stack);
    }

    @Override
    @NotNull
    public ItemStack getItem() {
        return this.getItemHandler().getStackInSlot(index);
    }

    // Override if your ItemStackHandler does not implement ItemStackHandlerModifiable
    @Override
    public void set(@NotNull ItemStack stack) {
        this.getItemHandler().setStackInSlot(index, stack);
        this.setChanged();
    }

    public void initialize(ItemStack stack) {
        this.getItemHandler().setStackInSlot(index, stack);
    }

    @Override
    public void onQuickCraft(@NotNull ItemStack oldStackIn, @NotNull ItemStack newStackIn) {

    }

    @Override
    public int getMaxStackSize() {
        return this.itemHandler.getSlotLimit(this.index);
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        ItemStack maxAdd = stack.copy();
        int maxInput = stack.getMaxStackSize();
        maxAdd.setCount(maxInput);

        ItemStackHandler handler = this.getItemHandler();
        ItemStack currentStack = handler.getStackInSlot(index);

        handler.setStackInSlot(index, ItemStack.EMPTY);

        ItemStack remainder = handler.insertItem(index, maxAdd, true);

        handler.setStackInSlot(index, currentStack);

        return maxInput - remainder.getCount();
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return !this.getItemHandler().extractItem(index, 1, true).isEmpty();
    }

    @Override
    @NotNull
    public ItemStack remove(int amount) {
        return this.getItemHandler().extractItem(index, amount, false);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }
}
