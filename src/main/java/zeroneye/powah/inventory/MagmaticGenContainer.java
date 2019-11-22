package zeroneye.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import zeroneye.powah.block.generator.magmatic.MagmaticGenTile;

import javax.annotation.Nullable;

public class MagmaticGenContainer extends PowahContainer<MagmaticGenTile> {
    public MagmaticGenContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getInv());
    }

    public MagmaticGenContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, MagmaticGenTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static MagmaticGenContainer createBasic(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new MagmaticGenContainer(IContainers.ENERGY_CELL_BASIC, i, playerInventory, buffer);
    }

    public static MagmaticGenContainer createSpirited(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new MagmaticGenContainer(IContainers.ENERGY_CELL_SPIRITED, i, playerInventory, buffer);
    }

    public static MagmaticGenContainer createNiotic(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new MagmaticGenContainer(IContainers.ENERGY_CELL_NIOTIC, i, playerInventory, buffer);
    }

    public static MagmaticGenContainer createBlazing(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new MagmaticGenContainer(IContainers.ENERGY_CELL_BLAZING, i, playerInventory, buffer);
    }

    public static MagmaticGenContainer createHardened(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new MagmaticGenContainer(IContainers.ENERGY_CELL_HARDENED, i, playerInventory, buffer);
    }

    public static MagmaticGenContainer createCreative(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new MagmaticGenContainer(IContainers.ENERGY_CELL_CREATIVE, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, MagmaticGenTile tile) {
        //addSlot(new SlotBase(tile, 0, 13 + 23, 45));
        //addSlot(new SlotBase(tile, 1, 36 + 23, 45));
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
