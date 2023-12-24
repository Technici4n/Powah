package owmii.powah.util;

import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.energy.IEnergyStorage;
import owmii.powah.ChargeableItemsEvent;
import owmii.powah.lib.logistics.inventory.Inventory;

/**
 * Utilities for charging and discharging items.
 */
public final class ChargeUtil {
    private ChargeUtil() {
    }

    // a bit ugly, but I couldn't find a better way
    public static long chargeItemsInPlayerInv(Player player, long maxPerSlot, long maxTotal) {
        return chargeItemsInPlayerInv(player, maxPerSlot, maxTotal, s -> true);
    }

    public static boolean canDischarge(ItemStack stack) {
        var storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return storage != null && storage.canExtract() && storage.getEnergyStored() > 0;
    }

    public static long chargeItemsInPlayerInv(Player player, long maxPerSlot, long maxTotal, Predicate<ItemStack> allowStack) {
        var stacks = new ArrayList<>(owmii.powah.util.Player.invStacks(player).stream().toList());
        var event = new ChargeableItemsEvent(player);
        NeoForge.EVENT_BUS.post(event);
        stacks.addAll(event.getItems());
        stacks.removeIf(allowStack.negate());
        return transferSlotList(IEnergyStorage::receiveEnergy, stacks, maxPerSlot, maxTotal);
    }

    public static long chargeItemsInContainer(Container container, long maxPerSlot, long maxTotal) {
        var ret = transferSlotList(IEnergyStorage::receiveEnergy,
                IntStream.range(0, container.getContainerSize()).mapToObj(container::getItem).toList(), maxPerSlot, maxTotal);
        container.setChanged();
        return ret;
    }

    public static long chargeItemsInInventory(Inventory inv, int slotFrom, int slotTo, long maxPerSlot, long maxTotal) {
        // maybe call setChanged?
        return transferSlotList(IEnergyStorage::receiveEnergy, IntStream.range(slotFrom, slotTo).mapToObj(inv::getStackInSlot).toList(), maxPerSlot,
                maxTotal);
    }

    public static long dischargeItemsInInventory(Inventory inv, long maxPerSlot, long maxTotal) {
        return transferSlotList(IEnergyStorage::extractEnergy, IntStream.range(0, inv.getSlots()).mapToObj(inv::getStackInSlot).toList(), maxPerSlot,
                maxTotal);
    }

    private static long transferSlotList(EnergyTransferOperation op, Iterable<ItemStack> stacks, long maxPerStack, long maxTotal) {
        long charged = 0;
        for (ItemStack stack : stacks) {
            if (stack.isEmpty())
                continue;
            var cap = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (cap != null) {
                charged += op.perform(cap, Ints.saturatedCast(Math.min(maxPerStack, maxTotal - charged)), false);
            }
        }
        return charged;
    }

    interface EnergyTransferOperation {
        int perform(IEnergyStorage storage, int maxAmount, boolean simulate);
    }
}
