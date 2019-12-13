package zeroneye.powah.block;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import zeroneye.lib.block.BlockBase;
import zeroneye.lib.item.BlockItemBase;
import zeroneye.lib.util.Energy;
import zeroneye.powah.block.generator.GeneratorBlock;
import zeroneye.powah.energy.ItemPowahStorage;
import zeroneye.powah.item.PowahBlockItem;

import javax.annotation.Nullable;
import java.util.List;

public abstract class PowahBlock extends BlockBase {
    protected int capacity;
    protected int maxExtract;
    protected int maxReceive;
    protected boolean isCreative;

    public PowahBlock(Properties properties, int capacity, int maxExtract, int maxReceive) {
        super(properties);
        this.capacity = capacity;
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
    }

    @Override
    public BlockItemBase getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return new PowahBlockItem(this, properties, group);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Energy.getForgeEnergy(stack).ifPresent(storage -> {
            if (storage instanceof ItemPowahStorage && !isCreative()) {
                if (storage.getMaxEnergyStored() > 0) {
                    tooltip.add(new TranslationTextComponent("info.powah.stored", "" + TextFormatting.DARK_GRAY + storage.getEnergyStored(), storage.getMaxEnergyStored()).applyTextStyle(TextFormatting.GRAY));
                }
                if (getBlock() instanceof GeneratorBlock) {
                    tooltip.add(new TranslationTextComponent("info.powah.generates", "" + TextFormatting.DARK_GRAY + ((GeneratorBlock) getBlock()).perTick()).applyTextStyle(TextFormatting.GRAY));
                }
                int maxIn = getMaxReceive();
                int maxOut = getMaxExtract();
                tooltip.add(new TranslationTextComponent("info.powah.max.io", "" + TextFormatting.DARK_GRAY + (maxIn == maxOut ? maxOut : maxIn == 0 || maxOut == 0 ? Math.max(maxIn, maxOut) : (maxIn + "/" + maxOut))).applyTextStyle(TextFormatting.GRAY));
                additionalTooltip(stack, worldIn, tooltip, flagIn);
                tooltip.add(new StringTextComponent(""));
            }
        });
    }

    protected void additionalTooltip(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

    }

    @Override
    public int stackSize() {
        return 1;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getMaxExtract() {
        return maxExtract;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public boolean isCreative() {
        return isCreative;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    public void setTransfer(int transfer) {
        this.maxReceive = transfer;
        this.maxExtract = transfer;
    }

    @SuppressWarnings("unchecked")
    public <T extends PowahBlock> T setCreative(boolean isCreative) {
        this.isCreative = isCreative;
        return (T) this;
    }
}
