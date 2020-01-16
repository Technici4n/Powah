package owmii.powah.compat.jei.reactor;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.Pair;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.generator.reactor.Reactors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReactorSCoolantCategory implements IRecipeCategory<ReactorSCoolantCategory.Recipe> {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");
    public static final ResourceLocation ID = new ResourceLocation(Powah.MOD_ID, "solid.coolants");
    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;

    public ReactorSCoolantCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GUI_BACK, 0, 0, 160, 24).addPadding(1, 0, 0, 0).build();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Reactors.BASIC.get()));
        this.localizedName = I18n.format("info.powah.solid.coolant");

    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends Recipe> getRecipeClass() {
        return Recipe.class;
    }

    @Override
    public String getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(Recipe recipe, IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, recipe.stack);
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, Recipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();
        itemStacks.init(0, true, 3, 4);
        itemStacks.set(ingredients);

    }

    @Override
    public void draw(Recipe recipe, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.fontRenderer.drawString(TextFormatting.DARK_GRAY + I18n.format("info.powah.amount", "" + TextFormatting.RESET + recipe.amount), 30.0F, 3.0F, 0x555555);
        minecraft.fontRenderer.drawString(TextFormatting.DARK_GRAY + I18n.format("info.powah.coldness", "" + TextFormatting.RESET + recipe.coldness), 30.0F, 15.0F, 43690);
    }

    public static class Maker {
        public static List<Recipe> getBucketRecipes(IIngredientManager ingredientManager) {
            Collection<ItemStack> allItemStacks = ingredientManager.getAllIngredients(VanillaTypes.ITEM);
            List<Recipe> recipes = new ArrayList<>();
            allItemStacks.forEach(stack -> {
                if (PowahAPI.REACTOR_SOLID_COOLANTS.containsKey(stack.getItem().getRegistryName())) {
                    Pair<Integer, Integer> pr = PowahAPI.getReactorSolidCoolant(stack.getItem());
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
