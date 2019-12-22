package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import owmii.lib.inventory.slot.SlotBase;
import owmii.powah.block.generator.thermoelectric.ThermoGeneratorTile;

import javax.annotation.Nullable;

public class ThermoGenContainer extends PowahContainer<ThermoGeneratorTile> {
    public ThermoGenContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getTile());
    }

    public ThermoGenContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, ThermoGeneratorTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static ThermoGenContainer createBasic(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new ThermoGenContainer(IContainers.MAGMATIC_GENERATOR_BASIC, i, playerInventory, buffer);
    }

    public static ThermoGenContainer createSpirited(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new ThermoGenContainer(IContainers.MAGMATIC_GENERATOR_SPIRITED, i, playerInventory, buffer);
    }

    public static ThermoGenContainer createNiotic(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new ThermoGenContainer(IContainers.MAGMATIC_GENERATOR_NIOTIC, i, playerInventory, buffer);
    }

    public static ThermoGenContainer createBlazing(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new ThermoGenContainer(IContainers.MAGMATIC_GENERATOR_BLAZING, i, playerInventory, buffer);
    }

    public static ThermoGenContainer createHardened(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new ThermoGenContainer(IContainers.MAGMATIC_GENERATOR_HARDENED, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, ThermoGeneratorTile tile) {
        addSlot(new SlotBase(tile.getInventory(), 0, 36, 45));
        addPlayerInv(playerInventory, 8, 146, 88);
    }
}
