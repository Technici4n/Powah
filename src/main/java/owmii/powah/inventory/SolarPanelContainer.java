package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.EnergyContainerBase;
import owmii.powah.block.solarpanel.SolarPanelTile;

import javax.annotation.Nullable;

public class SolarPanelContainer extends EnergyContainerBase<SolarPanelTile> {
    public SolarPanelContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
    }

    public SolarPanelContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, SolarPanelTile tile) {
        super(containerType, id, playerInventory, tile);
    }

    public static SolarPanelContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new SolarPanelContainer(IContainers.SOLAR_PANEL, i, playerInventory, buffer);
    }
}
