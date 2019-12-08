package zeroneye.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import zeroneye.powah.block.hopper.EnergyHopperTile;

import javax.annotation.Nullable;

public class EnergyHopperContainer extends PowahContainer<EnergyHopperTile> {
    public EnergyHopperContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getInv());
    }

    public EnergyHopperContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, EnergyHopperTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static EnergyHopperContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnergyHopperContainer(IContainers.ENERGY_HOPPER, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, EnergyHopperTile tile) {
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
