package owmii.powah.api;

import java.util.OptionalInt;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class PowahAPI {
    private PowahAPI() {
    }

    /**
     * the heat of the fluid used in the Magmatic generator.
     *
     * @param fluid: the fluid used as fuel.
     * @return the heat value;
     **/
    public static int getMagmaticFluidHeat(Fluid fluid) {
        var config = BuiltInRegistries.FLUID.getData(PassiveHeatSourceConfig.FLUID_DATA_MAP, fluid.builtInRegistryHolder().key());
        if (config == null) {
            return 0;
        }
        return config.temperature();
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
        var config = BuiltInRegistries.BLOCK.getData(PassiveHeatSourceConfig.BLOCK_DATA_MAP, block.builtInRegistryHolder().key());
        if (config == null) {
            return 0;
        }
        return config.temperature();
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
