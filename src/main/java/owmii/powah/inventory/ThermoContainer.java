package owmii.powah.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.lib.logistics.inventory.slot.SlotBase;
import owmii.powah.block.thermo.ThermoTile;

public class ThermoContainer extends AbstractEnergyContainer<ThermoTile> {
    public ThermoContainer(int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(Containers.THERMO.get(), id, inventory, buffer);
    }

    public ThermoContainer(int id, Inventory inventory, ThermoTile te) {
        super(Containers.THERMO.get(), id, inventory, te);
    }

    public static ThermoContainer create(int id, Inventory inventory, FriendlyByteBuf buffer) {
        return new ThermoContainer(id, inventory, buffer);
    }

    @Override
    protected void init(Inventory inventory, ThermoTile te) {
        super.init(inventory, te);
        addSlot(new SlotBase(te.getInventory(), 0, 4, 54));
        addPlayerInventory(inventory, 8, 84, 4);
    }
}
