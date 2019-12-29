package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.slot.SlotBase;
import owmii.powah.block.generator.panel.solar.SolarPanelTile;

import javax.annotation.Nullable;

public class SolarPanelContainer extends PowahContainer<SolarPanelTile> {
    public SolarPanelContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getTile());
    }

    public SolarPanelContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, SolarPanelTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static SolarPanelContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new SolarPanelContainer(IContainers.SOLAR_PANEL, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, SolarPanelTile tile) {
        addSlot(new SlotBase(tile.getInventory(), 0, 13 + 23, 45));
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
