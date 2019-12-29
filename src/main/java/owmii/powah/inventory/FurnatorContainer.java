package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.slot.SlotBase;
import owmii.powah.block.generator.furnator.FurnatorTile;

import javax.annotation.Nullable;

public class FurnatorContainer extends PowahContainer<FurnatorTile> {
    public FurnatorContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getTile());
    }

    public FurnatorContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, FurnatorTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static FurnatorContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new FurnatorContainer(IContainers.FURNATOR, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, FurnatorTile tile) {
        addSlot(new SlotBase(tile.getInventory(), 0, 13 + 23, 45));
        addSlot(new SlotBase(tile.getInventory(), 1, 57 + 23, 17));
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
