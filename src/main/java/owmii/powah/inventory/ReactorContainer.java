package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.EnergyContainerBase;
import owmii.lib.inventory.slot.SlotBase;
import owmii.powah.block.reactor.ReactorTile;

import javax.annotation.Nullable;

public class ReactorContainer extends EnergyContainerBase<ReactorTile> {
    public ReactorContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, ReactorTile te) {
        super(containerType, id, playerInventory, te);
    }

    public ReactorContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
    }

    public static ReactorContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new ReactorContainer(IContainers.REACTOR, i, playerInventory, buffer);
    }

    @Override
    protected void addContainer(PlayerInventory playerInventory, ReactorTile te) {
        addSlot(new SlotBase(te.getInventory(), 0, 66, 28));
        addSlot(new SlotBase(te.getInventory(), 1, 23, 5));
        addSlot(new SlotBase(te.getInventory(), 2, 23, 51));
        addSlot(new SlotBase(te.getInventory(), 3, 127, 51));
        super.addContainer(playerInventory, te);
    }
}
