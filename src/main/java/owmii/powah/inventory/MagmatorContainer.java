package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.lib.logistics.inventory.slot.SlotBase;
import owmii.powah.block.magmator.MagmatorTile;

public class MagmatorContainer extends AbstractEnergyContainer<MagmatorTile> {
    public MagmatorContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(Containers.MAGMATOR, id, inventory, buffer);
    }

    public MagmatorContainer(int id, PlayerInventory inventory, MagmatorTile te) {
        super(Containers.MAGMATOR, id, inventory, te);
    }

    public static MagmatorContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new MagmatorContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, MagmatorTile te) {
        super.init(inventory, te);
        addSlot(new SlotBase(te.getInventory(), 0, 4, 54));
        addPlayerInventory(inventory, 8, 84, 4);
    }
}
