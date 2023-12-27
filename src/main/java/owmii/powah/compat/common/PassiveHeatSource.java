package owmii.powah.compat.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;

public record PassiveHeatSource(ResourceLocation id, Block block, int heat) {
    public PassiveHeatSource {
        Powah.LOGGER.debug("HEAT SOURCE RECIPE INIT: " + this);
    }

    @Nullable
    public Fluid fluid() {
        if (block instanceof LiquidBlock liquidBlock) {
            return liquidBlock.getFluid();
        } else {
            return null;
        }
    }

    public static Collection<PassiveHeatSource> getAll() {
        Powah.LOGGER.debug("HEAT SOURCE RECIPE ALL: [" + PowahAPI.HEAT_SOURCES.entrySet().stream()
                .map(e -> e.getKey() + " -> " + e.getValue())
                .collect(Collectors.joining(", ")) + "]");

        var result = new ArrayList<PassiveHeatSource>();
        for (var entry : PowahAPI.HEAT_SOURCES.entrySet()) {
            var id = entry.getKey();
            int heat = entry.getValue();

            var block = BuiltInRegistries.BLOCK.get(id);
            if (block != Blocks.AIR) {
                result.add(new PassiveHeatSource(id, block, heat));
            }
        }
        return result;
    }
}
