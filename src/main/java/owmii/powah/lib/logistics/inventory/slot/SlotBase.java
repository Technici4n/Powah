package owmii.powah.lib.logistics.inventory.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import owmii.powah.lib.logistics.inventory.Inventory;
import owmii.powah.lib.logistics.inventory.ItemStackHandler;

public class SlotBase extends SlotItemHandler {
    private boolean enabled = true;

    public SlotBase(ItemStackHandler handler, int id, int x, int y) {
        super(handler, id, x, y);
    }

    @Override
    public boolean mayPickup(Player player) {
        if (this.getItemHandler() instanceof Inventory) {
            return !((Inventory) getItemHandler()).extractItemFromSlot(index, 1, true).isEmpty();
        } else
            return super.mayPickup(player);
    }

    @NotNull
    @Override
    public ItemStack remove(int amount) {
        if (this.getItemHandler() instanceof Inventory) {
            return ((Inventory) getItemHandler()).extractItemFromSlot(index, amount, false);
        } else
            return super.remove(amount);
    }

    @Override
    public boolean isActive() {
        return this.enabled;
    }

    public SlotBase setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
