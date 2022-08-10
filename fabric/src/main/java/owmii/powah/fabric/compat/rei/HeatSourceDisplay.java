package owmii.powah.fabric.compat.rei;

import dev.architectury.fluid.FluidStack;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class HeatSourceDisplay implements Display {

    private final List<EntryIngredient> inputs;

    private final int heat;

    public HeatSourceDisplay(Recipe recipe) {
        if (recipe.fluid == null) {
            inputs = List.of(EntryIngredients.of(recipe.getBlock()));
        } else {
            inputs = List.of(EntryIngredients.of(recipe.fluid));
        }
        this.heat = recipe.getHeat();
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

    public static class Maker {
        public static List<Recipe> getBucketRecipes() {
            Collection<ItemStack> allItemStacks = EntryRegistry.getInstance().getEntryStacks()
                    .filter(stack -> Objects.equals(stack.getType(), VanillaEntryTypes.ITEM))
                    .<ItemStack>map(EntryStack::castValue).toList();
            List<Recipe> recipes = new ArrayList<>();
            Powah.LOGGER.debug("HEAT SOURCE RECIPE ALL: [" + PowahAPI.HEAT_SOURCES.entrySet().stream()
                    .map(e -> e.getKey() + " -> " + e.getValue())
                    .collect(Collectors.joining(", ")) + "]");
            allItemStacks.forEach(stack -> {
                if (stack.getItem() instanceof BlockItem) {
                    BlockItem item = (BlockItem) stack.getItem();
                    Block block = item.getBlock();
                    if (PowahAPI.HEAT_SOURCES.containsKey(block)) {
                        recipes.add(new Recipe(block, PowahAPI.getHeatSource(block)));
                    }
                }
            });

            Collection<FluidStack> allIngredients = EntryRegistry.getInstance().getEntryStacks()
                    .filter(stack -> Objects.equals(stack.getType(), VanillaEntryTypes.FLUID))
                    .<FluidStack>map(EntryStack::castValue).toList();;

            allIngredients.forEach(fluidStack -> {
                if (!fluidStack.isEmpty()) {
                    Block block = fluidStack.getFluid().defaultFluidState().createLegacyBlock().getBlock();
                    if (PowahAPI.HEAT_SOURCES.containsKey(block)) {
                        recipes.add(new Recipe(block, PowahAPI.getHeatSource(block)));
                    }
                }
            });

            return recipes;
        }
    }

    public static class Recipe {
        private final Block block;
        private final int heat;

        @Nullable
        private final Fluid fluid;

        public Recipe(Block block, int heat) {
            if (block instanceof LiquidBlock liquidBlock) {
                this.fluid = liquidBlock.fluid;
            } else {
                this.fluid = null;
            }
            this.block = block;
            this.heat = heat;
            Powah.LOGGER.debug("HEAT SOURCE RECIPE INIT: " + this);
        }

        @Nullable
        public Fluid getFluid() {
            return this.fluid;
        }

        public Block getBlock() {
            return this.block;
        }

        public int getHeat() {
            return this.heat;
        }

        @Override
        public String toString() {
            return "HeatSourceRecipe{" + Registry.BLOCK.getKey(block) + (fluid != null ? " (fluid " + Registry.FLUID.getKey(fluid) + ")" : "") + " -> " + heat + "}";
        }
    }
}
