package owmii.powah.client;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import owmii.powah.item.Itms;

public class ItemModelProperties {
    public static void register() {
        ItemModelsProperties.func_239418_a_(Itms.BINDING_CARD, new ResourceLocation("bound"), ItemModelProperties::renderBindingCard);
        ItemModelsProperties.func_239418_a_(Itms.BINDING_CARD_DIM, new ResourceLocation("bound"), ItemModelProperties::renderBindingCard);
    }

    static float renderBindingCard(ItemStack stack, ClientWorld world, LivingEntity livingEntity) {
        float f = 0.0F;
        CompoundNBT nbt = stack.getTag();
        if (nbt != null) {
            if (nbt.hasUniqueId("bound_player_id")) {
                f = 1.0F;
            }
        }
        return f;
    }
}
