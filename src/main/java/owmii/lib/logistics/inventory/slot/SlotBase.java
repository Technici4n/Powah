package owmii.lib.logistics.inventory.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
    public boolean mayPickup(Player player) {
        if (this.getItemHandler() instanceof Inventory) {
            return !((Inventory) getItemHandler()).extractItemFromSlot(getSlotIndex(), 1, true).isEmpty();
        } else return super.mayPickup(player);
    }

    @Nonnull
    @Override
    public ItemStack remove(int amount) {
        if (this.getItemHandler() instanceof Inventory) {
            return ((Inventory) getItemHandler()).extractItemFromSlot(getSlotIndex(), amount, false);
        } else return super.remove(amount);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isActive() {
        return this.enabled;
    }

    @OnlyIn(Dist.CLIENT)
    public SlotBase setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
