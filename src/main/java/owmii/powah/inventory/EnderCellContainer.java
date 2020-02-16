package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.EnergyContainerBase;
import owmii.lib.inventory.slot.SlotBase;
import owmii.powah.block.endercell.EnderCellTile;

import javax.annotation.Nullable;

public class EnderCellContainer extends EnergyContainerBase<EnderCellTile> {
    public EnderCellContainer(@Nullable final ContainerType<?> containerType, final int id, final PlayerInventory playerInventory, final EnderCellTile te) {
        super(containerType, id, playerInventory, te);
    }

    protected EnderCellContainer(@Nullable final ContainerType<?> containerType, final int id, final PlayerInventory playerInventory, final PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
    }

    public static EnderCellContainer create(final int i, final PlayerInventory playerInventory, final PacketBuffer buffer) {
        return new EnderCellContainer(IContainers.ENDER_CELL, i, playerInventory, buffer);
    }

    @Override
    protected void addContainer(PlayerInventory playerInventory, EnderCellTile te) {
        addSlot(new SlotBase(te.getInventory(), 0, 148, 37).hide());
        super.addContainer(playerInventory, te);
    }
}
