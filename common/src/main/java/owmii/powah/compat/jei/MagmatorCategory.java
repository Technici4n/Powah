package owmii.powah.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.fluid.FluidStack;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;

public class MagmatorCategory implements IRecipeCategory<MagmatorCategory.Recipe> {
    public static final RecipeType<Recipe> TYPE = RecipeType.create(Powah.MOD_ID, "magmatic", Recipe.class);

    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");
    private final IDrawable background;
    private final IDrawable icon;

    public MagmatorCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GUI_BACK, 0, 0, 160, 24).addPadding(1, 0, 0, 0).build();
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Blcks.MAGMATOR.get(Tier.BASIC)));

    }

    @Override
    public RecipeType<Recipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.powah.jei.category.magmatic");
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
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 5)
                .addFluidStack(recipe.fluid(), FluidStack.bucketAmount());
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrix, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.font.draw(matrix, recipe.heat + " FE/100 mb", 27.0F, 9.0F, 0x444444);
    }

    public static List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        for (var entry : Registry.FLUID.entrySet()) {
            var fluidId = entry.getKey().location();
            var heat = PowahAPI.MAGMATIC_FLUIDS.get(fluidId);
            if (heat != null) {
                recipes.add(new Recipe(entry.getValue(), heat));
            }
        }

        recipes.sort(Comparator.comparingInt(Recipe::heat));
        return recipes;
    }

    public record Recipe(Fluid fluid, int heat) {
    }
}
