package owmii.powah.compat.crafttweaker.energizing;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.minecraft.item.crafting.Ingredient;
import org.openzen.zencode.java.ZenCodeType;
import owmii.powah.api.recipe.energizing.EnergizingRecipe;
import owmii.powah.api.recipe.energizing.EnergizingRecipeSorter;
import owmii.powah.api.recipe.energizing.IEnergizingRecipe;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.powah.Energizing")
public class Energizing {
    @ZenCodeType.Method
    public static void addRecipe(IItemStack output, int energy, IIngredient[] ingredients) {
        CraftTweakerAPI.apply(new Add(output, energy, ingredients));
    }

    @ZenCodeType.Method
    public static void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new Remove(output));
    }

    @ZenCodeType.Method
    public static void clearAll() {
        CraftTweakerAPI.apply(new Clear());
    }

    static class Clear implements IRuntimeAction {

        @Override
        public void apply() {
            EnergizingRecipeSorter.RECIPES.clear();
        }

        @Override
        public String describe() {
            return "[Powah] Cleared all recipes.";
        }
    }

    static class Add implements IRuntimeAction {
        private final IEnergizingRecipe recipe;

        public Add(IItemStack output, int energy, IIngredient[] ingredients) {
            List<Ingredient> ing = new ArrayList<>();
            for (IIngredient iIngredient : ingredients) {
                ing.add(iIngredient.asVanillaIngredient());
            }
            this.recipe = new EnergizingRecipe(output.getInternal(), energy, ing.toArray(new Ingredient[0]));
        }

        @Override
        public void apply() {
            EnergizingRecipeSorter.RECIPES.add(this.recipe);
        }

        @Override
        public String describe() {
            return "[Powah] Created new Energizing recipe for: " + this.recipe.getOutput().getDisplayName();
        }
    }

    static class Remove implements IRuntimeAction {
        private final IItemStack output;

        public Remove(IItemStack output) {
            this.output = output;
        }

        @Override
        public void apply() {
            EnergizingRecipeSorter.RECIPES.removeIf(recipe -> recipe.getOutput().isItemEqual(this.output.getInternal()));
        }

        @Override
        public String describe() {
            return "[Powah] Removed Energizing recipes for: " + this.output.getInternal().getDisplayName();
        }
    }
}
