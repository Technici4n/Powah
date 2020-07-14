package owmii.powah.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.items.ItemHandlerHelper;
import owmii.lib.item.ItemBase;
import owmii.powah.config.Configs;

public class PhotoelectricPaneItem extends ItemBase {
    public PhotoelectricPaneItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (Configs.GENERAL.lens_of_ender.get()) {
            if (target.getClass() == EndermanEntity.class || target.getClass() == EndermiteEntity.class) {
                if (!playerIn.world.isRemote) {
                    ItemStack stack1 = playerIn.getHeldItem(hand);
                    if (!playerIn.isCreative()) {
                        stack1.shrink(1);
                    }
                    ItemHandlerHelper.giveItemToPlayer(playerIn, new ItemStack(IItems.LENS_OF_ENDER));
                    target.playSound(SoundEvents.ENTITY_ENDERMAN_DEATH, 0.5F, 1.0F);
                    target.remove();
                }
                return ActionResultType.SUCCESS;
            }
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
}
