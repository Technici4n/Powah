package owmii.powah.lib.item;

import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import owmii.powah.lib.block.AbstractBlock;
import owmii.powah.lib.block.IBlock;
import owmii.powah.lib.data.ItemModelType;
import owmii.powah.lib.registry.IVariant;
import owmii.powah.lib.registry.IVariantEntry;

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
