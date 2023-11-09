package owmii.powah.lib.block;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import owmii.powah.EnvHandler;
import owmii.powah.api.energy.IEnergyConnector;
import owmii.powah.block.Tier;
import owmii.powah.config.IConfigHolder;
import owmii.powah.config.IEnergyConfig;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.client.wiki.page.panel.InfoBox;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.item.IEnergyItemProvider;
import owmii.powah.lib.logistics.Transfer;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.registry.IVariant;
import owmii.powah.lib.util.Util;

public abstract class AbstractEnergyBlock<C extends IEnergyConfig<Tier>, B extends AbstractEnergyBlock<C, B>> extends AbstractBlock<Tier, B>
        implements IConfigHolder<Tier, C>, InfoBox.IInfoBoxHolder, IEnergyItemProvider {
    public AbstractEnergyBlock(Properties properties) {
        this(properties, IVariant.getEmpty());
    }

    public AbstractEnergyBlock(Properties properties, Tier variant) {
        super(properties, variant);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ResourceKey<CreativeModeTab> group) {
        return new EnergyBlockItem(this, properties, group);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof AbstractEnergyStorage) {
            return ((AbstractEnergyStorage) tile).getEnergy().toComparatorPower();
        }
        return super.getAnalogOutputSignal(state, world, pos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (checkValidEnergySide()) {
            Direction side = state.getValue(BlockStateProperties.FACING);
            BlockPos pos1 = pos.relative(side);
            return world.getBlockState(pos1).getBlock() instanceof IEnergyConnector ||
                    world instanceof Level level && EnvHandler.INSTANCE.hasEnergy(level, pos1, side.getOpposite());
        }
        return super.canSurvive(state, world, pos);
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
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Energy.ifPresent(stack, energy -> {
            addEnergyInfo(stack, energy, tooltip);
            addEnergyTransferInfo(stack, energy, tooltip);
            additionalEnergyInfo(stack, energy, tooltip);
        });
    }

    public void addEnergyInfo(ItemStack stack, Energy.Item storage, List<Component> tooltip) {
        if (storage.getCapacity() > 0)
            tooltip.add(Component.translatable("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                    .append(Component
                            .translatable("info.lollipop.fe.stored", Util.addCommas(storage.getStored()), Util.numFormat(storage.getCapacity()))
                            .withStyle(ChatFormatting.DARK_GRAY)));
    }

    public void addEnergyTransferInfo(ItemStack stack, Energy.Item storage, List<Component> tooltip) {
        long ext = storage.getMaxExtract();
        long re = storage.getMaxReceive();
        if (ext + re > 0) {
            if (ext == re) {
                tooltip.add(Component.translatable("info.lollipop.max.io").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                        .append(Component.translatable("info.lollipop.fe.pet.tick", Util.numFormat(ext))
                                .withStyle(ChatFormatting.DARK_GRAY)));
            } else {
                if (ext > 0)
                    tooltip.add(Component.translatable("info.lollipop.max.extract").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                            .append(Component.translatable("info.lollipop.fe.pet.tick", Util.numFormat(ext))
                                    .withStyle(ChatFormatting.DARK_GRAY)));
                if (re > 0)
                    tooltip.add(Component.translatable("info.lollipop.max.receive").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                            .append(Component.translatable("info.lollipop.fe.pet.tick", Util.numFormat(re))
                                    .withStyle(ChatFormatting.DARK_GRAY)));
            }
        }
    }

    public void additionalEnergyInfo(ItemStack stack, Energy.Item energy, List<Component> tooltip) {
    }

    @Override
    public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
        Energy.ifPresent(stack, storage -> {
            if (storage != null) {
                if (storage.getMaxEnergyStored() > 0)
                    box.set(Component.translatable("info.lollipop.capacity"),
                            Component.translatable("info.lollipop.fe", Util.addCommas(storage.getCapacity())));
                box.set(Component.translatable("info.lollipop.max.io"),
                        Component.translatable("info.lollipop.fe.pet.tick", Util.addCommas(storage.getMaxExtract())));
            }
        });
        return box;
    }
}
