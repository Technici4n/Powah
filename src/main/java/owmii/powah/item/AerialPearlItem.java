package owmii.powah.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import owmii.lib.item.ItemBase;
import owmii.powah.config.Configs;

public class AerialPearlItem extends ItemBase {
    public AerialPearlItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        if (Configs.GENERAL.player_aerial_pearl.get()) {
            if (this == Itms.AERIAL_PEARL) {
                if (target.getClass() == Zombie.class
                        || target.getClass() == ZombieVillager.class
                        || target.getClass() == Husk.class) {
                    if (!playerIn.level.isClientSide) {
                        ItemStack stack1 = playerIn.getItemInHand(hand);
                        ItemHandlerHelper.giveItemToPlayer(playerIn, new ItemStack(Itms.PLAYER_AERIAL_PEARL));
                        target.playSound(SoundEvents.ZOMBIE_DEATH, 0.5F, 1.0F);
                        target.remove(Entity.RemovalReason.KILLED);
                        if (!playerIn.isCreative()) {
                            stack1.shrink(1);
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.interactLivingEntity(stack, playerIn, target, hand);
    }
}
