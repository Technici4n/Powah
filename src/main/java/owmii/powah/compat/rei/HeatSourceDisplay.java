package owmii.powah.compat.rei;

import java.util.Collections;
import java.util.List;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import owmii.powah.compat.common.PassiveHeatSource;

public class HeatSourceDisplay implements Display {

    private final List<EntryIngredient> inputs;

    private final int heat;

    public HeatSourceDisplay(PassiveHeatSource recipe) {
        if (recipe.fluid() == null) {
            inputs = List.of(EntryIngredients.of(recipe.block()));
        } else {
            inputs = List.of(EntryIngredients.of(recipe.fluid()));
        }
        this.heat = recipe.heat();
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return Collections.emptyList();
    }

    @Override
    public CategoryIdentifier<HeatSourceDisplay> getCategoryIdentifier() {
        return HeatSourceCategory.ID;
    }

    public int getHeat() {
        return heat;
    }

}
