package zeroneye.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import zeroneye.lib.inventory.slot.SlotBase;
import zeroneye.powah.block.generator.panel.solar.SolarPanelTile;

import javax.annotation.Nullable;

public class SolarPanelContainer extends PowahContainer<SolarPanelTile> {
    public SolarPanelContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getInv());
    }

    public SolarPanelContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, SolarPanelTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static SolarPanelContainer createBasic(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new SolarPanelContainer(IContainers.SOLAR_PANEL_BASIC, i, playerInventory, buffer);
    }

    public static SolarPanelContainer createSpirited(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new SolarPanelContainer(IContainers.SOLAR_PANEL_SPIRITED, i, playerInventory, buffer);
    }

    public static SolarPanelContainer createNiotic(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new SolarPanelContainer(IContainers.SOLAR_PANEL_NIOTIC, i, playerInventory, buffer);
    }

    public static SolarPanelContainer createBlazing(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new SolarPanelContainer(IContainers.SOLAR_PANEL_BLAZING, i, playerInventory, buffer);
    }

    public static SolarPanelContainer createHardened(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new SolarPanelContainer(IContainers.SOLAR_PANEL_HARDENED, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, SolarPanelTile tile) {
        addSlot(new SlotBase(tile, 0, 13 + 23, 45));
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
