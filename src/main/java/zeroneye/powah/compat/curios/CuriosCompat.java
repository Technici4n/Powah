package zeroneye.powah.compat.curios;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosAPI;

import java.util.ArrayList;
import java.util.List;

public class CuriosCompat {
    public static final String ID = "curios";
    private static int LOADED;

    public static boolean isLoaded() {
        if (LOADED == 0) LOADED = ModList.get().isLoaded(ID) ? 1 : -1;
        return LOADED == 1;
    }

    public static List<ItemStack> getAllStacks(PlayerEntity player) {
        final List<ItemStack> list = new ArrayList<>();
        if (isLoaded()) {
            CuriosAPI.getCuriosHandler(player).ifPresent(itemHandler -> {
                itemHandler.getCurioMap().forEach((s, stackHandler) -> {
                    for (int i = 0; i < stackHandler.getSlots(); i++) {
                        ItemStack stack = stackHandler.getStackInSlot(i);
                        if (!stack.isEmpty()) {
                            list.add(stack);
                        }
                    }
                });
            });
        }
        return list;
    }
}
