package owmii.powah.recipe;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import owmii.powah.Powah;
import owmii.powah.block.energizing.EnergizingRecipe;

public class Recipes {
    public static final DeferredRegister<RecipeSerializer<?>> DR_SERIALIZER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Powah.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> DR_TYPE = DeferredRegister.create(Registries.RECIPE_TYPE, Powah.MOD_ID);

    public static final Supplier<EnergizingRecipe.Serializer> ENERGIZING_SERIALIZER = DR_SERIALIZER.register("energizing",
            EnergizingRecipe.Serializer::new);
    public static final Supplier<RecipeType<EnergizingRecipe>> ENERGIZING = DR_TYPE.register("energizing", () -> new RecipeType<>() {
        public String toString() {
            return EnergizingRecipe.ID.toString();
        }
    });

    public static final Supplier<RecipeSerializer<ReactorFuelRecipe>> REACTOR_FUEL_SERIALIZER = DR_SERIALIZER.register(
            ReactorFuelRecipe.TYPE_ID.getPath(),
            () -> ReactorFuelRecipe.SERIALIZER);
    public static final Supplier<RecipeType<ReactorFuelRecipe>> REACTOR_FUEL = DR_TYPE.register("reactor_fuel",
            () -> RecipeType.simple(ReactorFuelRecipe.TYPE_ID));
}
