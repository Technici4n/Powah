package owmii.powah.compat.rei;

import java.util.Collections;
import java.util.List;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import owmii.powah.compat.common.SolidCoolant;

public class SolidCoolantDisplay implements Display {

    private final List<EntryIngredient> inputs;

    private final int amount, coldness;

    public SolidCoolantDisplay(SolidCoolant recipe) {
        this.inputs = List.of(EntryIngredients.of(recipe.item()));
        this.amount = recipe.amount();
        this.coldness = recipe.temperature();
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
    public CategoryIdentifier<SolidCoolantDisplay> getCategoryIdentifier() {
        return SolidCoolantCategory.ID;
    }

    public int getAmount() {
        return amount;
    }

    public int getColdness() {
        return coldness;
    }

}
