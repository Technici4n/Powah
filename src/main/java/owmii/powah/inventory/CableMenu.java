package owmii.powah.inventory;

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.block.cable.CableBlockEntity;
import owmii.powah.lib.logistics.inventory.BaseEnergyMenu;

public class CableMenu extends BaseEnergyMenu<CableBlockEntity> {
    private Direction side = Direction.NORTH;

    public CableMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(Containers.CABLE.get(), id, inventory, buffer);
        if (buffer.readableBytes() >= 4) {
            this.side = Direction.from3DDataValue(buffer.readInt());
        }
    }

    public CableMenu(int id, Inventory inventory, CableBlockEntity te) {
        super(Containers.CABLE.get(), id, inventory, te);
    }

    public static CableMenu create(int id, Inventory inventory, FriendlyByteBuf buffer) {
        return new CableMenu(id, inventory, buffer);
    }

    public Direction getSide() {
        return this.side;
    }
}
