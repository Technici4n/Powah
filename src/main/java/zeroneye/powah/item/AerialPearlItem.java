package zeroneye.powah.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.items.ItemHandlerHelper;
import zeroneye.lib.item.ItemBase;
import zeroneye.powah.config.Config;

public class AerialPearlItem extends ItemBase {
    public AerialPearlItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (Config.GENERAL.player_aerial_pearl.get()) {
            if (this == IItems.AERIAL_PEARL) {
                if (target.getClass() == ZombieEntity.class || target.getClass() == HuskEntity.class) {
                    if (!playerIn.world.isRemote) {
                        ItemStack stack1 = playerIn.getHeldItem(hand);
                        ItemHandlerHelper.giveItemToPlayer(playerIn, new ItemStack(IItems.PLAYER_AERIAL_PEARL));
                        target.playSound(SoundEvents.ENTITY_ZOMBIE_DEATH, 0.5F, 1.0F);
                        target.remove();
                        if (!playerIn.isCreative()) {
                            stack1.shrink(1);
                        }
                    }
                    return true;
                }
            }
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
}
