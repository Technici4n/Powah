package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.ContainerBase;
import owmii.powah.block.PowahTile;

import javax.annotation.Nullable;

public class PowahContainer<T extends PowahTile> extends ContainerBase<T> {
    protected PowahContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
    }

    protected PowahContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, T tile) {
        super(containerType, id, playerInventory, tile);
    }
}
