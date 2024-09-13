package owmii.powah.compat.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.api.PassiveHeatSourceConfig;

public record PassiveHeatSource(ResourceLocation id, @Nullable Block block, @Nullable Fluid fluid, int heat) {
    public static List<PassiveHeatSource> getAll() {
        var result = new ArrayList<PassiveHeatSource>();

        for (var entry : BuiltInRegistries.BLOCK.getDataMap(PassiveHeatSourceConfig.BLOCK_DATA_MAP).entrySet()) {
            var id = entry.getKey().location();
            int heat = entry.getValue().temperature();

            var block = BuiltInRegistries.BLOCK.get(id);
            var recipeId = Powah.id("passive_heat_source/block/" + id.getNamespace() + "/" + id.getPath());
            result.add(new PassiveHeatSource(recipeId, block, null, heat));
        }

        for (var entry : BuiltInRegistries.FLUID.getDataMap(PassiveHeatSourceConfig.FLUID_DATA_MAP).entrySet()) {
            var id = entry.getKey().location();
            int heat = entry.getValue().temperature();
            var fluid = BuiltInRegistries.FLUID.get(id);

            if (!fluid.isSource(fluid.defaultFluidState())) {
                continue;
            }

            var recipeId = Powah.id("passive_heat_source/fluid/" + id.getNamespace() + "/" + id.getPath());
            result.add(new PassiveHeatSource(recipeId, null, fluid, heat));
        }

        // Order by heat
        result.sort(Comparator.comparingInt(PassiveHeatSource::heat));

        return result;
    }
}
