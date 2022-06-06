package owmii.powah.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.lib.logistics.inventory.slot.SlotBase;
import owmii.powah.block.reactor.ReactorTile;

public class ReactorContainer extends AbstractEnergyContainer<ReactorTile> {
    public ReactorContainer(int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(Containers.REACTOR.get(), id, inventory, buffer);
    }

    public ReactorContainer(int id, Inventory inventory, ReactorTile te) {
        super(Containers.REACTOR.get(), id, inventory, te);
    }

    public static ReactorContainer create(int id, Inventory inventory, FriendlyByteBuf buffer) {
        return new ReactorContainer(id, inventory, buffer);
    }

    @Override
    protected void init(Inventory inventory, ReactorTile te) {
        super.init(inventory, te);
        addSlot(new SlotBase(te.getInventory(), 0, 4, 54));
        addSlot(new SlotBase(te.getInventory(), 1, 73, 29));
        addSlot(new SlotBase(te.getInventory(), 2, 31, 6));
        addSlot(new SlotBase(te.getInventory(), 3, 31, 52));
        addSlot(new SlotBase(te.getInventory(), 4, 120, 52));
        addPlayerInventory(inventory, 8, 84, 4);
    }
}
