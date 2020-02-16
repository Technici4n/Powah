package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.EnergyContainerBase;
import owmii.lib.inventory.slot.SlotBase;
import owmii.powah.block.playertransmitter.PlayerTransmitterTile;

import javax.annotation.Nullable;

public class PlayerTransmitterContainer extends EnergyContainerBase<PlayerTransmitterTile> {
    public PlayerTransmitterContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PlayerTransmitterTile te) {
        super(containerType, id, playerInventory, te);
    }

    public PlayerTransmitterContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
    }

    public static PlayerTransmitterContainer create(final int i, final PlayerInventory playerInventory, final PacketBuffer buffer) {
        return new PlayerTransmitterContainer(IContainers.ENERGY_CELL, i, playerInventory, buffer);
    }

    @Override
    protected void addContainer(PlayerInventory playerInventory, PlayerTransmitterTile te) {
        addSlot(new SlotBase(te.getInventory(), 0, 85, 32).bg(0, 0).ov(0, 18));
        super.addContainer(playerInventory, te);
    }
}
