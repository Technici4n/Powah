package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.EnergyContainerBase;
import owmii.powah.block.magmator.MagmatorTile;

import javax.annotation.Nullable;

public class MagmatorContainer extends EnergyContainerBase<MagmatorTile> {
    public MagmatorContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
    }

    public MagmatorContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, MagmatorTile tile) {
        super(containerType, id, playerInventory, tile);
    }

    public static MagmatorContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new MagmatorContainer(IContainers.MAGMATOR, i, playerInventory, buffer);
    }
}
