package zeroneye.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import zeroneye.lib.inventory.slot.SlotBase;
import zeroneye.powah.block.discharger.DischargerTile;

import javax.annotation.Nullable;

public class DischargerContainer extends PowahContainer<DischargerTile> {
    public DischargerContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getInv());
    }

    public DischargerContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, DischargerTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static DischargerContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new DischargerContainer(IContainers.DISCHARGER, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, DischargerTile tile) {
        for (int i = 0; i < 4; ++i) {
            addSlot(new SlotBase(tile.getInventory(), i, 46 + i * 23, 37));
        }
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
