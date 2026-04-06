package owmii.powah.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.compat.common.SolidCoolant;

public class JeiSolidCoolantCategory extends AbstractCategory<SolidCoolant> {
    public static final IRecipeType<SolidCoolant> TYPE = IRecipeType.create(Powah.MOD_ID, "solid_coolant", SolidCoolant.class);

    public JeiSolidCoolantCategory(IGuiHelper guiHelper) {
        super(guiHelper, Blcks.DRY_ICE.get(), Component.translatable("gui.powah.jei.category.solid.coolant"), 160, 24);
    }

    @Override
    public IRecipeType<SolidCoolant> getRecipeType() {
        return TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SolidCoolant recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 5).setStandardSlotBackground().add(recipe.item());
    }

    @Override
    public void draw(SolidCoolant recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        var minecraft = Minecraft.getInstance();
        guiGraphics.text(minecraft.font, I18n.get("info.lollipop.amount") + ": " + I18n.get("info.lollipop.mb", recipe.amount()), 30, 3,
                0xFF444444,
                false);
        guiGraphics.text(minecraft.font, I18n.get("info.lollipop.temperature") + ": "
                + I18n.get("info.lollipop.temperature.c", "" + ChatFormatting.DARK_AQUA + recipe.temperature()), 30, 15, 0xFF444444, false);
    }
}
