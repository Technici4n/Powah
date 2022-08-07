package owmii.powah.compat.jei.magmator;

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
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
        var slot = builder.addSlot(RecipeIngredientRole.INPUT, 4, 5)
                .addFluidStack(recipe.getFluid(), FluidStack.bucketAmount());

        if (!Items.BUCKET.equals(recipe.bucket)) {
            slot.addItemStack(new ItemStack(recipe.bucket));
        }
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrix, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.font.draw(matrix, recipe.heat + " FE/100 mb", 27.0F, 9.0F, 0x444444);
    }

    public static class Maker {
        public static List<Recipe> getBucketRecipes(IIngredientManager ingredientManager) {
            Collection<ItemStack> allItemStacks = ingredientManager.getAllIngredients(VanillaTypes.ITEM_STACK);
            List<Recipe> recipes = new ArrayList<>();

            allItemStacks.forEach(stack -> {
                if (stack.getItem() instanceof BucketItem bucket) {
                    Fluid fluid = bucket.content;
                    if (PowahAPI.getMagmaticFluidHeat(fluid) != 0) {
                        recipes.add(new Recipe(bucket, PowahAPI.getMagmaticFluidHeat(fluid)));
                    }
                }
            });

            List<Fluid> fluids = PowahAPI.MAGMATIC_FLUIDS.keySet().stream().flatMap(f -> Registry.FLUID.getOptional(f).stream()).collect(Collectors.toCollection(ArrayList::new));
            recipes.forEach(recipe -> {
                fluids.remove(recipe.fluid);
            });

            fluids.forEach(fluid -> {
                recipes.add(new Recipe(fluid, PowahAPI.getMagmaticFluidHeat(fluid)));
            });

            return recipes;
        }
    }

    public static class Recipe {
        private final Fluid fluid;
        private final BucketItem bucket;
        private final int heat;

        public Recipe(BucketItem bucket, int heat) {
            this.bucket = bucket;
            this.fluid = bucket.content;
            this.heat = heat;
        }

        public Recipe(Fluid fluid, int heat) {
            this.bucket = (BucketItem) Items.BUCKET;
            this.fluid = fluid;
            this.heat = heat;
        }

        public BucketItem getBucket() {
            return this.bucket;
        }

        public Fluid getFluid() {
            return this.fluid;
        }

        public int getHeat() {
            return this.heat;
        }
    }
}
