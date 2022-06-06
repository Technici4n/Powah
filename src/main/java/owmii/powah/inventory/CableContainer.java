package owmii.powah.inventory;

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.powah.block.cable.CableTile;

public class CableContainer extends AbstractEnergyContainer<CableTile> {
    private Direction side = Direction.NORTH;

    public CableContainer(int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(Containers.CABLE.get(), id, inventory, buffer);
        this.side = Direction.from3DDataValue(buffer.readInt());
    }

    public CableContainer(int id, Inventory inventory, CableTile te) {
        super(Containers.CABLE.get(), id, inventory, te);
    }

    public static CableContainer create(int id, Inventory inventory, FriendlyByteBuf buffer) {
        return new CableContainer(id, inventory, buffer);
    }

    public Direction getSide() {
        return this.side;
    }
}
