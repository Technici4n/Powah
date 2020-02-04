package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.EnergyContainerBase;
import owmii.powah.block.energycell.EnergyCellTile;

import javax.annotation.Nullable;

public class EnergyCellContainer extends EnergyContainerBase<EnergyCellTile> {
    public EnergyCellContainer(@Nullable final ContainerType<?> containerType, final int id, final PlayerInventory playerInventory, final EnergyCellTile te) {
        super(containerType, id, playerInventory, te);
    }

    protected EnergyCellContainer(@Nullable final ContainerType<?> containerType, final int id, final PlayerInventory playerInventory, final PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
    }

    public static EnergyCellContainer create(final int i, final PlayerInventory playerInventory, final PacketBuffer buffer) {
        return new EnergyCellContainer(IContainers.ENERGY_CELL, i, playerInventory, buffer);
    }
}
