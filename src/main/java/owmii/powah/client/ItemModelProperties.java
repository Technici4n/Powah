package owmii.powah.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import owmii.powah.item.Itms;

public class ItemModelProperties {
    public static void register() {
        ItemProperties.register(Itms.BINDING_CARD, new ResourceLocation("bound"), ItemModelProperties::renderBindingCard);
        ItemProperties.register(Itms.BINDING_CARD_DIM, new ResourceLocation("bound"), ItemModelProperties::renderBindingCard);
    }

    static float renderBindingCard(ItemStack stack, ClientLevel world, LivingEntity livingEntity, int var4) {
        float f = 0.0F;
        CompoundTag nbt = stack.getTag();
        if (nbt != null) {
            if (nbt.hasUUID("bound_player_id")) {
                f = 1.0F;
            }
        }
        return f;
    }
}
