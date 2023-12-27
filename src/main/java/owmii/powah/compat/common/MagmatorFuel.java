package owmii.powah.compat.common;

import com.google.common.collect.HashMultimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;

public record MagmatorFuel(ResourceLocation id, Fluid fluid, Set<BucketItem> buckets, int heat) {
    public static Collection<MagmatorFuel> getAll() {
        List<MagmatorFuel> result = new ArrayList<>();

        var buckets = HashMultimap.<Fluid, BucketItem>create();
        BuiltInRegistries.ITEM.forEach(item -> {
            if (item instanceof BucketItem bucketItem) {
                buckets.put(bucketItem.getFluid(), bucketItem);
            }
        });

        for (var entry : PowahAPI.MAGMATIC_FLUIDS.entrySet()) {
            var id = entry.getKey();
            var heat = entry.getValue();
            var fluid = BuiltInRegistries.FLUID.get(id);
            if (fluid != Fluids.EMPTY) {
                result.add(new MagmatorFuel(Powah.id("magmator_fuel/" + id.getNamespace() + "/" + id.getPath()), fluid, buckets.get(fluid), heat));
            }
        }

        return result;
    }

}
