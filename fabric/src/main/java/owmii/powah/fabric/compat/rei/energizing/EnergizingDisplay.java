package owmii.powah.fabric.compat.rei.energizing;

import java.util.List;
import java.util.Optional;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import owmii.powah.block.energizing.EnergizingRecipe;

public class EnergizingDisplay implements Display {
    private final RecipeHolder<EnergizingRecipe> holder;
    private final List<EntryIngredient> inputs, output;
    private final long energy;

    public EnergizingDisplay(RecipeHolder<EnergizingRecipe> holder) {
        this.holder = holder;
        var recipe = holder.value();
        this.inputs = EntryIngredients.ofIngredients(recipe.getIngredients());
        this.output = List.of(EntryIngredients.of(recipe.getResultItem()));
        this.energy = recipe.getScaledEnergy();
    }

    @Override
    public Optional<ResourceLocation> getDisplayLocation() {
        return Optional.of(holder.id());
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
