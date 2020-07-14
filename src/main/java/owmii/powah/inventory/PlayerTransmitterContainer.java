package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.lib.logistics.inventory.slot.SlotBase;
import owmii.powah.block.transmitter.PlayerTransmitterTile;

public class PlayerTransmitterContainer extends AbstractEnergyContainer<PlayerTransmitterTile> {
    public PlayerTransmitterContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(Containers.PLAYER_TRANSMITTER, id, inventory, buffer);
    }

    public PlayerTransmitterContainer(int id, PlayerInventory inventory, PlayerTransmitterTile te) {
        super(Containers.PLAYER_TRANSMITTER, id, inventory, te);
    }

    public static PlayerTransmitterContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new PlayerTransmitterContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, PlayerTransmitterTile te) {
        super.init(inventory);
        addSlot(new SlotBase(te.getInventory(), 0, 4, 29));
        addPlayerInventory(inventory, 8, 59, 4);
    }
}
