package owmii.powah.forge.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class HeatSourceCategory implements IRecipeCategory<HeatSourceCategory.Recipe> {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");
    public static final ResourceLocation ID = new ResourceLocation(Powah.MOD_ID, "heat.sources");
    private final IDrawable background;
    private final IDrawable icon;

    public HeatSourceCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GUI_BACK, 0, 0, 160, 24).addPadding(1, 0, 0, 0).build();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Blocks.MAGMA_BLOCK));

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
        return new TranslatableComponent("gui.powah.jei.category.heat.sources");
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
        if (recipe.block != null) {
            ingredients.setInput(VanillaTypes.ITEM, recipe.block);
        }
        if (recipe.fluid != null) {
            ingredients.setInput(VanillaTypes.FLUID, new FluidStack(recipe.fluid, 1000));
        }
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, Recipe recipe, IIngredients ingredients) {
        if (recipe.fluid == null) {
            IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();
            itemStacks.init(0, true, 3, 4);
            itemStacks.set(ingredients);
        } else {
            IGuiFluidStackGroup fluidStack = iRecipeLayout.getFluidStacks();
            fluidStack.init(0, true, 4, 5);
            fluidStack.set(ingredients);
        }
    }

    @Override
    public void draw(Recipe recipe, PoseStack matrix, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.font.draw(matrix, ChatFormatting.DARK_GRAY + I18n.get("info.lollipop.temperature") + ": " + ChatFormatting.RESET + I18n.get("info.lollipop.temperature.c", recipe.heat), 30.0F, 9.0F, 0xc43400);
    }

    public static List<Recipe> getRecipes(IIngredientManager ingredientManager) {
        Collection<ItemStack> allItemStacks = ingredientManager.getAllIngredients(VanillaTypes.ITEM);
        List<Recipe> recipes = new ArrayList<>();

        // Block heat sources
        for (var entry : Registry.BLOCK.entrySet()) {
            var blockItem = Item.byBlock(entry.getValue());
            if (blockItem == Items.AIR) {
                continue; // Can't handle blocks that have no equivalent block item for displaying them
            }

            var blockId = entry.getKey().location();
            var heat = PowahAPI.HEAT_SOURCES.getOrDefault(blockId, 0);
            if (heat != 0) {
                recipes.add(new Recipe(blockItem.getDefaultInstance(), null, heat));
            }
        }

        // Fluid heat sources
        for (var entry : Registry.FLUID.entrySet()) {
            var fluidId = entry.getKey().location();
            var heat = PowahAPI.HEAT_SOURCES.getOrDefault(fluidId, 0);
            if (heat != 0) {
                recipes.add(new Recipe(null, entry.getValue(), heat));
            }
        }

        // Sort by heat ascending
        recipes.sort(Comparator.comparingInt(Recipe::heat));

        return recipes;
    }

    public record Recipe(@Nullable ItemStack block, @Nullable Fluid fluid, int heat) {
    }
}
