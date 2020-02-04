package owmii.powah.api.recipe.energizing;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.items.ItemStackHandler;
import owmii.powah.api.PowahAPI;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * Used by Powah in the {@link FMLLoadCompleteEvent} to sort the Energizing Recipes from PowahAPI.
 * The Recipes Set hold all sorted recipes, changing/removing any recipe will effect the result of the Energizing Orb.
 **/
public class EnergizingRecipeSorter {
    public static final Set<IEnergizingRecipe> RECIPES = new HashSet<>();

    public static void sort() {
        RECIPES.clear();
        PowahAPI.ENERGIZING_REGISTRIES.forEach(registry -> {
            RECIPES.addAll(registry.getRecipes());
        });
    }

    @Nullable
    public static IEnergizingRecipe get(ItemStackHandler inventory, World world, BlockPos pos) {
        final IEnergizingRecipe[] recipes = {null};
        RECIPES.forEach(recipe -> {
            if (recipe.match(inventory, world, pos)) {
                if (!recipe.getOutput().isEmpty()) {
                    recipes[0] = recipe;
                }
            }
        });
        return recipes[0];
    }
}
