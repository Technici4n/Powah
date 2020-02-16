package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.EnergyContainerBase;
import owmii.powah.block.thermo.ThermoGenTile;

import javax.annotation.Nullable;

public class ThermoGenContainer extends EnergyContainerBase<ThermoGenTile> {
    public ThermoGenContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
    }

    public ThermoGenContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, ThermoGenTile tile) {
        super(containerType, id, playerInventory, tile);
    }

    public static ThermoGenContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new ThermoGenContainer(IContainers.THERMO_GEN, i, playerInventory, buffer);
    }
}
