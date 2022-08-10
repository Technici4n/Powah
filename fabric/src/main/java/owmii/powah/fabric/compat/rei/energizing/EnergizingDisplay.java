package owmii.powah.fabric.compat.rei.energizing;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import owmii.powah.block.energizing.EnergizingRecipe;

import java.util.List;

public class EnergizingDisplay implements Display {
    private final List<EntryIngredient> inputs, output;
    private final long energy;

    public EnergizingDisplay(EnergizingRecipe recipe) {
        this.inputs = EntryIngredients.ofIngredients(recipe.getIngredients());
        this.output = List.of(EntryIngredients.of(recipe.getResultItem()));
        this.energy = recipe.getEnergy();
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return output;
    }

    @Override
    public CategoryIdentifier<EnergizingDisplay> getCategoryIdentifier() {
        return EnergizingCategory.ID;
    }

    public long getEnergy() {
        return energy;
    }
}
