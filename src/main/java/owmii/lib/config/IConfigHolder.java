package owmii.lib.config;

import owmii.lib.registry.IVariant;

public interface IConfigHolder<V extends Enum<V> & IVariant<V>, C extends IConfig<V>> {
    C getConfig();
}
