package owmii.powah.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import owmii.powah.block.solar.SolarTile;
import owmii.powah.lib.item.ItemBase;

public class LensOfEnderItem extends ItemBase {
    public LensOfEnderItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, Level world, BlockPos pos, Player player, InteractionHand hand, Direction side,
            Vec3 hit) {
        if (!world.isClientSide) {
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof SolarTile solar) {
                if (!solar.hasLensOfEnder()) {
                    solar.setHasLensOfEnder(true);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.onItemUseFirst(stack, world, pos, player, hand, side, hit);
    }
}
