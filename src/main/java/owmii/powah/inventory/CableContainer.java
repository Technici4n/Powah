package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.powah.block.cable.CableTile;

public class CableContainer extends AbstractEnergyContainer<CableTile> {
    private Direction side = Direction.NORTH;

    public CableContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(Containers.CABLE, id, inventory, buffer);
        this.side = Direction.byIndex(buffer.readInt());
    }

    public CableContainer(int id, PlayerInventory inventory, CableTile te) {
        super(Containers.CABLE, id, inventory, te);
    }

    public static CableContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new CableContainer(id, inventory, buffer);
    }

    public Direction getSide() {
        return this.side;
    }
}
