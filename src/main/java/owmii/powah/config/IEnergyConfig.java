package owmii.powah.config;

import owmii.powah.lib.registry.IVariant;

public interface IEnergyConfig<V extends Enum<V> & IVariant<V>> {
    long getCapacity(V variant);

    long getTransfer(V variant);
}
