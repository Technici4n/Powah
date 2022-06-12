package owmii.powah.config;

/* TODO ARCH
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.powah.api.PowahAPI;

import java.util.HashMap;
import java.util.Map;

import static owmii.powah.config.Config.MARKER;
import static owmii.powah.Powah.LOGGER;

public class ConfigHandler {
    private static final Map<Fluid, Integer> MAGMATIC_FLUIDS = new HashMap<>();
    private static final Map<Block, Integer> HEAT_SOURCES = new HashMap<>();
    private static final Map<Fluid, Integer> COOLANT_FLUIDS = new HashMap<>();

    public static void post() {
        collectCoolantFluids();
        collectHeatSources();
        collectMagmaticFluids();
    }

    static void collectCoolantFluids() {
        if (Configs.GENERAL.coolantFluidsAPI.get()) COOLANT_FLUIDS.putAll(PowahAPI.COOLANTS);
        Configs.GENERAL.coolantFluids.get().forEach(s -> {
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
                                        COOLANT_FLUIDS.put(fluid, coldness);
                                        LOGGER.info(MARKER, "Added coolant fluid: {}, with coldness of: {} per mb", regName, args[1]);
                                    } else
                                        LOGGER.warn(MARKER, "Ignored coolant fluid: {}, with coldness > 1 or < -100: {}", regName, args[1]);
                                } catch (Exception e) {
                                    LOGGER.fatal(MARKER, "Ignored coolant fluid: {}, for incorrect args: {}", regName, args[1]);
                                    e.printStackTrace();
                                }
                            } else LOGGER.fatal(MARKER, "Ignored Wrong coolant fluid registry name {}.", regName);
                        } catch (Exception e) {
                            LOGGER.fatal(MARKER, "Ignored Wrong coolant fluid registry name {}.", regName);
                            e.printStackTrace();
                        }
                    } else LOGGER.warn(MARKER, "Ignored coolant fluid for missing mod: {}.", rl.getNamespace());
                } else LOGGER.fatal(MARKER, "Ignored Wrong coolant fluid registry name {}.", regName);
            }
        });
        PowahAPI.COOLANTS.clear();
        PowahAPI.COOLANTS.putAll(COOLANT_FLUIDS);
    }

    static void collectHeatSources() {
        if (Configs.GENERAL.heatBlocksAPI.get()) HEAT_SOURCES.putAll(PowahAPI.HEAT_SOURCES);
        Configs.GENERAL.heatBlocks.get().forEach(s -> {
            String[] args = s.split("=");
            if (args.length == 2) {
                String regName = args[0];
                if (regName.contains(":")) {
                    ResourceLocation rl = new ResourceLocation(regName);
                    if (ModList.get().isLoaded(rl.getNamespace().toLowerCase())) {
                        try {
                            Block block = ForgeRegistries.BLOCKS.getValue(rl);
                            if (block != null && block != Blocks.AIR && ForgeRegistries.BLOCKS.containsKey(rl)) {
                                try {
                                    int heat = Integer.parseInt(args[1]);
                                    if (heat > 0 && heat < 900000000) {
                                        HEAT_SOURCES.put(block, heat);
                                        LOGGER.info(MARKER, "Added block: {}, with heat of: {}", regName, args[1]);
                                    } else
                                        LOGGER.warn(MARKER, "Ignored block: {}, with heat < 1 or > 900000000: {}", regName, args[1]);
                                } catch (Exception e) {
                                    LOGGER.fatal(MARKER, "Ignored block: {}, for incorrect args: {}", regName, args[1]);
                                    e.printStackTrace();
                                }
                            } else LOGGER.fatal(MARKER, "Ignored air or unregistered block registry name {}.", regName);
                        } catch (Exception e) {
                            LOGGER.fatal(MARKER, "Ignored Wrong block registry name {}.", regName);
                            e.printStackTrace();
                        }
                    } else LOGGER.warn(MARKER, "Ignored block for missing mod: {}.", rl.getNamespace());
                } else LOGGER.fatal(MARKER, "Ignored Wrong block registry name {}.", regName);
            }
        });
        PowahAPI.HEAT_SOURCES.clear();
        PowahAPI.HEAT_SOURCES.putAll(HEAT_SOURCES);
    }

    static void collectMagmaticFluids() {
        if (Configs.MAGMATOR.magmaticFluidsAPI.get()) MAGMATIC_FLUIDS.putAll(PowahAPI.MAGMATIC_FLUIDS);
        Configs.MAGMATOR.magmaticFluids.get().forEach(s -> {
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
                                    if (heat > 0 && heat <= 900000000) {
                                        MAGMATIC_FLUIDS.put(fluid, heat);
                                        LOGGER.info(MARKER, "Added fluid: {}, with heat of: {} per 100 mb", regName, args[1]);
                                    } else
                                        LOGGER.warn(MARKER, "Ignored fluid: {}, with heat < 1 or > 900000000: {}", regName, args[1]);
                                } catch (Exception e) {
                                    LOGGER.fatal(MARKER, "Ignored fluid: {}, for incorrect args: {}", regName, args[1]);
                                    e.printStackTrace();
                                }
                            } else LOGGER.fatal(MARKER, "Ignored Wrong fluid registry name {}.", regName);
                        } catch (Exception e) {
                            LOGGER.fatal(MARKER, "Ignored Wrong fluid registry name {}.", regName);
                            e.printStackTrace();
                        }
                    } else LOGGER.warn(MARKER, "Ignored fluid for missing mod: {}.", rl.getNamespace());
                } else LOGGER.fatal(MARKER, "Ignored Wrong fluid registry name {}.", regName);
            }
        });
        PowahAPI.MAGMATIC_FLUIDS.clear();
        PowahAPI.MAGMATIC_FLUIDS.putAll(MAGMATIC_FLUIDS);
    }
}
 */
