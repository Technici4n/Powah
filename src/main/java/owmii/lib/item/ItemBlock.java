package owmii.lib.item;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import owmii.lib.block.AbstractBlock;
import owmii.lib.block.IBlock;
import owmii.lib.data.ItemModelType;
import owmii.lib.registry.IVariant;
import owmii.lib.registry.IVariantEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ItemBlock<V extends IVariant, B extends Block & IBlock<V, B>> extends BlockItem implements IItem, IVariantEntry<V, B> {
    private final B block;

    @SuppressWarnings("ConstantConditions")
    public ItemBlock(B block, Properties builder, @Nullable CreativeModeTab group) {
        super(block, builder.tab(group));
        this.block = block;
    }

    @Override
    public Component getName(ItemStack stack) {
        if (this.block instanceof AbstractBlock) {
            return ((AbstractBlock) this.block).getDisplayName(stack);
        }
        return super.getName(stack);
    }

    @Override
    public B getBlock() {
        return this.block;
    }

    @Override
    public ItemModelType getItemModelType() {
        return ItemModelType.BLOCK;
    }

    @Override
    public V getVariant() {
        return getBlock().getVariant();
    }
}
