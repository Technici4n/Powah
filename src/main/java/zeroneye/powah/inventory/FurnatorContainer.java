package zeroneye.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import zeroneye.lib.inventory.slot.SlotBase;
import zeroneye.powah.block.generator.furnator.FurnatorTile;

import javax.annotation.Nullable;

public class FurnatorContainer extends PowahContainer<FurnatorTile> {
    public FurnatorContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getInv());
    }

    public FurnatorContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, FurnatorTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static FurnatorContainer createBasic(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new FurnatorContainer(IContainers.FURNATOR_BASIC, i, playerInventory, buffer);
    }

    public static FurnatorContainer createSpirited(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new FurnatorContainer(IContainers.FURNATOR_SPIRITED, i, playerInventory, buffer);
    }

    public static FurnatorContainer createNiotic(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new FurnatorContainer(IContainers.FURNATOR_NIOTIC, i, playerInventory, buffer);
    }

    public static FurnatorContainer createBlazing(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new FurnatorContainer(IContainers.FURNATOR_BLAZING, i, playerInventory, buffer);
    }

    public static FurnatorContainer createHardened(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new FurnatorContainer(IContainers.FURNATOR_HARDENED, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, FurnatorTile tile) {
        addSlot(new SlotBase(tile.getInventory(), 0, 13 + 23, 45));
        addSlot(new SlotBase(tile.getInventory(), 1, 57 + 23, 17));
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
