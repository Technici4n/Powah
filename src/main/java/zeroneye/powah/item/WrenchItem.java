package zeroneye.powah.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import zeroneye.lib.item.ItemBase;
import zeroneye.lib.util.Stack;
import zeroneye.powah.api.wrench.IWrench;
import zeroneye.powah.api.wrench.IWrenchable;
import zeroneye.powah.api.wrench.WrenchMode;

import javax.annotation.Nullable;
import java.util.List;

public class WrenchItem extends ItemBase implements IWrench {
    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction direction, Vec3d hit) {
        WrenchMode mode = getWrenchMode(stack);
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof IWrenchable) {
            if (((IWrenchable) state.getBlock()).onWrench(state, world, pos, player, hand, direction, mode, hit)) {
                return ActionResultType.SUCCESS;
            }
        } else {
            if (mode.equals(WrenchMode.ROTATE)) {
                BlockState state1 = state.getBlock().rotate(state, world, pos, Rotation.CLOCKWISE_90);
                if (!state.equals(state1)) {
                    world.setBlockState(pos, state1, 3);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return super.onItemUseFirst(stack, world, pos, player, hand, direction, hit);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (playerIn.isSneaking()) {
            nextWrenchMode(stack);
            playerIn.sendStatusMessage(new TranslationTextComponent("info.powah.wrench.mode." + getWrenchMode(stack).name().toLowerCase(), TextFormatting.YELLOW).applyTextStyle(TextFormatting.GRAY), true);
            return ActionResult.newResult(ActionResultType.SUCCESS, stack);
        }
        return ActionResult.newResult(ActionResultType.PASS, stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            CompoundNBT nbt = getWrenchNBT(stack);
            ItemStack mainHand = player.getHeldItemMainhand();
            ItemStack offhand = player.getHeldItemOffhand();
            boolean flag = nbt.getBoolean("ChatInfo");
            if (Stack.orEquals(stack, mainHand, offhand)) {
                if (!flag) {
                    player.sendStatusMessage(new TranslationTextComponent("info.powah.wrench.mode." + getWrenchMode(stack).name().toLowerCase(), TextFormatting.YELLOW).applyTextStyle(TextFormatting.GRAY), true);
                    nbt.putBoolean("ChatInfo", true);
                }
            } else if (flag) {
                nbt.putBoolean("ChatInfo", false);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("info.powah.mode", TextFormatting.DARK_GRAY + I18n.format("info.powah.wrench.mode." + getWrenchMode(stack).name().toLowerCase())).applyTextStyle(TextFormatting.GRAY));
    }
}
