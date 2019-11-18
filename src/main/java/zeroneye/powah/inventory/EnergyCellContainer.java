package zeroneye.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import zeroneye.lib.inventory.slot.SlotBase;
import zeroneye.powah.block.storage.EnergyCellTile;

import javax.annotation.Nullable;

public class EnergyCellContainer extends PowahContainer<EnergyCellTile> {
    public EnergyCellContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getInv());
    }

    public EnergyCellContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, EnergyCellTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static EnergyCellContainer createBasic(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnergyCellContainer(IContainers.ENERGY_CELL_BASIC, i, playerInventory, buffer);
    }

    public static EnergyCellContainer createSpirited(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnergyCellContainer(IContainers.ENERGY_CELL_SPIRITED, i, playerInventory, buffer);
    }

    public static EnergyCellContainer createNiotic(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnergyCellContainer(IContainers.ENERGY_CELL_NIOTIC, i, playerInventory, buffer);
    }

    public static EnergyCellContainer createBlazing(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnergyCellContainer(IContainers.ENERGY_CELL_BLAZING, i, playerInventory, buffer);
    }

    public static EnergyCellContainer createHardened(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnergyCellContainer(IContainers.ENERGY_CELL_HARDENED, i, playerInventory, buffer);
    }

    public static EnergyCellContainer createCreative(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnergyCellContainer(IContainers.ENERGY_CELL_CREATIVE, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, EnergyCellTile tile) {
        addSlot(new SlotBase(tile, 0, 13 + 23, 45));
        addSlot(new SlotBase(tile, 1, 36 + 23, 45));
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
