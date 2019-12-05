package zeroneye.powah.handler;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import zeroneye.powah.api.PowahAPI;
import zeroneye.powah.config.Config;

import java.util.HashMap;
import java.util.Map;

import static zeroneye.powah.Powah.LOGGER;
import static zeroneye.powah.config.Config.MARKER;

public class MagmaticFluidHandler {
    private static final Map<Fluid, Integer> MAGMATIC_FLUIDS = new HashMap<>();

    public static void post() {
        if (Config.MAGMATIC_GENERATOR.magmaticFluidsAPI.get()) {
            MAGMATIC_FLUIDS.putAll(PowahAPI.MAGMATIC_FLUIDS);
        }

        Config.MAGMATIC_GENERATOR.magmaticFluids.get().forEach(s -> {
            String[] args = s.split("=");
            if (args.length == 2) {
                String regName = args[0];
                if (regName.contains(":")) {
                    ResourceLocation rl = new ResourceLocation(regName);
                    if (ModList.get().isLoaded(rl.getNamespace().toLowerCase())) {
                        try {
                            Fluid fluid = ForgeRegistries.FLUIDS.getValue(rl);
                            if (fluid != null) {
                                try {
                                    int heat = Integer.parseInt(args[1]);
                                    if (heat > 0) {
                                        MAGMATIC_FLUIDS.put(fluid, heat);
                                        LOGGER.info(MARKER, "Added fluid: {}, with heat of: {} per 100 mb", regName, args[1]);
                                    } else {
                                        LOGGER.warn(MARKER, "Ignored fluid: {}, with heat < 1: {}", regName, args[1]);
                                    }
                                } catch (Exception e) {
                                    LOGGER.fatal(MARKER, "Ignored fluid: {}, for incorrect args: {}", regName, args[1]);
                                    e.printStackTrace();
                                }
                            } else {
                                LOGGER.fatal(MARKER, "Ignored Wrong fluid registry name {}.", regName);
                            }
                        } catch (Exception e) {
                            LOGGER.fatal(MARKER, "Ignored Wrong fluid registry name {}.", regName);
                            e.printStackTrace();
                        }
                    } else {
                        LOGGER.warn(MARKER, "Ignored fluid for missing mod: {}.", rl.getNamespace());
                    }
                } else {
                    LOGGER.fatal(MARKER, "Ignored Wrong fluid registry name {}.", regName);
                }
            }
        });
        PowahAPI.MAGMATIC_FLUIDS.clear();
        PowahAPI.MAGMATIC_FLUIDS.putAll(MAGMATIC_FLUIDS);
    }
}
