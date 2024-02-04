package owmii.powah.compat.rei;

import java.util.List;
import java.util.Optional;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import owmii.powah.recipe.ReactorFuel;

public class ReactorFuelDisplay implements Display {
    private final ResourceLocation id;
    private final List<EntryIngredient> inputs, output;
    private final int fuelAmount;
    private final int temperature;

    public ReactorFuelDisplay(ResourceLocation id, ReactorFuel fuel) {
        this.id = id;
        var stack = BuiltInRegistries.ITEM.get(id).getDefaultInstance();
        this.inputs = List.of(EntryIngredients.of(stack));
        this.output = List.of();
        this.fuelAmount = fuel.fuelAmount();
        this.temperature = fuel.temperature();
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
