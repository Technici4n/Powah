package owmii.powah.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.powah.lib.logistics.inventory.slot.SlotBase;
import owmii.powah.block.ender.AbstractEnderTile;

public class EnderCellContainer extends AbstractEnergyContainer<AbstractEnderTile<?, ?, ?>> {
    public EnderCellContainer(int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(Containers.ENDER_CELL.get(), id, inventory, buffer);
    }

    public EnderCellContainer(int id, Inventory inventory, AbstractEnderTile te) {
        super(Containers.ENDER_CELL.get(), id, inventory, te);
    }

    public static EnderCellContainer create(int id, Inventory inventory, FriendlyByteBuf buffer) {
        return new EnderCellContainer(id, inventory, buffer);
    }

    @Override
    protected void init(Inventory inventory, AbstractEnderTile te) {
        super.init(inventory);
        addSlot(new SlotBase(te.getInventory(), 0, 0, 1000));
        addSlot(new SlotBase(te.getInventory(), 1, 4, 4));
        addSlot(new SlotBase(te.getInventory(), 2, 4, 29));
        addPlayerInventory(inventory, 8, 82, 4);
    }
}
