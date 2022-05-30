package owmii.lib.logistics.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.block.IInventoryHolder;

import javax.annotation.Nullable;

public class AbstractEnergyContainer<T extends AbstractTileEntity<?, ?> & IInventoryHolder> extends AbstractTileContainer<T> {
    public AbstractEnergyContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(containerType, id, inventory, buffer);
    }

    public AbstractEnergyContainer(@Nullable ContainerType<?> type, int id, PlayerInventory inventory, T te) {
        super(type, id, inventory, te);
    }
}
