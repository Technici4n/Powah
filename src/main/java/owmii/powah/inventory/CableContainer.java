package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.powah.block.cable.CableTile;

import javax.annotation.Nullable;

public class CableContainer extends PowahContainer<CableTile> {
    public CableContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getTile());
    }

    public CableContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, CableTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static CableContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new CableContainer(IContainers.CABLE, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, CableTile tile) {
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
