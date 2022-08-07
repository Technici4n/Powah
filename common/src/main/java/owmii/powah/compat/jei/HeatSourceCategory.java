package owmii.powah.compat.jei;

import dev.architectury.fluid.FluidStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import owmii.powah.EnvHandler;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;

import javax.annotation.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class HeatSourceCategory implements IRecipeCategory<HeatSourceCategory.Recipe> {
    public static final RecipeType<Recipe> TYPE = RecipeType.create(Powah.MOD_ID, "heat_source", Recipe.class);

    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");
    private final IDrawable background;
    private final IDrawable icon;

    public HeatSourceCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GUI_BACK, 0, 0, 160, 24).addPadding(1, 0, 0, 0).build();
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.MAGMA_BLOCK));

    }

    @Override
    public RecipeType<Recipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.powah.jei.category.heat.sources");
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
        var input = builder.addSlot(RecipeIngredientRole.INPUT, 4, 5);

        if (recipe.fluid == null) {
            input.addItemStack(new ItemStack(recipe.getBlock()));
        } else {
            input.addFluidStack(recipe.fluid, FluidStack.bucketAmount());
        }
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrix, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.font.draw(matrix, ChatFormatting.DARK_GRAY + I18n.get("info.lollipop.temperature") + ": " + ChatFormatting.RESET + I18n.get("info.lollipop.temperature.c", recipe.heat), 30.0F, 9.0F, 0xc43400);
    }

    public static class Maker {
        public static List<Recipe> getBucketRecipes(IIngredientManager ingredientManager) {
            Collection<ItemStack> allItemStacks = ingredientManager.getAllIngredients(VanillaTypes.ITEM_STACK);
            List<Recipe> recipes = new ArrayList<>();
            Powah.LOGGER.debug("HEAT SOURCE RECIPE ALL: [" + PowahAPI.HEAT_SOURCES.entrySet().stream()
                    .map(e -> e.getKey() + " -> " + e.getValue())
                    .collect(Collectors.joining(", ")) + "]");
            allItemStacks.forEach(stack -> {
                if (stack.getItem() instanceof BlockItem) {
                    BlockItem item = (BlockItem) stack.getItem();
                    Block block = item.getBlock();
                    if (PowahAPI.HEAT_SOURCES.containsKey(block)) {
                        recipes.add(new Recipe(block, PowahAPI.getHeatSource(block)));
                    }
                }
            });

            EnvHandler.INSTANCE.getAllFluidIngredients(ingredientManager).forEach(fluidStack -> {
                if (!fluidStack.isEmpty()) {
                    Block block = fluidStack.getFluid().defaultFluidState().createLegacyBlock().getBlock();
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
            if (block instanceof LiquidBlock) {
                this.fluid = ((LiquidBlock) block).fluid;
            } else {
                this.fluid = null;
            }
            this.block = block;
            this.heat = heat;
            Powah.LOGGER.debug("HEAT SOURCE RECIPE INIT: " + this);
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

        @Override
        public String toString() {
            var blockId = Registry.BLOCK.getKey(block);
            var fluidId = fluid != null ? Registry.FLUID.getKey(fluid) : null;

            return "HeatSourceRecipe{" + blockId + (fluid != null ? " (fluid " + fluidId + ")" : "") + " -> " + heat + "}";
        }
    }
}
