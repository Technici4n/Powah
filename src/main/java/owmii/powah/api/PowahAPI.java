package owmii.powah.api;

import java.util.OptionalInt;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class PowahAPI {
    private PowahAPI() {
    }

    /**
     * the heat of the fluid used in the Magmatic generator.
     *
     * @param fluid: the fluid used as fuel.
     * @return the heat value;
     **/
    public static int getMagmaticFluidEnergyProduced(Fluid fluid) {
        var config = BuiltInRegistries.FLUID.getData(MagmatorFuelValue.DATA_MAP_TYPE, fluid.builtInRegistryHolder().key());
        if (config == null) {
            return 0;
        }
        return config.energyProduced();
    }

    /**
     * the coldness of the coolant fluid.
     *
     * @param fluid: the fluid used as coolant.
     * @return the coldness value;
     **/
    public static OptionalInt getCoolant(Fluid fluid) {
        var config = BuiltInRegistries.FLUID.getData(FluidCoolantConfig.DATA_MAP_TYPE, fluid.builtInRegistryHolder().key());
        if (config == null) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(config.temperature());
    }

    /**
     * the heat of the heat source block/fluid block.
     *
     * @param block: the block used as heat source.
     * @return the heat of the block;
     **/
    public static int getHeatSource(Block block) {
        var config = BuiltInRegistries.BLOCK.getData(PassiveHeatSourceConfig.BLOCK_DATA_MAP, BuiltInRegistries.BLOCK.wrapAsHolder(block).getKey());
        if (config == null) {
            return 0;
        }
        return config.temperature();
    }

    /**
     * the heat of the heat source block/fluid block.
     *
     * @param fluid: the fluid used as heat source.
     * @return the heat of the block;
     **/
    public static int getHeatSource(Fluid fluid) {
        var config = BuiltInRegistries.FLUID.getData(PassiveHeatSourceConfig.FLUID_DATA_MAP, BuiltInRegistries.FLUID.wrapAsHolder(fluid).getKey());
        if (config == null) {
            return 0;
        }
        return config.temperature();
    }

    public static int getHeatSource(BlockState blockState) {
        var heatFromBlock = 0;
        var heatFromFluid = 0;
        if (!blockState.isEmpty()) {
            heatFromBlock = getHeatSource(blockState.getBlock());
        }
        var fluidState = blockState.getFluidState();
        if (!fluidState.isEmpty()) {
            heatFromFluid = getHeatSource(fluidState.holder().value()) * fluidState.getAmount() / FluidState.AMOUNT_FULL;
        }
        return Math.max(heatFromBlock, heatFromFluid);
    }

    /**
     * the coldness of the solid coolant.
     *
     * @param item: the stack used as solid coolant.
     * @return the coldness value;
     **/
    public static SolidCoolantConfig getSolidCoolant(ItemLike item) {
        var config = BuiltInRegistries.ITEM.getData(SolidCoolantConfig.DATA_MAP_TYPE, item.asItem().builtInRegistryHolder().key());
        if (config == null) {
            return new SolidCoolantConfig(0, 0);
        }
        return config;
    }
}
