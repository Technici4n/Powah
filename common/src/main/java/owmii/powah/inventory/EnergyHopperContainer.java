package owmii.powah.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.powah.block.hopper.EnergyHopperTile;

public class EnergyHopperContainer extends AbstractEnergyContainer<EnergyHopperTile> {
    public EnergyHopperContainer(int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(Containers.ENERGY_HOPPER.get(), id, inventory, buffer);
    }

    public EnergyHopperContainer(int id, Inventory inventory, EnergyHopperTile te) {
        super(Containers.ENERGY_HOPPER.get(), id, inventory, te);
    }

    public static EnergyHopperContainer create(int id, Inventory inventory, FriendlyByteBuf buffer) {
        return new EnergyHopperContainer(id, inventory, buffer);
    }

    @Override
    protected void init(Inventory inventory, EnergyHopperTile te) {
        super.init(inventory);
        addPlayerInventory(inventory, 8, 59, 4);
    }
}
