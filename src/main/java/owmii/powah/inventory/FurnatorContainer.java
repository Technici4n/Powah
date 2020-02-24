package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.EnergyContainerBase;
import owmii.lib.inventory.slot.SlotBase;
import owmii.lib.inventory.slot.SlotOverlay;
import owmii.powah.block.furnator.FurnatorTile;

import javax.annotation.Nullable;

public class FurnatorContainer extends EnergyContainerBase<FurnatorTile> {
    public FurnatorContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
    }

    public FurnatorContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, FurnatorTile tile) {
        super(containerType, id, playerInventory, tile);
    }

    public static FurnatorContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new FurnatorContainer(IContainers.FURNATOR, i, playerInventory, buffer);
    }

    @Override
    protected void addContainer(PlayerInventory playerInventory, FurnatorTile te) {
        addSlot(new SlotBase(te.getInventory(), te.builtInSlots(), 85, 17).bg(SlotOverlay.SLOT));
        super.addContainer(playerInventory, te);
    }
}
