package owmii.powah.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.lib.logistics.inventory.slot.SlotBase;
import owmii.powah.block.magmator.MagmatorTile;

public class MagmatorContainer extends AbstractEnergyContainer<MagmatorTile> {
    public MagmatorContainer(int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(Containers.MAGMATOR.get(), id, inventory, buffer);
    }

    public MagmatorContainer(int id, Inventory inventory, MagmatorTile te) {
        super(Containers.MAGMATOR.get(), id, inventory, te);
    }

    public static MagmatorContainer create(int id, Inventory inventory, FriendlyByteBuf buffer) {
        return new MagmatorContainer(id, inventory, buffer);
    }

    @Override
    protected void init(Inventory inventory, MagmatorTile te) {
        super.init(inventory, te);
        addSlot(new SlotBase(te.getInventory(), 0, 4, 54));
        addPlayerInventory(inventory, 8, 84, 4);
    }
}
