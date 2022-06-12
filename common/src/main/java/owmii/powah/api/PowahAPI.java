package owmii.powah.api;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class PowahAPI {

    // Magmatic gen fluids
    public static final Map<Fluid, Integer> MAGMATIC_FLUIDS = new HashMap<>();

    // Coolants/heat sources
    public static final Map<Fluid, Integer> COOLANTS = new HashMap<>();
    public static final Map<ResourceLocation, Pair<Integer, Integer>> SOLID_COOLANTS = new HashMap<>();
    public static final Map<Block, Integer> HEAT_SOURCES = new HashMap<>();

    /**
     * Register fluid that used to fuel the Magmatic generator.
     *
     * @param fluid: the fluid used as fuel.
     * @param heat:  the heat of the fluid.
     **/
    public static void registerMagmaticFluid(Fluid fluid, int heat) {
        MAGMATIC_FLUIDS.put(fluid, heat);
    }

    /**
     * the heat of the fluid used in the Magmatic generator.
     *
     * @param fluid: the fluid used as fuel.
     * @return the heat value;
     **/
    public static int getMagmaticFluidHeat(Fluid fluid) {
        if (MAGMATIC_FLUIDS.containsKey(fluid)) {
            return MAGMATIC_FLUIDS.get(fluid);
        }
        return 0;
    }

    /**
     * Register a coolant fluid.
     *
     * @param fluid:   the fluid used as coolant.
     * @param cooling: the coldness of the fluid.
     **/
    public static void registerCoolant(Fluid fluid, int cooling) {
        COOLANTS.put(fluid, cooling);
    }

    /**
     * the coldness of the coolant fluid.
     *
     * @param fluid: the fluid used as coolant.
     * @return the coldness value;
     **/
    public static int getCoolant(Fluid fluid) {
        if (COOLANTS.containsKey(fluid)) {
            return COOLANTS.get(fluid);
        }
        return 0;
    }

    /**
     * Register the heat source block/fluid block.
     *
     * @param block: the block used as heat source.
     * @param heat:  the heat of the block.
     **/
    public static void registerHeatSource(Block block, int heat) {
        if (block == Blocks.AIR || Registry.BLOCK.getKey(block).equals(Registry.BLOCK.getDefaultKey())) {
            throw new IllegalArgumentException(block + " is air or unregistered");
        }
        HEAT_SOURCES.put(block, heat);
    }

    /**
     * the heat of the heat source block/fluid block.
     *
     * @param block: the block used as heat source.
     * @return the heat of the block;
     **/
    public static int getHeatSource(Block block) {
        if (HEAT_SOURCES.containsKey(block)) {
            return HEAT_SOURCES.get(block);
        }
        return 0;
    }

    /**
     * Register a solid coolant item.
     *
     * @param item:    the stack used as solid coolant.
     * @param cooling: the coldness of the stack.
     **/
    public static void registerSolidCoolant(ItemLike item, int size, int cooling) {
        SOLID_COOLANTS.put(Registry.ITEM.getKey(item.asItem()), Pair.of(size, cooling));
    }

    /**
     * the coldness of the solid coolant.
     *
     * @param item: the stack used as solid coolant.
     * @return the coldness value;
     **/
    public static Pair<Integer, Integer> getSolidCoolant(ItemLike item) {
        var regName = Registry.ITEM.getKey(item.asItem());
        if (SOLID_COOLANTS.containsKey(regName)) {
            return SOLID_COOLANTS.get(regName);
        }
        return Pair.of(0, 0);
    }
}
