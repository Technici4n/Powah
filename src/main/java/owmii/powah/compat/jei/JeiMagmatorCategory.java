package owmii.powah.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;
import owmii.powah.compat.common.MagmatorFuel;

public class JeiMagmatorCategory extends AbstractCategory<MagmatorFuel> {
    public static final IRecipeType<MagmatorFuel> TYPE = IRecipeType.create(Powah.MOD_ID, "magmatic", MagmatorFuel.class);

    public JeiMagmatorCategory(IGuiHelper guiHelper) {
        super(guiHelper, Blcks.MAGMATOR.get(Tier.BASIC), Component.translatable("gui.powah.jei.category.magmatic"), 160, 24);
    }

    @Override
    public IRecipeType<MagmatorFuel> getRecipeType() {
        return TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MagmatorFuel recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 5).setStandardSlotBackground().add(recipe.fluid());
    }

    @Override
    public void draw(MagmatorFuel recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        var minecraft = Minecraft.getInstance();
        guiGraphics.text(minecraft.font, recipe.heat() + " FE/100 mb", 27, 9, 0xFF444444, false);
    }
}
