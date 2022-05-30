package owmii.lib.logistics.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.block.IInventoryHolder;

import javax.annotation.Nullable;

public abstract class AbstractTileContainer<T extends AbstractTileEntity<?, ?> & IInventoryHolder> extends AbstractContainer {
    public final T te;

    public AbstractTileContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(containerType, id, inventory, getInventory(inventory.player, buffer.readBlockPos()));
    }

    public AbstractTileContainer(@Nullable ContainerType<?> type, int id, PlayerInventory inventory, T te) {
        super(type, id, inventory);
        this.te = te;
        init(inventory, te);
        this.te.setContainerOpen(true);
    }

    @Override
    protected final void init(PlayerInventory inventory) {
        super.init(inventory);
    }

    protected void init(PlayerInventory inventory, T te) {

    }

    @SuppressWarnings("unchecked")
    protected static <T extends AbstractTileEntity<?, ?>> T getInventory(PlayerEntity player, BlockPos pos) {
        TileEntity tile = player.world.getTileEntity(pos);
        if (tile instanceof AbstractTileEntity<?, ?>)
            return (T) tile;
        return (T) new AbstractTileEntity(TileEntityType.SIGN);
    }

    @Override
    public void onContainerClosed(PlayerEntity player) {
        super.onContainerClosed(player);
        this.te.setContainerOpen(false);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();
            int size = this.te.getInventory().getSlots();
            if (index < size) {
                if (!mergeItemStack(stack1, size, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(stack1, 0, size, false)) {
                return ItemStack.EMPTY;
            }
            if (stack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
                slot.onTake(this.player, stack);
            } else {
                slot.onSlotChanged();
            }
        }
        return stack;
    }
}
