package owmii.powah.block.energizing;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import owmii.powah.api.recipe.energizing.IEnergizingRecipe;
import owmii.powah.api.recipe.energizing.IEnergizingRegistry;
import owmii.powah.item.IItems;

import java.util.HashSet;
import java.util.Set;

public class EnergizingRecipes implements IEnergizingRegistry {
    private static final EnergizingRecipes INSTANCE = new EnergizingRecipes();
    private final Set<IEnergizingRecipe> recipes = new HashSet<>();

    public static EnergizingRecipes all() {
        INSTANCE.addRecipe(new ItemStack(IItems.ENERGISED_STEEL, 2), 4000, Ingredient.fromTag(Tags.Items.INGOTS_IRON), Ingredient.fromTag(Tags.Items.INGOTS_GOLD));
        INSTANCE.addRecipe(new ItemStack(IItems.BLAZING_CRYSTAL), 90000, Ingredient.fromStacks(new ItemStack(Items.BLAZE_ROD)));
        INSTANCE.addRecipe(new ItemStack(IItems.BLAZING_CRYSTAL), 120000, Ingredient.fromStacks(new ItemStack(Items.BLAZE_POWDER)), Ingredient.fromStacks(new ItemStack(Items.BLAZE_POWDER)), Ingredient.fromStacks(new ItemStack(Items.BLAZE_POWDER)), Ingredient.fromStacks(new ItemStack(Items.BLAZE_POWDER)));
        INSTANCE.addRecipe(new ItemStack(IItems.NIOTIC_CRYSTAL), 300000, Ingredient.fromTag(Tags.Items.GEMS_DIAMOND));
        INSTANCE.addRecipe(new ItemStack(IItems.SPIRITED_CRYSTAL), 1000000, Ingredient.fromTag(Tags.Items.GEMS_EMERALD));
        INSTANCE.addRecipe(new ItemStack(IItems.CHARGED_SNOWBALL), 500000, Ingredient.fromStacks(new ItemStack(Items.SNOWBALL)));
        INSTANCE.addRecipe(new ItemStack(IItems.ENDER_CORE), 50000, Ingredient.fromStacks(new ItemStack(Items.ENDER_EYE)), Ingredient.fromStacks(new ItemStack(IItems.DIELECTRIC_CASING)), Ingredient.fromStacks(new ItemStack(IItems.CAPACITOR_BASIC_TINY)));
        return INSTANCE;
    }

    @Override
    public Set<IEnergizingRecipe> getRecipes() {
        return INSTANCE.recipes;
    }
}