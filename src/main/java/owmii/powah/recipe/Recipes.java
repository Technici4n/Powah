package owmii.powah.recipe;

import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import owmii.powah.Powah;
import owmii.powah.block.energizing.EnergizingRecipe;

@SuppressWarnings("unchecked")
public class Recipes {
    public static final owmii.lib.registry.Registry<RecipeSerializer<?>> REG = new owmii.lib.registry.Registry(RecipeSerializer.class, Powah.MOD_ID);
    public static final EnergizingRecipe.Serializer ENERGIZING_SERIALIZER = REG.register("energizing", new EnergizingRecipe.Serializer());
    public static final RecipeType<EnergizingRecipe> ENERGIZING = Registry.register(Registry.RECIPE_TYPE, EnergizingRecipe.ID.toString(), new RecipeType<EnergizingRecipe>() {
        public String toString() {
            return EnergizingRecipe.ID.toString();
        }
    });
}
