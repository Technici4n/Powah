package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.lib.logistics.inventory.slot.SlotBase;
import owmii.powah.block.thermo.ThermoTile;

public class ThermoContainer extends AbstractEnergyContainer<ThermoTile> {
    public ThermoContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(Containers.THERMO, id, inventory, buffer);
    }

    public ThermoContainer(int id, PlayerInventory inventory, ThermoTile te) {
        super(Containers.THERMO, id, inventory, te);
    }

    public static ThermoContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new ThermoContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, ThermoTile te) {
        super.init(inventory, te);
        addSlot(new SlotBase(te.getInventory(), 0, 4, 54));
        addPlayerInventory(inventory, 8, 84, 4);
    }
}
