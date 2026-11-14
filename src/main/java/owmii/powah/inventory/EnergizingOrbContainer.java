package owmii.powah.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import owmii.powah.block.energizing.EnergizingOrbTile;
import owmii.powah.lib.logistics.inventory.AbstractTileContainer;
import owmii.powah.lib.logistics.inventory.slot.SlotBase;

public class EnergizingOrbContainer extends AbstractTileContainer<EnergizingOrbTile> {
    public EnergizingOrbContainer(int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(Containers.ENERGIZING_ORB.get(), id, inventory, buffer);
    }

    public EnergizingOrbContainer(int id, Inventory inventory, EnergizingOrbTile orb) {
        super(Containers.ENERGIZING_ORB.get(), id, inventory, orb);
    }

    @Override
    protected void init(Inventory inventory, EnergizingOrbTile orb) {
        addOrbSlot(orb, 0, 103, 28);
        for (int i = 0; i < 6; i++) {
            addOrbSlot(orb, i + 1, 10 + (i % 3) * 18, 19 + (i / 3) * 18);
        }
        addPlayerInventory(inventory, 8, 82, 4);
    }

    private void addOrbSlot(EnergizingOrbTile orb, int slot, int x, int y) {
        addSlot(new SlotBase(orb.getInventory(), slot, x, y) {
            @Override
            public void setChanged() {
                orb.onSlotChanged(slot);
            }

            @Override
            public int getMaxStackSize(ItemStack stack) {
                return Math.min(getMaxStackSize(), stack.getMaxStackSize());
            }
        });
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        // When shift-clicking into the orb, skip the output even if it contains the same item.
        return super.moveItemStackTo(stack, startIndex == 0 ? 1 : startIndex, endIndex, reverseDirection);
    }

    @Override
    public boolean stillValid(Player player) {
        return !te.isRemoved() && te.getLevel() == player.level() && Container.stillValidBlockEntity(te, player);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id != 0) {
            return false;
        }
        te.setOutputFilter(getCarried());
        return true;
    }
}
