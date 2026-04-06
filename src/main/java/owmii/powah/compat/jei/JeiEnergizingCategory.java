package owmii.powah.compat.jei;

import com.google.common.base.Suppliers;
import java.util.function.Supplier;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import owmii.powah.block.Blcks;
import owmii.powah.block.energizing.EnergizingRecipe;
import owmii.powah.recipe.Recipes;
import owmii.powah.util.Util;

public class JeiEnergizingCategory extends AbstractCategory<RecipeHolder<EnergizingRecipe>> {
    public static final Supplier<IRecipeType<RecipeHolder<EnergizingRecipe>>> TYPE = Suppliers
            .memoize(() -> IRecipeType.create(Recipes.ENERGIZING.get()));

    private final IDrawable arrow;

    public JeiEnergizingCategory(IGuiHelper guiHelper) {
        super(guiHelper, Blcks.ENERGIZING_ORB.get(), Component.translatable("gui.powah.jei.category.energizing"), 160, 38);
        arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public IRecipeType<RecipeHolder<EnergizingRecipe>> getRecipeType() {
        return TYPE.get();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<EnergizingRecipe> recipeHolder, IFocusGroup focuses) {
        var recipe = recipeHolder.value();
        var ingredients = recipe.getIngredients();
        int size = ingredients.size();
        for (int i = 0; i < Math.max(size, 5); i++) {
            var slot = builder.addSlot(RecipeIngredientRole.INPUT, (i * 20) + 4, 5).setStandardSlotBackground();
            if (i < ingredients.size()) {
                slot.add(ingredients.get(i));
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 137, 5).setOutputSlotBackground().add(recipe.getResultItem().create());
    }

    @Override
    public void draw(RecipeHolder<EnergizingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX,
            double mouseY) {
        var recipe = recipeHolder.value();
        var minecraft = Minecraft.getInstance();
        guiGraphics.text(minecraft.font, I18n.get("info.lollipop.fe", Util.addCommas(recipe.getEnergy())), 2, 29, 0xFF444444, false);

        arrow.draw(guiGraphics, 105, 5);
    }
}
