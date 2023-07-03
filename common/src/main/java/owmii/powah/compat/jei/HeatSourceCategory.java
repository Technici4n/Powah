package owmii.powah.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.fluid.FluidStack;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;

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

        if (recipe.block() != null) {
            input.addItemStack(recipe.block());
        }
        if (recipe.fluid != null) {
            input.addFluidStack(recipe.fluid, FluidStack.bucketAmount());
        }
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.drawString(minecraft.font, ChatFormatting.DARK_GRAY + I18n.get("info.lollipop.temperature") + ": "
                + ChatFormatting.RESET + I18n.get("info.lollipop.temperature.c", recipe.heat), 30, 9, 0xc43400, false);
    }

    public static List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        // Block heat sources. We iterate the item registry in search of block items, because
        // we need an item to show as the ingredient and link it as a JEI ingredient.
        for (var item : BuiltInRegistries.ITEM) {
            if (!(item instanceof BlockItem blockItem)) {
                continue;
            }

            var block = blockItem.getBlock();
            var blockId = BuiltInRegistries.BLOCK.getKey(block);
            var heat = PowahAPI.HEAT_SOURCES.getOrDefault(blockId, 0);
            if (heat != 0) {
                recipes.add(new Recipe(blockItem.getDefaultInstance(), null, heat));
            }
        }

        // Fluid heat sources
        for (var entry : BuiltInRegistries.FLUID.entrySet()) {
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
