package owmii.powah.compat.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import owmii.powah.Powah;
import owmii.powah.api.FluidCoolantConfig;

public record FluidCoolant(ResourceLocation id, Fluid fluid, Set<BucketItem> buckets, int coldness) {
    public static Collection<FluidCoolant> getAll() {
        List<FluidCoolant> result = new ArrayList<>();

        var dataMap = BuiltInRegistries.FLUID.getDataMap(FluidCoolantConfig.DATA_MAP_TYPE);
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

            result.add(new FluidCoolant(Powah.id("fluid_coolant/" + id.getNamespace() + "/" + id.getPath()), fluid, buckets, heat.temperature()));
        }

        // Order by coldness * amount
        result.sort(Comparator.comparingInt(p -> p.coldness));

        return result;
    }

}
