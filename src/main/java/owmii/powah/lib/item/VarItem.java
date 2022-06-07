package owmii.powah.lib.item;

import owmii.powah.lib.registry.IVariant;
import owmii.powah.lib.registry.IVariantEntry;

public abstract class VarItem<V extends Enum<V> & IVariant<V>, I extends VarItem<V, I>> extends ItemBase implements IVariantEntry<V, I> {
    private final V variant;

    public VarItem(Properties properties, V variant) {
        super(properties);
        this.variant = variant;
    }

    public VarItem(Properties properties) {
        this(properties, IVariant.getEmpty());
    }

    public V getVariant() {
        return this.variant;
    }
}
