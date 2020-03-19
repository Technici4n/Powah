package owmii.powah.compat.crafttweaker.energizing;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import org.openzen.zencode.java.ZenCodeType;
import owmii.powah.block.energizing.EnergizingRecipe;
import owmii.powah.recipe.Recipes;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenCodeType.Name("mods.powah.Energizing")
public class Energizing implements IRecipeManager {
    @ZenCodeType.Method
    public void addRecipe(IItemStack output, int energy, IIngredient[] ingredients) {
        CraftTweakerAPI.apply(new Add(this, output, energy, ingredients));
    }

    @Override
    public IRecipeType getRecipeType() {
        return Recipes.ENERGIZING;
    }

    static class Add implements IRuntimeAction {
        private final EnergizingRecipe recipe;
        private final IRecipeManager manager;

        public Add(IRecipeManager manager, IItemStack output, int energy, IIngredient[] ingredients) {
            this.manager = manager;
            List<Ingredient> ing = new ArrayList<>();
            for (IIngredient iIngredient : ingredients) {
                ing.add(iIngredient.asVanillaIngredient());
            }
            this.recipe = new EnergizingRecipe(output.getInternal(), energy, ing.toArray(new Ingredient[0]));
        }

        @Override
        public void apply() {
            this.manager.getRecipes().put(this.recipe.getId(), this.recipe);
        }

        @Override
        public String describe() {
            return "[Powah] Added new Energizing recipe for: " + this.recipe.getRecipeOutput().getDisplayName();
        }
    }
}
