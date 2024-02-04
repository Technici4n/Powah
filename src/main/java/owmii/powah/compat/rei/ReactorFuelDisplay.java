package owmii.powah.compat.rei;

import java.util.List;
import java.util.Optional;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import owmii.powah.recipe.ReactorFuelRecipe;

public class ReactorFuelDisplay implements Display {
    private final ResourceLocation id;
    private final ReactorFuelRecipe recipe;
    private final List<EntryIngredient> inputs, output;
    private final int fuelAmount;
    private final int temperature;

    public ReactorFuelDisplay(RecipeHolder<ReactorFuelRecipe> holder) {
        this.id = holder.id();
        this.recipe = holder.value();
        this.inputs = EntryIngredients.ofIngredients(recipe.getIngredients());
        this.output = List.of();
        this.fuelAmount = recipe.fuelAmount();
        this.temperature = recipe.temperature();
    }

    @Override
    public Optional<ResourceLocation> getDisplayLocation() {
        return Optional.of(id);
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
    public CategoryIdentifier<ReactorFuelDisplay> getCategoryIdentifier() {
        return ReactorFuelCategory.ID;
    }

    public int getFuelAmount() {
        return fuelAmount;
    }

    public int getTemperature() {
        return temperature;
    }
}
