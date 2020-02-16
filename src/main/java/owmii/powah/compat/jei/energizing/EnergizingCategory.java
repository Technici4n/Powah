package owmii.powah.compat.jei.energizing;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import owmii.lib.util.Text;
import owmii.powah.Powah;
import owmii.powah.api.recipe.energizing.EnergizingRecipe;
import owmii.powah.api.recipe.energizing.IEnergizingRecipe;
import owmii.powah.block.IBlocks;

public class EnergizingCategory implements IRecipeCategory<IEnergizingRecipe> {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/energizing.png");
    public static final ResourceLocation ID = new ResourceLocation(Powah.MOD_ID, "energizing");
    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;

    public EnergizingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GUI_BACK, 0, 0, 160, 38).addPadding(1, 0, 0, 0).build();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(IBlocks.ENERGIZING_ORB));
        this.localizedName = I18n.format("gui.powah.jei.category.energizing");
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends IEnergizingRecipe> getRecipeClass() {
        return EnergizingRecipe.class;
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
    public void setIngredients(IEnergizingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IEnergizingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
        int size = recipe.getIngredients().size();
        for (int i = 0; i < size; i++) {
            itemStackGroup.init(i, true, (i * 20) + 3, 4);
        }
        itemStackGroup.init(size, false, 136, 4);
        itemStackGroup.set(ingredients);
    }

    @Override
    public void draw(IEnergizingRecipe recipe, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.fontRenderer.drawString(I18n.format("info.lollipop.energy.fe.2", Text.addCommas(recipe.getEnergy())), 2.0F, 29.0F, 0x484241);
    }
}
