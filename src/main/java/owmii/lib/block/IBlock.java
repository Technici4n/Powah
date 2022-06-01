package owmii.lib.block;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeBlock;
import owmii.lib.item.ItemBlock;
import owmii.lib.registry.IVariant;
import owmii.lib.registry.IVariantEntry;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;

public interface IBlock<V extends IVariant, B extends Block & IBlock<V, B>> extends IForgeBlock, IVariantEntry<V, B>, EntityBlock {
    @SuppressWarnings("unchecked")
    default ItemBlock getBlockItem(Item.Properties properties, @Nullable CreativeModeTab group) {
        return new ItemBlock((Block) this, properties, group);
    }

    @Nullable
    @Override
    default BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Nullable
    @Override
    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (newBlockEntity(BlockPos.ZERO, state) != null) {
            return (l, p, s, be) -> ((AbstractTickableTile<?, ?>) be).tick();
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    default void renderByItem(ItemStack stack, PoseStack matrix, MultiBufferSource rtb, int light, int ov) {
    }
}
