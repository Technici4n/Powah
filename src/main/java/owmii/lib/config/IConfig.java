package owmii.lib.config;

import owmii.lib.registry.IVariant;

public interface IConfig<V extends Enum<V> & IVariant<V>> {
    void reload();
}
