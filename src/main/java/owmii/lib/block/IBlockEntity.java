package owmii.lib.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IBlockEntity {
    default void onPlaced(World world, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
    }

    default void onAdded(World world, BlockState state, BlockState oldState, boolean isMoving) {
    }

    default void onRemoved(World world, BlockState state, BlockState newState, boolean isMoving) {
    }
}
