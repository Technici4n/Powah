package owmii.powah.lib.config;

import owmii.powah.lib.registry.IVariant;

public interface IConfig<V extends Enum<V> & IVariant<V>> {
    void reload();
}
