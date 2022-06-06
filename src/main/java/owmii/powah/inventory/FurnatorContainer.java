package owmii.powah.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.lib.logistics.inventory.slot.SlotBase;
import owmii.powah.block.furnator.FurnatorTile;

public class FurnatorContainer extends AbstractEnergyContainer<FurnatorTile> {
    public FurnatorContainer(int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(Containers.FURNATOR.get(), id, inventory, buffer);
    }

    public FurnatorContainer(int id, Inventory inventory, FurnatorTile te) {
        super(Containers.FURNATOR.get(), id, inventory, te);
    }

    public static FurnatorContainer create(int id, Inventory inventory, FriendlyByteBuf buffer) {
        return new FurnatorContainer(id, inventory, buffer);
    }

    @Override
    protected void init(Inventory inventory, FurnatorTile te) {
        super.init(inventory, te);
        addSlot(new SlotBase(te.getInventory(), 0, 4, 54));
        addSlot(new SlotBase(te.getInventory(), 1, 87, 18));
        addPlayerInventory(inventory, 8, 84, 4);
    }
}
