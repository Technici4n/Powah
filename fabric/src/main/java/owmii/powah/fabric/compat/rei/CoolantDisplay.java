package owmii.powah.fabric.compat.rei;

import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluid;
import owmii.powah.api.PowahAPI;

import java.util.*;
import java.util.stream.Collectors;

public class CoolantDisplay implements Display {

    private final List<EntryIngredient> inputs;
    private final int coldness;

    public CoolantDisplay(Recipe recipe) {
        this.inputs = List.of(
                EntryIngredients.of(recipe.getFluid()),
                !Items.BUCKET.equals(recipe.bucket) ? EntryIngredients.of(recipe.getBucket()) : EntryIngredient.of()
        );
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
    public CategoryIdentifier<CoolantDisplay> getCategoryIdentifier() {
        return CoolantCategory.ID;
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
                if (stack.getItem() instanceof BucketItem bucket && !(stack.getItem() instanceof MobBucketItem)) {
                    Fluid fluid = bucket.content;
                    if (PowahAPI.getCoolant(fluid) != 0) {
                        recipes.add(new Recipe(bucket, PowahAPI.getCoolant(fluid)));
                    }
                }
            });

            List<Fluid> fluids = PowahAPI.COOLANT_FLUIDS.keySet().stream().flatMap(f -> Registry.FLUID.getOptional(f).stream()).collect(Collectors.toCollection(ArrayList::new));
            recipes.forEach(recipe -> {
                fluids.remove(recipe.fluid);
            });

            return recipes;
        }
    }

    public static class Recipe {
        private final Fluid fluid;
        private final BucketItem bucket;
        private final int coldness;

        public Recipe(BucketItem bucket, int coldness) {
            this.bucket = bucket;
            this.fluid = bucket.content;
            this.coldness = coldness;
        }

        public Recipe(Fluid fluid, int coldness) {
            this.bucket = (BucketItem) Items.BUCKET;
            this.fluid = fluid;
            this.coldness = coldness;
        }

        public BucketItem getBucket() {
            return this.bucket;
        }

        public Fluid getFluid() {
            return this.fluid;
        }

        public int getColdness() {
            return this.coldness;
        }
    }
}
