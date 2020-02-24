package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.EnergyContainerBase;
import owmii.lib.inventory.slot.SlotBase;
import owmii.lib.inventory.slot.SlotOverlay;
import owmii.powah.block.energydischarger.EnergyDischargerTile;

import javax.annotation.Nullable;

public class EnergyDischargerContainer extends EnergyContainerBase<EnergyDischargerTile> {
    public EnergyDischargerContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, EnergyDischargerTile te) {
        super(containerType, id, playerInventory, te);
    }

    public EnergyDischargerContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
    }

    public static EnergyDischargerContainer create(final int i, final PlayerInventory playerInventory, final PacketBuffer buffer) {
        return new EnergyDischargerContainer(IContainers.ENERGY_DISCHARGER, i, playerInventory, buffer);
    }

    @Override
    protected void addContainer(PlayerInventory playerInventory, EnergyDischargerTile te) {
        int size = te.getInventory().getSlots();
        for (int i = size - 1; i >= 0; i--) {
            addSlot(new SlotBase(te.getInventory(), i, 79 + size * 20 / 2 - i * 20, 32).bg(SlotOverlay.SLOT).ov(SlotOverlay.DISCHARGING));
        }
        super.addContainer(playerInventory, te);
    }
}
