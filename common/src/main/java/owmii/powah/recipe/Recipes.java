package owmii.powah.recipe;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import owmii.powah.Powah;
import owmii.powah.block.energizing.EnergizingRecipe;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class Recipes {
    public static final DeferredRegister<RecipeSerializer<?>> DR_SERIALIZER = DeferredRegister.create(Powah.MOD_ID, Registry.RECIPE_SERIALIZER_REGISTRY);
    public static final DeferredRegister<RecipeType<?>> DR_TYPE = DeferredRegister.create(Powah.MOD_ID, Registry.RECIPE_TYPE_REGISTRY);

    public static final Supplier<EnergizingRecipe.Serializer> ENERGIZING_SERIALIZER = DR_SERIALIZER.register("energizing", EnergizingRecipe.Serializer::new);
    public static final Supplier<RecipeType<EnergizingRecipe>> ENERGIZING = DR_TYPE.register("energizing", () -> new RecipeType<>() {
        public String toString() {
            return EnergizingRecipe.ID.toString();
        }
    });
}
