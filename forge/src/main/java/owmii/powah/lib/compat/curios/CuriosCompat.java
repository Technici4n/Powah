package owmii.powah.lib.compat.curios;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;

public class CuriosCompat {
    public static final String ID = "curios";
    private static int loaded;

    public static boolean isLoaded() {
        if (loaded == 0) loaded = ModList.get().isLoaded(ID) ? 1 : -1;
        return loaded == 1;
    }

    public static List<ItemStack> getAllStacks(Player player) {
        final List<ItemStack> list = new ArrayList<>();
        if (isLoaded()) {
            CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(itemHandler -> {
                itemHandler.getCurios().forEach((s, stackHandler) -> {
                    for (int i = 0; i < stackHandler.getSlots(); i++) {
                        ItemStack stack = stackHandler.getStacks().getStackInSlot(i);
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
