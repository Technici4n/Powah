package owmii.powah.compat.rei.magmator;

import java.util.Collections;
import java.util.List;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.BucketItem;
import owmii.powah.compat.common.MagmatorFuel;

public class MagmatorDisplay implements Display {
    private final List<EntryIngredient> inputs;

    private final int heat;

    public MagmatorDisplay(MagmatorFuel recipe) {
        var inputBuilder = EntryIngredient.builder()
                .add(EntryStacks.of(recipe.fluid()));
        for (BucketItem bucket : recipe.buckets()) {
            inputBuilder.add(EntryStacks.of(bucket));
        }
        this.inputs = List.of(inputBuilder.build());
        this.heat = recipe.heat();
    }

    public int getHeat() {
        return heat;
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
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return MagmatorCategory.ID;
    }
}
