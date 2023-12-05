package owmii.powah.lib.registry;

import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class VarReg<V extends Enum<V> & IVariant<V>, E> {
    private static final Map<String, List<String>> ALL_VARIANTS = new HashMap<>();

    public static List<String> getSiblingIds(String name) {
        return ALL_VARIANTS.getOrDefault(name, List.of(name));
    }

    private final LinkedHashMap<V, Supplier<E>> all = new LinkedHashMap<>();

    public VarReg(DeferredRegister<E> dr, String name, VariantConstructor<V, E> ctor, V[] variants) {
        for (V variant : variants) {
            var entryName = name + "_" + variant.getName();
            ALL_VARIANTS.computeIfAbsent(name, s -> new ArrayList<>()).add(entryName);
            this.all.put(variant, dr.register(entryName, () -> ctor.get(variant)));
        }
    }

    public E[] getArr(IntFunction<E[]> generator) {
        E[] es = getAll().stream().toArray(generator);
        return es;
    }

    public List<E> getAll() {
        return all.values().stream().map(Supplier::get).toList();
    }

    public E get(V variant) {
        return this.all.get(variant).get();
    }

    @FunctionalInterface
    public interface VariantConstructor<V extends Enum<V> & IVariant<V>, E> {
        E get(V variant);
    }
}
