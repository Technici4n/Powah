package owmii.powah.forge.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class CoolantCategory implements IRecipeCategory<CoolantCategory.Recipe> {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");
    public static final ResourceLocation ID = new ResourceLocation(Powah.MOD_ID, ".coolant");
    private final IDrawable background;
    private final IDrawable icon;

    public CoolantCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GUI_BACK, 0, 0, 160, 24).addPadding(1, 0, 0, 0).build();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Items.WATER_BUCKET));
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
        return new TranslatableComponent("gui.powah.jei.category.coolant");
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
        ingredients.setInput(VanillaTypes.FLUID, new FluidStack(recipe.fluid(), 1000));
        ingredients.setInputs(VanillaTypes.ITEM_STACK, recipe.buckets());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, Recipe recipe, IIngredients ingredients) {
        IGuiFluidStackGroup fluidStack = iRecipeLayout.getFluidStacks();
        fluidStack.init(0, true, 4, 5);
        fluidStack.set(ingredients);
    }

    @Override
    public void draw(Recipe recipe, PoseStack matrix, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.font.draw(matrix, I18n.get("info.lollipop.temperature") + ": " + I18n.get("info.lollipop.temperature.c", "" + ChatFormatting.DARK_AQUA + recipe.coldness), 30.0F, 9.0F, 0x444444);
    }

    public static List<Recipe> getRecipes(IIngredientManager ingredientManager) {
        Map<Fluid, List<ItemStack>> fluidsAndBuckets = new IdentityHashMap<>();

        // Find all fluids
        for (var fluid : Registry.FLUID) {
            if (PowahAPI.getCoolant(fluid) != 0) {
                fluidsAndBuckets.put(fluid, new ArrayList<>());
            }
        }

        // Find all associated bucket items / tanks
        for (var stack : ingredientManager.getAllIngredients(VanillaTypes.ITEM_STACK)) {
            if (stack.getItem() instanceof BucketItem bucketItem) {
                var buckets = fluidsAndBuckets.get(bucketItem.getFluid());
                if (buckets != null) {
                    buckets.add(stack);
                }
            }
        }

        List<Recipe> recipes = new ArrayList<>();
        for (var entry : fluidsAndBuckets.entrySet()) {
            var temperature = PowahAPI.getCoolant(entry.getKey());
            if (temperature != 0) {
                recipes.add(new Recipe(entry.getKey(), entry.getValue(), temperature));
            }
        }

        // Sort from bad to good
        recipes.sort(Comparator.comparingInt(Recipe::coldness).reversed());

        return recipes;
    }

    public record Recipe(Fluid fluid, List<ItemStack> buckets, int coldness) {
    }
}
