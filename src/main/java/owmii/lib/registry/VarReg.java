package owmii.lib.registry;

import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.IntFunction;

public class VarReg<V extends Enum<V> & IVariant<V>, E extends IForgeRegistryEntry<E>> {
    private final LinkedHashMap<V, E> all = new LinkedHashMap<>();
    private final String name;
    private final VarObject<V, E> varObject;
    private final V[] variants;
    private Registry<E> registry;

    public VarReg(String name, VarObject<V, E> varObject, V[] variants, Registry<E> registry) {
        this.name = name;
        this.varObject = varObject;
        this.variants = variants;
        this.registry = registry;
        for (V variant : variants) {
            final E register = this.registry.register(this.name, this.varObject.get(variant), variant);
            this.all.put(variant, register);
        }
    }

    public E[] getArr(IntFunction<E[]> generator) {
        E[] es = getAll().stream().toArray(generator);
        return es;
    }

    public List<E> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(this.all.values()));
    }

    public E get(V variant) {
        return this.all.get(variant);
    }

    @FunctionalInterface
    public interface VarObject<V extends Enum<V> & IVariant<V>, E extends IForgeRegistryEntry<E>> {
        E get(V variant);
    }
}
