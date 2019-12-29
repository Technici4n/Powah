package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.slot.SlotBase;
import owmii.powah.block.generator.magmatic.MagmaticGenTile;

import javax.annotation.Nullable;

public class MagmaticGenContainer extends PowahContainer<MagmaticGenTile> {
    public MagmaticGenContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getTile());
    }

    public MagmaticGenContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, MagmaticGenTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static MagmaticGenContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new MagmaticGenContainer(IContainers.MAGMATIC_GENERATOR, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, MagmaticGenTile tile) {
        addSlot(new SlotBase(tile.getInventory(), 0, 36, 45));
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
