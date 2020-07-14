package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.lib.logistics.inventory.slot.SlotBase;
import owmii.powah.block.energycell.EnergyCellTile;

public class EnergyCellContainer extends AbstractEnergyContainer<EnergyCellTile> {
    public EnergyCellContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(Containers.ENERGY_CELL, id, inventory, buffer);
    }

    public EnergyCellContainer(int id, PlayerInventory inventory, EnergyCellTile te) {
        super(Containers.ENERGY_CELL, id, inventory, te);
    }

    public static EnergyCellContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new EnergyCellContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, EnergyCellTile te) {
        super.init(inventory);
        addSlot(new SlotBase(te.getInventory(), 0, 4, 4));
        addSlot(new SlotBase(te.getInventory(), 1, 4, 29));
        addPlayerInventory(inventory, 8, 59, 4);
    }
}
