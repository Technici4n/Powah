package owmii.powah.util;

import java.util.Optional;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.loot.NeoForgeLootContextParams;

/**
 * Since 26.3, furnace fuel is the {@link DataComponents#COOKING_FUEL} item component, whose burn time may be resolved
 * against a loot context. This mirrors how vanilla's AbstractFurnaceBlockEntity evaluates it.
 */
public final class FuelUtil {
    private FuelUtil() {
    }

    /**
     * Cheap check usable on both sides, matching vanilla's furnace slot validation.
     */
    public static boolean isFuel(ItemStack stack) {
        return stack.has(DataComponents.COOKING_FUEL);
    }

    /**
     * @return the furnace burn time of the given stack in ticks, or 0 if it is not a fuel.
     */
    public static int getBurnTime(ServerLevel level, BlockEntity blockEntity, ItemStack stack) {
        if (!isFuel(stack)) {
            return 0;
        }
        var context = new LootContext.Builder(
                new LootParams.Builder(level)
                        .withParameter(LootContextParams.BLOCK_STATE, blockEntity.getBlockState())
                        .withParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockEntity.getBlockPos()))
                        .withParameter(LootContextParams.CONTAINER, new SimpleContainer(stack))
                        .withOptionalParameter(NeoForgeLootContextParams.QUERIED_STACK, stack)
                        .create(LootContextParamSets.CONTAINER_PROCESS))
                .create(Optional.empty());
        return ResolvableInt.getFromItem(stack, DataComponents.COOKING_FUEL, CookingFuel::burnTime, context, 0);
    }
}
