package owmii.powah.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HeatSourceCategory implements IRecipeCategory<HeatSourceCategory.Recipe> {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");
    public static final ResourceLocation ID = new ResourceLocation(Powah.MOD_ID, "heat.sources");
    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;

    public HeatSourceCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GUI_BACK, 0, 0, 160, 24).addPadding(1, 0, 0, 0).build();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Blocks.MAGMA_BLOCK));
        this.localizedName = I18n.format("gui.powah.jei.category.heat.sources");

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
        if (recipe.fluid == null) {
            ingredients.setInput(VanillaTypes.ITEM, new ItemStack(recipe.getBlock()));
        } else {
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
    public void draw(Recipe recipe, MatrixStack matrix, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.fontRenderer.func_238405_a_(matrix, TextFormatting.DARK_GRAY + I18n.format("info.lollipop.temperature", "" + TextFormatting.RESET + recipe.heat), 30.0F, 9.0F, 0xc43400);
    }

    public static class Maker {
        public static List<Recipe> getBucketRecipes(IIngredientManager ingredientManager) {
            Collection<ItemStack> allItemStacks = ingredientManager.getAllIngredients(VanillaTypes.ITEM);
            List<Recipe> recipes = new ArrayList<>();
            allItemStacks.forEach(stack -> {
                if (stack.getItem() instanceof BlockItem) {
                    BlockItem item = (BlockItem) stack.getItem();
                    Block block = item.getBlock();
                    if (PowahAPI.HEAT_SOURCES.containsKey(block)) {
                        recipes.add(new Recipe(block, PowahAPI.getHeatSource(block)));
                    }
                }
            });

            Collection<FluidStack> allIngredients = ingredientManager.getAllIngredients(VanillaTypes.FLUID);

            allIngredients.forEach(fluidStack -> {
                if (!fluidStack.isEmpty()) {
                    Block block = fluidStack.getFluid().getDefaultState().getBlockState().getBlock();
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
            if (block instanceof FlowingFluidBlock) {
                this.fluid = ((FlowingFluidBlock) block).getFluid();
            } else {
                this.fluid = null;
            }
            this.block = block;
            this.heat = heat;
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
    }
}
