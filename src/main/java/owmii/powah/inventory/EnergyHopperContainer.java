package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.EnergyContainerBase;
import owmii.powah.block.energyhopper.EnergyHopperTile;

import javax.annotation.Nullable;

public class EnergyHopperContainer extends EnergyContainerBase<EnergyHopperTile> {
    public EnergyHopperContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, EnergyHopperTile te) {
        super(containerType, id, playerInventory, te);
    }

    public EnergyHopperContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
    }

    public static EnergyHopperContainer create(final int i, final PlayerInventory playerInventory, final PacketBuffer buffer) {
        return new EnergyHopperContainer(IContainers.ENERGY_HOPPER, i, playerInventory, buffer);
    }
}