package owmii.powah.compat.jei;

import java.util.List;
import java.util.function.Supplier;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import owmii.powah.block.Blcks;
import owmii.powah.block.energizing.EnergizingRecipe;
import owmii.powah.recipe.Recipes;
import owmii.powah.util.Util;

public class JeiEnergizingCategory extends AbstractCategory<RecipeHolder<EnergizingRecipe>> {
    public static final Supplier<RecipeType<RecipeHolder<EnergizingRecipe>>> TYPE = RecipeType.createFromDeferredVanilla(Recipes.ENERGIZING);

    public JeiEnergizingCategory(IGuiHelper guiHelper) {
        super(guiHelper, Blcks.ENERGIZING_ORB.get(), Component.translatable("gui.powah.jei.category.energizing"),
                guiHelper.drawableBuilder(Assets.ENERGIZING, 0, 0, 160, 38).addPadding(1, 0, 0, 0).build());
    }

    @Override
    public RecipeType<RecipeHolder<EnergizingRecipe>> getRecipeType() {
        return TYPE.get();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<EnergizingRecipe> recipeHolder, IFocusGroup focuses) {
        var recipe = recipeHolder.value();
        var ingredients = recipe.getIngredients();
        int size = ingredients.size();
        for (int i = 0; i < size; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, (i * 20) + 4, 5)
                    .addIngredients(ingredients.get(i));
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 137, 5)
                .addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(RecipeHolder<EnergizingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX,
            double mouseY) {
        var recipe = recipeHolder.value();
        var minecraft = Minecraft.getInstance();
        guiGraphics.drawString(minecraft.font, I18n.get("info.lollipop.fe", Util.addCommas(recipe.getEnergy())), 2, 29, 0x444444, false);
    }

    public static List<RecipeHolder<EnergizingRecipe>> getAllRecipes() {
        var minecraft = Minecraft.getInstance();
        var level = minecraft.level;
        assert level != null;
        var recipeManager = level.getRecipeManager();

        return recipeManager.getAllRecipesFor(Recipes.ENERGIZING.get());
    }
}
