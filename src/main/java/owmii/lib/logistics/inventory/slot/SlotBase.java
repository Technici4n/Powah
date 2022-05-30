package owmii.lib.logistics.inventory.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import owmii.lib.logistics.inventory.Inventory;

import javax.annotation.Nonnull;

public class SlotBase extends SlotItemHandler {
    private boolean enabled = true;

    public SlotBase(IItemHandler handler, int id, int x, int y) {
        super(handler, id, x, y);
    }

    @Override
    public boolean canTakeStack(PlayerEntity player) {
        if (this.getItemHandler() instanceof Inventory) {
            return !((Inventory) getItemHandler()).extractItemFromSlot(getSlotIndex(), 1, true).isEmpty();
        } else return super.canTakeStack(player);
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getItemHandler() instanceof Inventory) {
            return ((Inventory) getItemHandler()).extractItemFromSlot(getSlotIndex(), amount, false);
        } else return super.decrStackSize(amount);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isEnabled() {
        return this.enabled;
    }

    @OnlyIn(Dist.CLIENT)
    public SlotBase setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
