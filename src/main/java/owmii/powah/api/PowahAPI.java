package owmii.powah.api;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import owmii.powah.api.recipe.energizing.IEnergizingRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PowahAPI {
    // Energizing recipe registries
    public static final Set<IEnergizingRegistry> ENERGIZING_REGISTRIES = new HashSet<>();

    // Magmatic gen fluids
    public static final Map<Fluid, Integer> MAGMATIC_FLUIDS = new HashMap<>();

    // Thermo gen coolants/heat sources
    public static final Map<Fluid, Integer> THERMO_COOLANTS = new HashMap<>();
    public static final Map<Block, Integer> THERMO_HEAT_SOURCES = new HashMap<>();

    // Reactor coolants
    public static final Map<Fluid, Integer> REACTOR_COOLANTS = new HashMap<>();
    public static final Map<ResourceLocation, Pair<Integer, Integer>> REACTOR_SOLID_COOLANTS = new HashMap<>();

    /**
     * Register an Energizing recipes registry
     **/
    public static void register(IEnergizingRegistry registry) {
        ENERGIZING_REGISTRIES.add(registry);
    }

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
     * Register a coolant fluid used in the Thermo generator.
     *
     * @param fluid:   the fluid used as coolant.
     * @param cooling: the coldness of the fluid.
     **/
    public static void registerThermoCoolant(Fluid fluid, int cooling) {
        THERMO_COOLANTS.put(fluid, cooling);
    }

    /**
     * the coldness of the coolant fluid used in the Thermo generator.
     *
     * @param fluid: the fluid used as coolant.
     * @return the coldness value;
     **/
    public static int getThermoCoolant(Fluid fluid) {
        if (THERMO_COOLANTS.containsKey(fluid)) {
            return THERMO_COOLANTS.get(fluid);
        }
        return 0;
    }

    /**
     * Register the heat source block/fluid block used at the bottom of the Thermo generator.
     *
     * @param block: the block used as heat source.
     * @param heat:  the heat of the block.
     **/
    public static void registerThermoHeatSource(Block block, int heat) {
        THERMO_HEAT_SOURCES.put(block, heat);
    }

    /**
     * the heat of the heat source block/fluid block used at the bottom of the Thermo generator.
     *
     * @param block: the block used as heat source.
     * @return the heat of the block;
     **/
    public static int getThermoHeatSource(Block block) {
        if (THERMO_HEAT_SOURCES.containsKey(block)) {
            return THERMO_HEAT_SOURCES.get(block);
        }
        return 0;
    }

    /**
     * Register a coolant fluid used in the Reactor.
     *
     * @param fluid:   the fluid used as coolant.
     * @param cooling: the coldness of the fluid.
     **/
    public static void registerReactorCoolant(Fluid fluid, int cooling) {
        REACTOR_COOLANTS.put(fluid, cooling);
    }

    /**
     * the coldness of the coolant fluid used in the Reactor.
     *
     * @param fluid: the fluid used as coolant.
     * @return the coldness value;
     **/
    public static int getReactorCoolant(Fluid fluid) {
        if (REACTOR_COOLANTS.containsKey(fluid)) {
            return REACTOR_COOLANTS.get(fluid);
        }
        return 0;
    }

    /**
     * Register a solid coolant item used in the Reactor.
     *
     * @param item:    the stack used as solid coolant.
     * @param cooling: the coldness of the stack.
     **/
    public static void registerReactorSolidCoolant(IItemProvider item, int size, int cooling) {
        REACTOR_SOLID_COOLANTS.put(item.asItem().getRegistryName(), Pair.of(size, cooling));
    }

    /**
     * the coldness of the coolant fluid used in the Reactor.
     *
     * @param item: the stack used as solid coolant.
     * @return the coldness value;
     **/
    public static Pair<Integer, Integer> getReactorSolidCoolant(IItemProvider item) {
        if (REACTOR_SOLID_COOLANTS.containsKey(item.asItem().getRegistryName())) {
            return REACTOR_SOLID_COOLANTS.get(item.asItem().getRegistryName());
        }
        return Pair.of(0, 0);
    }
}
