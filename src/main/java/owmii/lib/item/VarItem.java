package owmii.lib.item;

import net.minecraft.item.Item;
import owmii.lib.registry.IRegistryObject;
import owmii.lib.registry.IVariant;
import owmii.lib.registry.IVariantEntry;
import owmii.lib.registry.Registry;

import java.util.List;

public abstract class VarItem<V extends Enum<V> & IVariant<V>, I extends VarItem<V, I>> extends ItemBase implements IRegistryObject<Item>, IVariantEntry<V, I> {
    private final V variant;

    @SuppressWarnings("NullableProblems")
    private Registry<Item> registry;

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

    @Override
    @SuppressWarnings("unchecked")
    public List<Item> getSiblings() {
        if (getVariant() instanceof IVariant.Single) {
            return getRegistry().getObjects().get(getRegistryName());
        }
        return getRegistry().getObjects().get(getSiblingsKey((I) this));
    }

    @Override
    public Registry<Item> getRegistry() {
        return this.registry;
    }

    @Override
    public void setRegistry(Registry<Item> registry) {
        this.registry = registry;
    }
}
