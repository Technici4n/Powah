package owmii.powah.compat.curios;

import java.util.stream.Stream;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import owmii.powah.ChargeableItemsEvent;
import top.theillusivec4.curios.api.CuriosCapability;

public class CuriosCompat {
    public static void init() {
        NeoForge.EVENT_BUS.addListener(CuriosCompat::addCurioStacks);
    }

    // I don't really like this, hopefully it doesn't crash...
    public static void addCurioStacks(ChargeableItemsEvent event) {
        var curiosInventory = event.getPlayer().getCapability(CuriosCapability.INVENTORY);
        if (curiosInventory == null) {
            return;
        }

        curiosInventory.getCurios().forEach((_, stackHandler) -> {
            for (int i = 0; i < stackHandler.getSlots(); i++) {
                ItemStack stack = stackHandler.getStacks().getStackInSlot(i);
                if (!stack.isEmpty()) {
                    event.getSources().add(Stream.of(ItemAccess.forStack(stack)));
                }
            }
        });
    }

}
