package zeroneye.powah.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import zeroneye.lib.item.ItemBase;
import zeroneye.powah.api.wrench.IWrench;
import zeroneye.powah.api.wrench.IWrenchable;

import javax.annotation.Nullable;
import java.util.List;

public class WrenchItem extends ItemBase implements IWrench {
    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction direction, Vec3d hit) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof IWrenchable) {
            if (((IWrenchable) state.getBlock()).onWrench(state, world, pos, player, hand, direction, getWrenchMode(stack), hit)) {
                return ActionResultType.SUCCESS;
            }
        }
        return super.onItemUseFirst(stack, world, pos, player, hand, direction, hit);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
//        if (playerIn.isSneaking()) { TODO
//            nextWrenchMode(stack);
//            return ActionResult.newResult(ActionResultType.SUCCESS, stack);
//        }
        return ActionResult.newResult(ActionResultType.PASS, stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        //tooltip.add(new TranslationTextComponent("info.powah.wrench.mode." + getWrenchMode(stack).name().toLowerCase()));
    }
}
