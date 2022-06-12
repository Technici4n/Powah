package owmii.powah.lib.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ItemBase extends Item implements IItem {
    public ItemBase(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return context.getPlayer() != null ? onItemUse(context.getLevel(), context.getClickedPos(), context.getPlayer(), context.getHand(), context.getClickedFace(), context.getClickLocation()) : super.useOn(context);
    }

    public InteractionResult onItemUse(Level world, BlockPos pos, Player player, InteractionHand hand, Direction side, Vec3 hit) {
        return InteractionResult.PASS;
    }

    /* TODO ARCH
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return context.getPlayer() != null ? onItemUseFirst(stack, context.getLevel(), context.getClickedPos(), context.getPlayer(), context.getHand(), context.getClickedFace(), context.getClickLocation()) : super.onItemUseFirst(stack, context);
    }
     */

    public InteractionResult onItemUseFirst(ItemStack stack, Level world, BlockPos pos, Player player, InteractionHand hand, Direction side, Vec3 hit) {
        return InteractionResult.PASS;
    }
}
