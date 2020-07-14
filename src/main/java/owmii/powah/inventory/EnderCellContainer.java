package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.lib.logistics.inventory.slot.SlotBase;
import owmii.powah.block.ender.AbstractEnderTile;

public class EnderCellContainer extends AbstractEnergyContainer<AbstractEnderTile<?, ?, ?>> {
    public EnderCellContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(Containers.ENDER_CELL, id, inventory, buffer);
    }

    public EnderCellContainer(int id, PlayerInventory inventory, AbstractEnderTile te) {
        super(Containers.ENDER_CELL, id, inventory, te);
    }

    public static EnderCellContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new EnderCellContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, AbstractEnderTile te) {
        super.init(inventory);
        addSlot(new SlotBase(te.getInventory(), 0, 0, 1000));
        addSlot(new SlotBase(te.getInventory(), 1, 4, 4));
        addSlot(new SlotBase(te.getInventory(), 2, 4, 29));
        addPlayerInventory(inventory, 8, 82, 4);
    }
}
