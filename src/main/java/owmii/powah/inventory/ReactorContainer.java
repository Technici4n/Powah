package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.lib.logistics.inventory.slot.SlotBase;
import owmii.powah.block.reactor.ReactorTile;

public class ReactorContainer extends AbstractEnergyContainer<ReactorTile> {
    public ReactorContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(Containers.REACTOR, id, inventory, buffer);
    }

    public ReactorContainer(int id, PlayerInventory inventory, ReactorTile te) {
        super(Containers.REACTOR, id, inventory, te);
    }

    public static ReactorContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new ReactorContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, ReactorTile te) {
        super.init(inventory, te);
        addSlot(new SlotBase(te.getInventory(), 0, 4, 54));
        addSlot(new SlotBase(te.getInventory(), 1, 73, 29));
        addSlot(new SlotBase(te.getInventory(), 2, 31, 6));
        addSlot(new SlotBase(te.getInventory(), 3, 31, 52));
        addSlot(new SlotBase(te.getInventory(), 4, 120, 52));
        addPlayerInventory(inventory, 8, 84, 4);
    }
}
