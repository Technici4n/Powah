package owmii.powah.forge.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Blcks;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SolidCoolantCategory implements IRecipeCategory<SolidCoolantCategory.Recipe> {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");
    public static final ResourceLocation ID = new ResourceLocation(Powah.MOD_ID, "solid.coolants");
    private final IDrawable background;
    private final IDrawable icon;

    public SolidCoolantCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GUI_BACK, 0, 0, 160, 24).addPadding(1, 0, 0, 0).build();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Blcks.DRY_ICE.get()));
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
    public Component getTitle() {
        return new TranslatableComponent("gui.powah.jei.category.solid.coolant");
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
    public void draw(Recipe recipe, PoseStack matrix, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.font.draw(matrix, I18n.get("info.lollipop.amount") + ": " + I18n.get("info.lollipop.mb", recipe.amount), 30.0F, 3.0F, 0x444444);
        minecraft.font.draw(matrix, I18n.get("info.lollipop.temperature") + ": " + I18n.get("info.lollipop.temperature.c", "" + ChatFormatting.DARK_AQUA + recipe.coldness), 30.0F, 15.0F, 0x444444);
    }

    public static class Maker {
        public static List<Recipe> getBucketRecipes(IIngredientManager ingredientManager) {
            List<Recipe> recipes = new ArrayList<>();
            for (var stack : ingredientManager.getAllIngredients(VanillaTypes.ITEM)) {
                if (PowahAPI.SOLID_COOLANTS.containsKey(stack.getItem().getRegistryName())) {
                    Pair<Integer, Integer> pr = PowahAPI.getSolidCoolant(stack.getItem());
                    recipes.add(new Recipe(stack, pr.getLeft(), pr.getRight()));
                }
            }
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
