package owmii.powah.block.energizing;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import owmii.powah.api.recipe.energizing.EnergizingRecipe;
import owmii.powah.api.recipe.energizing.IEnergizingRecipe;
import owmii.powah.api.recipe.energizing.IEnergizingRegistry;
import owmii.powah.block.IBlocks;
import owmii.powah.item.IItems;

import java.util.HashSet;
import java.util.Set;

public class EnergizingRecipes implements IEnergizingRegistry {
    @Override
    public Set<IEnergizingRecipe> getRecipes() {
        final Set<IEnergizingRecipe> recipes = new HashSet<>();
        recipes.add(new EnergizingRecipe(new ItemStack(IItems.ENERGIZED_STEEL, 2), 10000, Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT)), Ingredient.fromStacks(new ItemStack(Items.GOLD_INGOT))));
        recipes.add(new EnergizingRecipe(new ItemStack(IItems.BLAZING_CRYSTAL), 90000, Ingredient.fromStacks(new ItemStack(Items.BLAZE_ROD))));
        recipes.add(new EnergizingRecipe(new ItemStack(IItems.BLAZING_CRYSTAL), 120000, Ingredient.fromStacks(new ItemStack(Items.BLAZE_POWDER)), Ingredient.fromStacks(new ItemStack(Items.BLAZE_POWDER)), Ingredient.fromStacks(new ItemStack(Items.BLAZE_POWDER)), Ingredient.fromStacks(new ItemStack(Items.BLAZE_POWDER))));
        recipes.add(new EnergizingRecipe(new ItemStack(IItems.NIOTIC_CRYSTAL), 300000, Ingredient.fromStacks(new ItemStack(Items.DIAMOND))));
        recipes.add(new EnergizingRecipe(new ItemStack(IItems.SPIRITED_CRYSTAL), 1000000, Ingredient.fromStacks(new ItemStack(Items.EMERALD))));
        recipes.add(new EnergizingRecipe(new ItemStack(IItems.NITRO_CRYSTAL, 16), 20000000, Ingredient.fromItems(Items.NETHER_STAR), Ingredient.fromItems(Blocks.REDSTONE_BLOCK), Ingredient.fromItems(Blocks.REDSTONE_BLOCK), Ingredient.fromItems(IBlocks.BLAZING_CRYSTAL)));
        recipes.add(new EnergizingRecipe(new ItemStack(IItems.CHARGED_SNOWBALL), 500000, Ingredient.fromStacks(new ItemStack(Items.SNOWBALL))));
        recipes.add(new EnergizingRecipe(new ItemStack(IItems.ENDER_CORE), 50000, Ingredient.fromStacks(new ItemStack(Items.ENDER_EYE)), Ingredient.fromStacks(new ItemStack(IItems.DIELECTRIC_CASING)), Ingredient.fromStacks(new ItemStack(IItems.CAPACITOR_BASIC_TINY))));
        recipes.add(new EnergizingRecipe(new ItemStack(IBlocks.DRY_ICE), 10000, Ingredient.fromStacks(new ItemStack(Blocks.BLUE_ICE)), Ingredient.fromStacks(new ItemStack(Blocks.BLUE_ICE))));
        return recipes;
    }
}