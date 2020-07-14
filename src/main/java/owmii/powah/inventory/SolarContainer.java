package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.powah.block.solar.SolarTile;

public class SolarContainer extends AbstractEnergyContainer<SolarTile> {
    public SolarContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        super(Containers.SOLAR, id, inventory, buffer);
    }

    public SolarContainer(int id, PlayerInventory inventory, SolarTile te) {
        super(Containers.SOLAR, id, inventory, te);
    }

    public static SolarContainer create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new SolarContainer(id, inventory, buffer);
    }

    @Override
    protected void init(PlayerInventory inventory, SolarTile te) {
        super.init(inventory);
        addPlayerInventory(inventory, 8, 59, 4);
    }
}
