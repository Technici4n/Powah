package zeroneye.powah.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import zeroneye.lib.item.ItemBase;
import zeroneye.powah.config.Config;

public class CapacitorItem extends ItemBase {
    public CapacitorItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (Config.GENERAL.capacitor_blazing.get()) {
            if (this == IItems.CAPACITOR_BASIC_LARGE) {
                if (target.getClass() == BlazeEntity.class) {
                    if (!playerIn.world.isRemote) {
                        ItemStack stack1 = playerIn.getHeldItem(hand);
                        ItemHandlerHelper.giveItemToPlayer(playerIn, new ItemStack(IItems.CAPACITOR_BLAZING));
                        target.playSound(SoundEvents.ENTITY_BLAZE_DEATH, 0.5F, 1.0F);
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

    @Override
    public ActionResultType onItemUse(World world, BlockPos pos, PlayerEntity player, Hand hand, Direction direction) {
        if (this == IItems.CAPACITOR_BLAZING) {
            if (Config.GENERAL.capacitor_niotic.get()) {
                ItemStack stack = player.getHeldItem(hand);
                BlockState state = world.getBlockState(pos);
                if (state.getBlock() == Blocks.DIAMOND_ORE) {
                    if (!world.isRemote) {
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(IItems.CAPACITOR_NIOTIC));
                        world.destroyBlock(pos, false);
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        } else if (this == IItems.CAPACITOR_NIOTIC) {
            if (Config.GENERAL.capacitor_spirited.get()) {
                ItemStack stack = player.getHeldItem(hand);
                BlockState state = world.getBlockState(pos);
                if (state.getBlock() == Blocks.EMERALD_ORE) {
                    if (!world.isRemote) {
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(IItems.CAPACITOR_SPIRITED));
                        world.destroyBlock(pos, false);
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return super.onItemUse(world, pos, player, hand, direction);
    }
}
