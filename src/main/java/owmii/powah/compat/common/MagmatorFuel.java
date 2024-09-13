package owmii.powah.compat.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import owmii.powah.Powah;
import owmii.powah.api.MagmatorFuelValue;

public record MagmatorFuel(ResourceLocation id, Fluid fluid, Set<BucketItem> buckets, int heat) {
    public static List<MagmatorFuel> getAll() {
        List<MagmatorFuel> result = new ArrayList<>();

        var dataMap = BuiltInRegistries.FLUID.getDataMap(MagmatorFuelValue.DATA_MAP_TYPE);
        for (var entry : dataMap.entrySet()) {
            var id = entry.getKey().location();
            var heat = entry.getValue();
            var fluid = BuiltInRegistries.FLUID.get(id);

            if (!fluid.isSource(fluid.defaultFluidState())) {
                continue;
            }

            var buckets = new HashSet<BucketItem>();
            var bucket = fluid.getBucket();
            if (bucket instanceof BucketItem bucketItem && bucketItem.content == fluid) {
                buckets.add(bucketItem);
            }

            result.add(new MagmatorFuel(Powah.id("magmator_fuel/" + id.getNamespace() + "/" + id.getPath()), fluid, buckets, heat.energyProduced()));
        }

        // Order by energy produced
        result.sort(Comparator.comparingInt(MagmatorFuel::heat));

        return result;
    }

}
