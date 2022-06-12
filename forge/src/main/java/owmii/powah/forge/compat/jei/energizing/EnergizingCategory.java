package owmii.powah.forge.compat.jei.energizing;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import owmii.powah.lib.util.Util;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.block.energizing.EnergizingRecipe;

public class EnergizingCategory implements IRecipeCategory<EnergizingRecipe> {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/energizing.png");
    public static final ResourceLocation ID = new ResourceLocation(Powah.MOD_ID, "energizing");
    private final IDrawable background;
    private final IDrawable icon;

    public EnergizingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GUI_BACK, 0, 0, 160, 38).addPadding(1, 0, 0, 0).build();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Blcks.ENERGIZING_ORB.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends EnergizingRecipe> getRecipeClass() {
        return EnergizingRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("gui.powah.jei.category.energizing");
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
    public void setIngredients(EnergizingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, EnergizingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
        int size = recipe.getIngredients().size();
        for (int i = 0; i < size; i++) {
            stackGroup.init(i, true, (i * 20) + 3, 4);
        }
        stackGroup.init(size, false, 136, 4);
        stackGroup.set(ingredients);
    }

    @Override
    public void draw(EnergizingRecipe recipe, PoseStack matrix, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.font.draw(matrix, I18n.get("info.lollipop.fe", Util.addCommas(recipe.getEnergy())), 2.0F, 29.0F, 0x444444);
    }
}
