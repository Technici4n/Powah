package owmii.lib.registry;

import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

public interface IRegistryObject<T extends IForgeRegistryEntry<T>> {
    default T byVariant(IVariant variant) {
        return getSiblings().get(variant.ordinal());
    }

    List<T> getSiblings();

    Registry<T> getRegistry();

    void setRegistry(Registry<T> registry);
}
