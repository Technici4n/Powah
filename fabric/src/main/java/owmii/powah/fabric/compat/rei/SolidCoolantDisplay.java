package owmii.powah.fabric.compat.rei;

import java.util.*;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import owmii.powah.api.PowahAPI;

public class SolidCoolantDisplay implements Display {

    private final List<EntryIngredient> inputs;

    private final int amount, coldness;

    public SolidCoolantDisplay(Recipe recipe) {
        this.inputs = List.of(EntryIngredients.of(recipe.stack));
        this.amount = recipe.getAmount();
        this.coldness = recipe.getColdness();
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

    public static class Maker {
        public static List<Recipe> getBucketRecipes() {
            Collection<ItemStack> allItemStacks = EntryRegistry.getInstance().getEntryStacks()
                    .filter(stack -> Objects.equals(stack.getType(), VanillaEntryTypes.ITEM))
                    .<ItemStack>map(EntryStack::castValue).toList();
            List<Recipe> recipes = new ArrayList<>();
            allItemStacks.forEach(stack -> {
                if (PowahAPI.SOLID_COOLANTS.containsKey(BuiltInRegistries.ITEM.getKey(stack.getItem()))) {
                    Pair<Integer, Integer> pr = PowahAPI.getSolidCoolant(stack.getItem());
                    recipes.add(new Recipe(stack, pr.getLeft(), pr.getRight()));
                }
            });
            return recipes;
        }
    }

    public static class Recipe {
        private final ItemStack stack;
        private final int amount;
        private final int coldness;

        public Recipe(ItemStack stack, int amount, int coldness) {
            this.stack = stack;
            this.amount = amount;
            this.coldness = coldness;
        }

        public ItemStack getStack() {
            return this.stack;
        }

        public int getAmount() {
            return this.amount;
        }

        public int getColdness() {
            return this.coldness;
        }
    }
}
