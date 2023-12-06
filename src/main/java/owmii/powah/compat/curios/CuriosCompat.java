package owmii.powah.compat.curios;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import owmii.powah.ChargeableItemsEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosCompat {
    public static void init() {
        NeoForge.EVENT_BUS.addListener(CuriosCompat::addCurioStacks);
    }

    // I don't really like this, hopefully it doesn't crash...
    public static void addCurioStacks(ChargeableItemsEvent event) {
        CuriosApi.getCuriosHelper().getCuriosHandler(event.getPlayer()).ifPresent(itemHandler -> {
            itemHandler.getCurios().forEach((s, stackHandler) -> {
                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stack = stackHandler.getStacks().getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        event.getItems().add(stack);
                    }
                }
            });
        });
    }
}
