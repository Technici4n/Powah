package owmii.lib.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeItem;
import owmii.lib.data.ItemModelType;

public interface IItem extends IForgeItem {
    @OnlyIn(Dist.CLIENT)
    default void renderByItem(ItemStack stack, PoseStack matrix, MultiBufferSource rtb, int light, int ov) {
    }

    default ItemModelType getItemModelType() {
        return this instanceof DiggerItem ? ItemModelType.HANDHELD : ItemModelType.GENERATED;
    }

    default void oneTimeInfo(Player player, ItemStack stack, Component component) {
        CompoundTag p = player.getPersistentData();
        int i = player.getInventory().selected;
        int j = p.getInt("ChatInfo");
        if (i != j && p.contains("ChatInfo")) {
            p.remove("ChatInfo");
        }
        if (!stack.equals(player.getInventory().items.get(i), true))
            return;
        if (i != j || i == 0 && !p.contains("ChatInfo")) {
            player.displayClientMessage(component, true);
            p.putInt("ChatInfo", i);
        }
    }
}
