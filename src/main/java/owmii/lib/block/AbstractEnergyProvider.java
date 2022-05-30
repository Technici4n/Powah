package owmii.lib.block;

import net.minecraft.tileentity.TileEntityType;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.logistics.Transfer;
import owmii.lib.registry.IVariant;

public class AbstractEnergyProvider<V extends Enum<V> & IVariant<V>, C extends IEnergyConfig<V>, B extends AbstractEnergyBlock<V, C, B>> extends AbstractEnergyStorage<V, C, B> {
    public AbstractEnergyProvider(TileEntityType<?> type) {
        super(type);
    }

    public AbstractEnergyProvider(TileEntityType<?> type, V variant) {
        super(type, variant);
    }

    public long getGeneration() {
        return getBlock().getConfig().getGeneration(getVariant());
    }

    @Override
    public Transfer getTransferType() {
        return Transfer.EXTRACT;
    }
}
