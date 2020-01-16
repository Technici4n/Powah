package owmii.powah.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import owmii.lib.block.BlockBase;
import owmii.lib.item.BlockItemBase;
import owmii.lib.util.Energy;
import owmii.lib.util.Text;
import owmii.powah.block.generator.GeneratorBlock;
import owmii.powah.energy.ItemPowahStorage;
import owmii.powah.item.PowahBlockItem;

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
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof PowahTile) {
            PowahTile powahTile = (PowahTile) tileEntity;
            powahTile.sideConfig.init();
        }
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
    }

    @Override
    public void tooltip(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Energy.getForgeEnergy(stack).ifPresent(storage -> {
            if (storage instanceof ItemPowahStorage && !isCreative()) {
                if (storage.getMaxEnergyStored() > 0) {
                    tooltip.add(new TranslationTextComponent("info.powah.stored", TextFormatting.DARK_GRAY + Text.addCommas(storage.getEnergyStored()), Text.numFormat(storage.getMaxEnergyStored())).applyTextStyle(TextFormatting.GRAY));
                }
                if (getBlock() instanceof GeneratorBlock) {
                    tooltip.add(new TranslationTextComponent("info.powah.generates", TextFormatting.DARK_GRAY + Text.numFormat(((GeneratorBlock) getBlock()).perTick())).applyTextStyle(TextFormatting.GRAY));
                }
                int maxIn = getMaxReceive();
                int maxOut = getMaxExtract();
                tooltip.add(new TranslationTextComponent("info.powah.max.io", TextFormatting.DARK_GRAY + (maxIn == maxOut ? Text.numFormat(maxOut) : maxIn == 0 || maxOut == 0 ? Text.numFormat(Math.max(maxIn, maxOut)) : (Text.numFormat(maxIn) + "/" + Text.numFormat(maxOut)))).applyTextStyle(TextFormatting.GRAY));
                tooltip.add(new StringTextComponent(""));
            }
        });
    }

    @Override
    public int stackSize() {
        return 1;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getMaxExtract() {
        return this.maxExtract;
    }

    public int getMaxReceive() {
        return this.maxReceive;
    }

    public boolean isCreative() {
        return this.isCreative;
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
