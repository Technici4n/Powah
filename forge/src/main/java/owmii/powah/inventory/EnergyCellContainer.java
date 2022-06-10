package owmii.powah.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.powah.lib.logistics.inventory.slot.SlotBase;
import owmii.powah.block.energycell.EnergyCellTile;

public class EnergyCellContainer extends AbstractEnergyContainer<EnergyCellTile> {
    public EnergyCellContainer(int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(Containers.ENERGY_CELL.get(), id, inventory, buffer);
    }

    public EnergyCellContainer(int id, Inventory inventory, EnergyCellTile te) {
        super(Containers.ENERGY_CELL.get(), id, inventory, te);
    }

    public static EnergyCellContainer create(int id, Inventory inventory, FriendlyByteBuf buffer) {
        return new EnergyCellContainer(id, inventory, buffer);
    }

    @Override
    protected void init(Inventory inventory, EnergyCellTile te) {
        super.init(inventory);
        addSlot(new SlotBase(te.getInventory(), 0, 4, 4));
        addSlot(new SlotBase(te.getInventory(), 1, 4, 29));
        addPlayerInventory(inventory, 8, 59, 4);
    }
}
