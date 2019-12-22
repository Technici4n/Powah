package owmii.powah.handler;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.powah.api.PowahAPI;
import owmii.powah.config.Config;

import java.util.HashMap;
import java.util.Map;

import static owmii.powah.Powah.LOGGER;
import static owmii.powah.config.Config.MARKER;

public class CoolingFluidHandler {
    private static final Map<Fluid, Integer> COOLING_FLUIDS = new HashMap<>();

    public static void post() {
        if (Config.THERMO_GEN_CONFIG.coolantFluidsAPI.get()) {
            COOLING_FLUIDS.putAll(PowahAPI.COOLANT_FLUIDS);
        }

        Config.THERMO_GEN_CONFIG.coolantFluids.get().forEach(s -> {
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
                                    int coldness = Integer.parseInt(args[1]);
                                    if (coldness < 2 && coldness > -101) {
                                        COOLING_FLUIDS.put(fluid, coldness);
                                        LOGGER.info(MARKER, "Added fluid: {}, with coldness of: {} per mb", regName, args[1]);
                                    } else {
                                        LOGGER.warn(MARKER, "Ignored fluid: {}, with coldness > 1 or < -100: {}", regName, args[1]);
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
        PowahAPI.COOLANT_FLUIDS.clear();
        PowahAPI.COOLANT_FLUIDS.putAll(COOLING_FLUIDS);
    }
}
