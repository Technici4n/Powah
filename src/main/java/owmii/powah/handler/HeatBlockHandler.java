package owmii.powah.handler;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.powah.api.PowahAPI;
import owmii.powah.config.Config;

import java.util.HashMap;
import java.util.Map;

import static owmii.powah.Powah.LOGGER;
import static owmii.powah.config.Config.MARKER;

public class HeatBlockHandler {
    private static final Map<Block, Integer> HEAT_BLOCKS = new HashMap<>();

    public static void post() {
        if (Config.THERMO_GEN_CONFIG.heatBlocksAPI.get()) {
            HEAT_BLOCKS.putAll(PowahAPI.HEAT_BLOCKS);
        }

        Config.THERMO_GEN_CONFIG.heatBlocks.get().forEach(s -> {
            String[] args = s.split("=");
            if (args.length == 2) {
                String regName = args[0];
                if (regName.contains(":")) {
                    ResourceLocation rl = new ResourceLocation(regName);
                    if (ModList.get().isLoaded(rl.getNamespace().toLowerCase())) {
                        try {
                            Block block = ForgeRegistries.BLOCKS.getValue(rl);
                            if (block != null) {
                                try {
                                    int heat = Integer.parseInt(args[1]);
                                    if (heat > 0) {
                                        HEAT_BLOCKS.put(block, heat);
                                        LOGGER.info(MARKER, "Added block: {}, with heat of: {}", regName, args[1]);
                                    } else {
                                        LOGGER.warn(MARKER, "Ignored block: {}, with heat < 1: {}", regName, args[1]);
                                    }
                                } catch (Exception e) {
                                    LOGGER.fatal(MARKER, "Ignored block: {}, for incorrect args: {}", regName, args[1]);
                                    e.printStackTrace();
                                }
                            } else {
                                LOGGER.fatal(MARKER, "Ignored Wrong block registry name {}.", regName);
                            }
                        } catch (Exception e) {
                            LOGGER.fatal(MARKER, "Ignored Wrong block registry name {}.", regName);
                            e.printStackTrace();
                        }
                    } else {
                        LOGGER.warn(MARKER, "Ignored block for missing mod: {}.", rl.getNamespace());
                    }
                } else {
                    LOGGER.fatal(MARKER, "Ignored Wrong block registry name {}.", regName);
                }
            }
        });
        PowahAPI.HEAT_BLOCKS.clear();
        PowahAPI.HEAT_BLOCKS.putAll(HEAT_BLOCKS);
    }
}
