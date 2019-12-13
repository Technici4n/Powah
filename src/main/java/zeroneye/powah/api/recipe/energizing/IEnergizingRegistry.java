package zeroneye.powah.api.recipe.energizing;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Set;

public interface IEnergizingRegistry {
    Set<IEnergizingRecipe> getRecipes();

    default void addRecipe(IEnergizingRecipe recipe) {
        getRecipes().add(recipe);
    }

    default void addRecipe(ItemStack output, int energy, Ingredient... ingredients) {
        getRecipes().add(new EnergizingRecipe(output, energy, ingredients));
    }
}
