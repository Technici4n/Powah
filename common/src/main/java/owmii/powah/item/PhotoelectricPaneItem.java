package owmii.powah.item;

import dev.architectury.hooks.item.ItemStackHooks;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import owmii.powah.Powah;
import owmii.powah.lib.item.ItemBase;

public class PhotoelectricPaneItem extends ItemBase {
    public PhotoelectricPaneItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        if (Powah.config().general.lens_of_ender) {
            if (target.getClass() == EnderMan.class || target.getClass() == Endermite.class) {
                if (!playerIn.level.isClientSide) {
                    ItemStack stack1 = playerIn.getItemInHand(hand);
                    if (!playerIn.isCreative()) {
                        stack1.shrink(1);
                    }
                    ItemStackHooks.giveItem((ServerPlayer) playerIn, new ItemStack(Itms.LENS_OF_ENDER.get()));
                    target.playSound(SoundEvents.ENDERMAN_DEATH, 0.5F, 1.0F);
                    target.remove(Entity.RemovalReason.KILLED);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.interactLivingEntity(stack, playerIn, target, hand);
    }
}
