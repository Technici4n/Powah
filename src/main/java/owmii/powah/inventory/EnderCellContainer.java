package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.powah.block.storage.endercell.EnderCellTile;

import javax.annotation.Nullable;

public class EnderCellContainer extends PowahContainer<EnderCellTile> {
    public EnderCellContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getTile());
    }

    public EnderCellContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, EnderCellTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static EnderCellContainer createBasic(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnderCellContainer(IContainers.ENERGY_CELL_BASIC, i, playerInventory, buffer);
    }

    public static EnderCellContainer createSpirited(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnderCellContainer(IContainers.ENERGY_CELL_SPIRITED, i, playerInventory, buffer);
    }

    public static EnderCellContainer createNiotic(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnderCellContainer(IContainers.ENERGY_CELL_NIOTIC, i, playerInventory, buffer);
    }

    public static EnderCellContainer createBlazing(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnderCellContainer(IContainers.ENERGY_CELL_BLAZING, i, playerInventory, buffer);
    }

    public static EnderCellContainer createHardened(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnderCellContainer(IContainers.ENERGY_CELL_HARDENED, i, playerInventory, buffer);
    }

    public static EnderCellContainer createCreative(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnderCellContainer(IContainers.ENERGY_CELL_CREATIVE, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, EnderCellTile tile) {
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
