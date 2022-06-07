package owmii.powah.lib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import owmii.powah.lib.config.IEnergyConfig;
import owmii.powah.lib.logistics.Transfer;
import owmii.powah.lib.registry.IVariant;

public class AbstractEnergyProvider<V extends Enum<V> & IVariant<V>, C extends IEnergyConfig<V>, B extends AbstractEnergyBlock<V, C, B>> extends AbstractEnergyStorage<V, C, B> {
    public AbstractEnergyProvider(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public AbstractEnergyProvider(BlockEntityType<?> type, BlockPos pos, BlockState state, V variant) {
        super(type, pos, state, variant);
    }

    public long getGeneration() {
        return getBlock().getConfig().getGeneration(getVariant());
    }

    @Override
    public Transfer getTransferType() {
        return Transfer.EXTRACT;
    }
}
