package owmii.powah.lib.registry;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import owmii.powah.block.Tier;

public class TieredItemReg {
    private final String name;
    private final EnumSet<Tier> tiers;

    private final Map<Tier, DeferredItem<Item>> all = new EnumMap<>(Tier.class);

    public TieredItemReg(DeferredRegister.Items dr, String name, Factory factory, Tier[] variants) {
        this.name = name;
        this.tiers = EnumSet.noneOf(Tier.class);
        for (Tier tier : variants) {
            var entryName = name + "_" + tier.getSerializedName();
            this.all.put(tier, dr.registerItem(entryName, props -> factory.get(tier, props)));
            this.tiers.add(tier);
        }
    }

    public Item[] getArr() {
        return getAll().toArray(Item[]::new);
    }

    public List<Item> getAll() {
        return all.values().stream().map(DeferredHolder::get).toList();
    }

    public List<ResourceKey<Item>> getAllResourceKeys() {
        return all.values().stream().map(DeferredHolder::key).toList();
    }

    public Set<Tier> getTiers() {
        return Collections.unmodifiableSet(tiers);
    }

    public Item get(Tier tier) {
        if (!this.all.containsKey(tier)) {
            throw new IllegalStateException("Tiered item " + name + " does not have tier " + tier);
        }
        return this.all.get(tier).get();
    }

    @Override
    public String toString() {
        return name;
    }

    @FunctionalInterface
    public interface Factory {
        Item get(Tier variant, Item.Properties properties);
    }
}
