package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.slot.SlotBase;
import owmii.powah.block.energycell.EnergyCellTile;

import javax.annotation.Nullable;

public class EnergyCellContainer extends PowahContainer<EnergyCellTile> {
    public EnergyCellContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getTile());
    }

    public EnergyCellContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, EnergyCellTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static EnergyCellContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnergyCellContainer(IContainers.ENERGY_CELL, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, EnergyCellTile tile) {
        addSlot(new SlotBase(tile.getInventory(), 0, 13 + 23, 45));
        addSlot(new SlotBase(tile.getInventory(), 1, 36 + 23, 45));
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
