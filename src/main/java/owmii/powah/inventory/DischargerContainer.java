package owmii.powah.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.lib.logistics.inventory.slot.SlotBase;
import owmii.powah.block.discharger.EnergyDischargerTile;

public class DischargerContainer extends AbstractEnergyContainer<EnergyDischargerTile> {
    public DischargerContainer(int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(Containers.DISCHARGER, id, inventory, buffer);
    }

    public DischargerContainer(int id, Inventory inventory, EnergyDischargerTile te) {
        super(Containers.DISCHARGER, id, inventory, te);
    }

    public static DischargerContainer create(int id, Inventory inventory, FriendlyByteBuf buffer) {
        return new DischargerContainer(id, inventory, buffer);
    }

    @Override
    protected void init(Inventory inventory, EnergyDischargerTile te) {
        super.init(inventory);
        for (int i = 0; i < 7; i++) {
            addSlot(new SlotBase(te.getInventory(), i, 5 + (i * 25), 54));
        }
        addPlayerInventory(inventory, 8, 84, 4);
    }
}
