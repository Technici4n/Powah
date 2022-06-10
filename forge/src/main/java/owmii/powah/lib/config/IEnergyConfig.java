package owmii.powah.lib.config;

import owmii.powah.lib.registry.IVariant;

public interface IEnergyConfig<V extends Enum<V> & IVariant<V>> extends IConfig<V> {
    long getCapacity(V variant);

    long getTransfer(V variant);

    default long getGeneration(V variant) {
        return 0;
    }
}
