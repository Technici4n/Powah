package zeroneye.powah.api;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;

import java.util.HashMap;
import java.util.Map;

public class PowahAPI {
    public static final Map<Fluid, Integer> MAGMATIC_FLUIDS = new HashMap<>();
    public static final Map<Fluid, Integer> COOLANT_FLUIDS = new HashMap<>();
    public static final Map<Block, Integer> HEAT_BLOCKS = new HashMap<>();

    public static void registerMagmaticFluid(Fluid fluid, int heat) {
        MAGMATIC_FLUIDS.put(fluid, heat);
    }

    public static int getFluidHeat(Fluid fluid) {
        if (MAGMATIC_FLUIDS.containsKey(fluid)) {
            return MAGMATIC_FLUIDS.get(fluid);
        }
        return 0;
    }

    public static void registerCoolantFluid(Fluid fluid, int cooling) {
        COOLANT_FLUIDS.put(fluid, cooling);
    }

    public static int getCoolantFluid(Fluid fluid) {
        if (COOLANT_FLUIDS.containsKey(fluid)) {
            return COOLANT_FLUIDS.get(fluid);
        }
        return 0;
    }

    public static void registerHeatBlock(Block block, int cooling) {
        HEAT_BLOCKS.put(block, cooling);
    }

    public static int getBlockHeat(Block block) {
        if (HEAT_BLOCKS.containsKey(block)) {
            return HEAT_BLOCKS.get(block);
        }
        return 0;
    }
}
