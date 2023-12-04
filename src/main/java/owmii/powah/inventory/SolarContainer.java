package owmii.powah.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.block.solar.SolarTile;
import owmii.powah.lib.logistics.inventory.AbstractEnergyContainer;

public class SolarContainer extends AbstractEnergyContainer<SolarTile> {
    public SolarContainer(int id, Inventory inventory, FriendlyByteBuf buffer) {
        super(Containers.SOLAR.get(), id, inventory, buffer);
    }

    public SolarContainer(int id, Inventory inventory, SolarTile te) {
        super(Containers.SOLAR.get(), id, inventory, te);
    }

    public static SolarContainer create(int id, Inventory inventory, FriendlyByteBuf buffer) {
        return new SolarContainer(id, inventory, buffer);
    }

    @Override
    protected void init(Inventory inventory, SolarTile te) {
        super.init(inventory);
        addPlayerInventory(inventory, 8, 59, 4);
    }
}
