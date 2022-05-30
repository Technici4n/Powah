package owmii.lib.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import owmii.lib.client.renderer.item.TEItemRenderer;

public class ItemBase extends Item implements IItem {
    public ItemBase(Properties properties) {
        super(properties.setISTER(() -> TEItemRenderer::new));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return context.getPlayer() != null ? onItemUse(context.getWorld(), context.getPos(), context.getPlayer(), context.getHand(), context.getFace(), context.getHitVec()) : super.onItemUse(context);
    }

    public ActionResultType onItemUse(World world, BlockPos pos, PlayerEntity player, Hand hand, Direction side, Vector3d hit) {
        return ActionResultType.PASS;
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        return context.getPlayer() != null ? onItemUseFirst(stack, context.getWorld(), context.getPos(), context.getPlayer(), context.getHand(), context.getFace(), context.getHitVec()) : super.onItemUseFirst(stack, context);
    }

    public ActionResultType onItemUseFirst(ItemStack stack, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction side, Vector3d hit) {
        return ActionResultType.PASS;
    }
}
