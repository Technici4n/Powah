package zeroneye.powah.api;

import net.minecraft.fluid.Fluid;

import java.util.HashMap;
import java.util.Map;

public class PowahAPI {
    public static final Map<Fluid, Integer> MAGMATIC_FLUIDS = new HashMap<>();


    public static void registerMagmaticFluid(Fluid fluid, int heat) {
        MAGMATIC_FLUIDS.put(fluid, heat);
    }

    public static int getFluidHeat(Fluid fluid) {
        if (MAGMATIC_FLUIDS.containsKey(fluid)) {
            return MAGMATIC_FLUIDS.get(fluid);
        }
        return 0;
    }
}
