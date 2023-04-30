package owmii.powah.lib.logistics.inventory;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.lib.block.IInventoryHolder;

public class AbstractEnergyContainer<T extends AbstractTileEntity<?, ?> & IInventoryHolder> extends AbstractTileContainer<T> {
    public AbstractEnergyContainer(@Nullable MenuType<?> containerType, int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(containerType, id, inventory, buffer);
    }

    public AbstractEnergyContainer(@Nullable MenuType<?> type, int id, Inventory inventory, T te) {
        super(type, id, inventory, te);
    }
}
