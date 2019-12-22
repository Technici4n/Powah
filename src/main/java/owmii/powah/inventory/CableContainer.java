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

    public static CableContainer createBasic(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new CableContainer(IContainers.CABLE_BASIC, i, playerInventory, buffer);
    }

    public static CableContainer createSpirited(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new CableContainer(IContainers.CABLE_SPIRITED, i, playerInventory, buffer);
    }

    public static CableContainer createNiotic(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new CableContainer(IContainers.CABLE_NIOTIC, i, playerInventory, buffer);
    }

    public static CableContainer createBlazing(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new CableContainer(IContainers.CABLE_BLAZING, i, playerInventory, buffer);
    }

    public static CableContainer createHardened(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new CableContainer(IContainers.CABLE_HARDENED, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, CableTile tile) {
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
