package owmii.powah.config;

import owmii.powah.lib.registry.IVariant;

public interface IConfigHolder<V extends Enum<V> & IVariant<V>, C> {
    C getConfig();
}
