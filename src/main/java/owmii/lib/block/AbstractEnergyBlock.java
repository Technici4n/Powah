package owmii.lib.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import owmii.lib.api.energy.IEnergyConnector;
import owmii.lib.client.util.Text;
import owmii.lib.client.wiki.page.panel.InfoBox;
import owmii.lib.config.IConfigHolder;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.item.IEnergyItemProvider;
import owmii.lib.logistics.Transfer;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.registry.IVariant;
import owmii.lib.util.Util;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractEnergyBlock<V extends Enum<V> & IVariant<V>, C extends IEnergyConfig<V>, B extends AbstractEnergyBlock<V, C, B>> extends AbstractBlock<V, B> implements IConfigHolder<V, C>, InfoBox.IInfoBoxHolder, IEnergyItemProvider {
    public AbstractEnergyBlock(Properties properties) {
        this(properties, IVariant.getEmpty());
    }

    public AbstractEnergyBlock(Properties properties, V variant) {
        super(properties, variant);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return new EnergyBlockItem(this, properties, group);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof AbstractEnergyStorage) {
            return ((AbstractEnergyStorage) tile).getEnergy().toComparatorPower();
        }
        return super.getComparatorInputOverride(state, world, pos);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        if (checkValidEnergySide()) {
            Direction side = state.get(BlockStateProperties.FACING);
            BlockPos pos1 = pos.offset(side);
            return world.getBlockState(pos1).getBlock() instanceof IEnergyConnector
                    || Energy.isPresent(world.getTileEntity(pos1), side);
        }
        return super.isValidPosition(state, world, pos);
    }

    protected boolean checkValidEnergySide() {
        return false;
    }

    @Override
    public boolean isChargeable(ItemStack stack) {
        return getTransferType().canReceive;
    }

    public Transfer getTransferType() {
        return Transfer.ALL;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Energy.ifPresent(stack, storage -> {
            if (storage instanceof Energy.Item) {
                Energy.Item energy = (Energy.Item) storage;
                addEnergyInfo(stack, energy, tooltip);
                addEnergyTransferInfo(stack, energy, tooltip);
                additionalEnergyInfo(stack, energy, tooltip);
                tooltip.add(new StringTextComponent(""));
            }
        });
    }

    public void addEnergyInfo(ItemStack stack, Energy.Item storage, List<ITextComponent> tooltip) {
        if (storage.getCapacity() > 0)
            tooltip.add(new TranslationTextComponent("info.lollipop.stored").mergeStyle(TextFormatting.GRAY).append(Text.COLON).append(new TranslationTextComponent("info.lollipop.fe.stored", Util.addCommas(storage.getStored()), Util.numFormat(storage.getCapacity())).mergeStyle(TextFormatting.DARK_GRAY)));
    }

    public void addEnergyTransferInfo(ItemStack stack, Energy.Item storage, List<ITextComponent> tooltip) {
        long ext = storage.getMaxExtract();
        long re = storage.getMaxReceive();
        if (ext + re > 0) {
            if (ext == re) {
                tooltip.add(new TranslationTextComponent("info.lollipop.max.io").mergeStyle(TextFormatting.GRAY).append(Text.COLON).append(new StringTextComponent(Util.numFormat(ext)).append(new TranslationTextComponent("info.lollipop.fe.pet.tick")).mergeStyle(TextFormatting.DARK_GRAY)));
            } else {
                if (ext > 0)
                    tooltip.add(new TranslationTextComponent("info.lollipop.max.extract").mergeStyle(TextFormatting.GRAY).append(Text.COLON).append(new StringTextComponent(Util.numFormat(ext)).append(new TranslationTextComponent("info.lollipop.fe.pet.tick")).mergeStyle(TextFormatting.DARK_GRAY)));
                if (re > 0)
                    tooltip.add(new TranslationTextComponent("info.lollipop.max.receive").mergeStyle(TextFormatting.GRAY).append(Text.COLON).append(new StringTextComponent(Util.numFormat(re)).append(new TranslationTextComponent("info.lollipop.fe.pet.tick")).mergeStyle(TextFormatting.DARK_GRAY)));
            }
        }
    }

    public void additionalEnergyInfo(ItemStack stack, Energy.Item energy, List<ITextComponent> tooltip) {
    }

    @Override
    public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
        Energy.ifPresent(stack, storage -> {
            if (storage instanceof Energy.Item) {
                Energy.Item energy = (Energy.Item) storage;
                if (storage.getMaxEnergyStored() > 0)
                    box.set(new TranslationTextComponent("info.lollipop.capacity"), new TranslationTextComponent("info.lollipop.fe", Util.addCommas(energy.getCapacity())));
                box.set(new TranslationTextComponent("info.lollipop.max.io"), new TranslationTextComponent("info.lollipop.fe.pet.tick", Util.addCommas(energy.getMaxExtract())));
            }
        });
        return box;
    }
}
