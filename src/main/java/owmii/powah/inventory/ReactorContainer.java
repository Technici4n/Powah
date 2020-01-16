package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.slot.SlotBase;
import owmii.powah.block.generator.reactor.ReactorTile;

import javax.annotation.Nullable;

public class ReactorContainer extends PowahContainer<ReactorTile> {
    public ReactorContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getTile());
    }

    public ReactorContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, ReactorTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static ReactorContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new ReactorContainer(IContainers.REACTOR, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, ReactorTile tile) {
        addSlot(new SlotBase(tile.getInventory(), 0, 71, 28));
        addSlot(new SlotBase(tile.getInventory(), 1, 28, 5));
        addSlot(new SlotBase(tile.getInventory(), 2, 28, 51));
        addSlot(new SlotBase(tile.getInventory(), 3, 127, 51));
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
