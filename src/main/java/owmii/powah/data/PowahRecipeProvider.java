package owmii.powah.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.crafting.Ingredient;
import owmii.powah.Powah;
import owmii.powah.item.Itms;
import owmii.powah.recipe.ReactorFuelRecipe;

class PowahRecipeProvider extends RecipeProvider {
    public PowahRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        reactorFuel(output);
    }

    private static void reactorFuel(RecipeOutput output) {
        output.accept(
                Powah.id("reactor_fuels/uraninite"),
                new ReactorFuelRecipe(Ingredient.of(Itms.URANINITE.get()), 100, 700),
                null);
    }
}
