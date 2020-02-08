package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import owmii.lib.inventory.EnergyContainerBase;
import owmii.powah.block.cable.EnergyCableTile;

import javax.annotation.Nullable;

public class EnergyCableContainer extends EnergyContainerBase<EnergyCableTile> {
    private Direction side = Direction.NORTH;

    public EnergyCableContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, EnergyCableTile te) {
        super(containerType, id, playerInventory, te);
    }

    protected EnergyCableContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        this.side = Direction.byIndex(buffer.readInt());
    }

    public static EnergyCableContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnergyCableContainer(IContainers.ENERGY_CELL, i, playerInventory, buffer);
    }

    @Override
    protected void addContainer(PlayerInventory playerInventory, EnergyCableTile tile) {}

    public Direction getSide() {
        return this.side;
    }
}
