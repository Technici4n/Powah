package owmii.lib.item;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.block.AbstractBlock;
import owmii.lib.block.IBlock;
import owmii.lib.client.renderer.item.TEItemRenderer;
import owmii.lib.data.ItemModelType;
import owmii.lib.registry.IRegistryObject;
import owmii.lib.registry.IVariant;
import owmii.lib.registry.IVariantEntry;
import owmii.lib.registry.Registry;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlock<V extends IVariant, B extends Block & IBlock<V, B>> extends BlockItem implements IItem, IRegistryObject<Block>, IVariantEntry<V, B> {
    private final B block;

    @SuppressWarnings("ConstantConditions")
    public ItemBlock(B block, Properties builder, @Nullable ItemGroup group) {
        super(block, builder.group(group).setISTER(() -> TEItemRenderer::new));
        this.block = block;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (this.block instanceof AbstractBlock) {
            return ((AbstractBlock) this.block).getDisplayName(stack);
        }
        return super.getDisplayName(stack);
    }

    @Override
    public B getBlock() {
        return this.block;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderByItem(ItemStack stack, MatrixStack matrix, IRenderTypeBuffer rtb, int light, int ov) {
        getBlock().renderByItem(stack, matrix, rtb, light, ov);
    }

    @Override
    public ItemModelType getItemModelType() {
        return ItemModelType.BLOCK;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Block> getSiblings() {
        if (getBlock() instanceof IRegistryObject) {
            return ((IRegistryObject) getBlock()).getSiblings();
        } else return Lists.newArrayList(getBlock());
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public Registry<Block> getRegistry() {
        return null;
    }

    @Override
    public void setRegistry(Registry<Block> registry) {
    }

    @Override
    public V getVariant() {
        return getBlock().getVariant();
    }
}
