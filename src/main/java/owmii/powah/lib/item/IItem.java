package owmii.powah.lib.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import owmii.powah.lib.data.ItemModelType;

public interface IItem {
    default ItemModelType getItemModelType() {
        return this instanceof DiggerItem ? ItemModelType.HANDHELD : ItemModelType.GENERATED;
    }

    default void oneTimeInfo(Player player, ItemStack stack, Component component) {
        /*
         * TODO ARCH - unclear
         * CompoundTag p = player.getPersistentData();
         * int i = player.getInventory().selected;
         * int j = p.getInt("ChatInfo");
         * if (i != j && p.contains("ChatInfo")) {
         * p.remove("ChatInfo");
         * }
         * if (!stack.equals(player.getInventory().items.get(i), true))
         * return;
         * if (i != j || i == 0 && !p.contains("ChatInfo")) {
         * player.displayClientMessage(component, true);
         * p.putInt("ChatInfo", i);
         * }
         */
    }
}
