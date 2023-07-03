package owmii.powah.api;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.tuple.Pair;

public class PowahAPI {

    // Magmatic gen fluids
    public static final Map<ResourceLocation, Integer> MAGMATIC_FLUIDS = new HashMap<>();

    // Coolants/heat sources
    public static final Map<ResourceLocation, Integer> COOLANT_FLUIDS = new HashMap<>();
    public static final Map<ResourceLocation, Pair<Integer, Integer>> SOLID_COOLANTS = new HashMap<>();
    public static final Map<ResourceLocation, Integer> HEAT_SOURCES = new HashMap<>();

    /**
     * Register fluid that used to fuel the Magmatic generator.
     *
     * @param fluid: the fluid used as fuel.
     * @param heat:  the heat of the fluid.
     **/
    public static void registerMagmaticFluid(ResourceLocation fluid, int heat) {
        MAGMATIC_FLUIDS.put(fluid, heat);
    }

    /**
     * the heat of the fluid used in the Magmatic generator.
     *
     * @param fluid: the fluid used as fuel.
     * @return the heat value;
     **/
    public static int getMagmaticFluidHeat(Fluid fluid) {
        return MAGMATIC_FLUIDS.getOrDefault(BuiltInRegistries.FLUID.getKey(fluid), 0);
    }

    /**
     * Register a coolant fluid.
     *
     * @param fluid:   the fluid used as coolant.
     * @param cooling: the coldness of the fluid.
     **/
    public static void registerCoolant(ResourceLocation fluid, int cooling) {
        COOLANT_FLUIDS.put(fluid, cooling);
    }

    /**
     * the coldness of the coolant fluid.
     *
     * @param fluid: the fluid used as coolant.
     * @return the coldness value;
     **/
    public static int getCoolant(Fluid fluid) {
        return COOLANT_FLUIDS.getOrDefault(BuiltInRegistries.FLUID.getKey(fluid), 0);
    }

    /**
     * Register the heat source block/fluid block.
     *
     * @param block: the block used as heat source.
     * @param heat:  the heat of the block.
     **/
    public static void registerHeatSource(ResourceLocation block, int heat) {
        HEAT_SOURCES.put(block, heat);
    }

    /**
     * the heat of the heat source block/fluid block.
     *
     * @param block: the block used as heat source.
     * @return the heat of the block;
     **/
    public static int getHeatSource(Block block) {
        return HEAT_SOURCES.getOrDefault(BuiltInRegistries.BLOCK.getKey(block), 0);
    }

    /**
     * Register a solid coolant item.
     *
     * @param item:    the stack used as solid coolant.
     * @param cooling: the coldness of the stack.
     **/
    public static void registerSolidCoolant(ItemLike item, int size, int cooling) {
        SOLID_COOLANTS.put(BuiltInRegistries.ITEM.getKey(item.asItem()), Pair.of(size, cooling));
    }

    /**
     * the coldness of the solid coolant.
     *
     * @param item: the stack used as solid coolant.
     * @return the coldness value;
     **/
    public static Pair<Integer, Integer> getSolidCoolant(ItemLike item) {
        return SOLID_COOLANTS.getOrDefault(BuiltInRegistries.ITEM.getKey(item.asItem()), Pair.of(0, 0));
    }
}
