package owmii.powah.forge.compat.curios;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import owmii.powah.api.forge.ChargeableItemsEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosCompat {
    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(CuriosCompat::addCurioStacks);
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
