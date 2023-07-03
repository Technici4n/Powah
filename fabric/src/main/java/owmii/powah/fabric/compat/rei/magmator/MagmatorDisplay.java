package owmii.powah.fabric.compat.rei.magmator;

import dev.architectury.hooks.fluid.FluidBucketHooks;
import java.util.*;
import java.util.stream.Collectors;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import owmii.powah.api.PowahAPI;

public class MagmatorDisplay implements Display {
    private final List<EntryIngredient> inputs;

    private final int heat;

    public MagmatorDisplay(Recipe recipe) {
        this.inputs = List.of(
                EntryIngredients.of(recipe.getFluid()),
                !Items.BUCKET.equals(recipe.getBucket()) ? EntryIngredients.of(recipe.getBucket()) : EntryIngredient.of());
        this.heat = recipe.getHeat();
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

    public static class Maker {
        public static List<Recipe> getBucketRecipes() {
            Collection<ItemStack> allItemStacks = EntryRegistry.getInstance().getEntryStacks()
                    .filter(stack -> Objects.equals(stack.getType(), VanillaEntryTypes.ITEM))
                    .<ItemStack>map(EntryStack::castValue).toList();
            List<Recipe> recipes = new ArrayList<>();

            allItemStacks.forEach(stack -> {
                if (stack.getItem() instanceof BucketItem bucket) {
                    Fluid fluid = FluidBucketHooks.getFluid(bucket);
                    if (PowahAPI.getMagmaticFluidHeat(fluid) != 0) {
                        recipes.add(new Recipe(bucket, PowahAPI.getMagmaticFluidHeat(fluid)));
                    }
                }
            });

            List<Fluid> fluids = PowahAPI.MAGMATIC_FLUIDS.keySet().stream().flatMap(f -> BuiltInRegistries.FLUID.getOptional(f).stream())
                    .collect(Collectors.toCollection(ArrayList::new));
            recipes.forEach(recipe -> {
                fluids.remove(recipe.fluid);
            });

            fluids.forEach(fluid -> {
                recipes.add(new Recipe(fluid, PowahAPI.getMagmaticFluidHeat(fluid)));
            });

            return recipes;
        }
    }

    public static class Recipe {
        private final Fluid fluid;
        private final BucketItem bucket;
        private final int heat;

        public Recipe(BucketItem bucket, int heat) {
            this.bucket = bucket;
            this.fluid = FluidBucketHooks.getFluid(bucket);
            this.heat = heat;
        }

        public Recipe(Fluid fluid, int heat) {
            this.bucket = (BucketItem) Items.BUCKET;
            this.fluid = fluid;
            this.heat = heat;
        }

        public BucketItem getBucket() {
            return this.bucket;
        }

        public Fluid getFluid() {
            return this.fluid;
        }

        public int getHeat() {
            return this.heat;
        }
    }
}
