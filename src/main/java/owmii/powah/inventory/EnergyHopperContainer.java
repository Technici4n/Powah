package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.powah.block.hopper.EnergyHopperTile;

public class EnergyHopperContainer extends AbstractEnergyContainer<EnergyHopperTile> {
    public EnergyHopperContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(Containers.ENERGY_HOPPER, id, inventory, buffer);
    }

    public EnergyHopperContainer(int id, PlayerInventory inventory, EnergyHopperTile te) {
        super(Containers.ENERGY_HOPPER, id, inventory, te);
    }

    public static EnergyHopperContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new EnergyHopperContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, EnergyHopperTile te) {
        super.init(inventory);
        addPlayerInventory(inventory, 8, 59, 4);
    }
}
