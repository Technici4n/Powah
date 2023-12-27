package owmii.powah.compat.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;

public record FluidCoolant(ResourceLocation id, Fluid fluid, Set<BucketItem> buckets, int coldness) {
    public static Collection<FluidCoolant> getAll() {
        List<FluidCoolant> result = new ArrayList<>();

        for (var entry : PowahAPI.COOLANT_FLUIDS.entrySet()) {
            var id = entry.getKey();
            var heat = entry.getValue();
            var fluid = BuiltInRegistries.FLUID.get(id);
            if (fluid != Fluids.EMPTY) {
                var buckets = new HashSet<BucketItem>();
                var bucket = fluid.getBucket();
                if (bucket instanceof BucketItem bucketItem && bucketItem.getFluid() == fluid) {
                    buckets.add(bucketItem);
                }

                result.add(new FluidCoolant(Powah.id("fluid_coolant/" + id.getNamespace() + "/" + id.getPath()), fluid, buckets, heat));
            }
        }

        return result;
    }

}
