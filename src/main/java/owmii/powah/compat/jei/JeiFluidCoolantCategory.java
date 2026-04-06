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
import net.minecraft.world.item.Items;
import owmii.powah.Powah;
import owmii.powah.compat.common.FluidCoolant;

public class JeiFluidCoolantCategory extends AbstractCategory<FluidCoolant> {
    public static final IRecipeType<FluidCoolant> TYPE = IRecipeType.create(Powah.MOD_ID, "coolant", FluidCoolant.class);

    public JeiFluidCoolantCategory(IGuiHelper guiHelper) {
        super(guiHelper, Items.WATER_BUCKET, Component.translatable("gui.powah.jei.category.coolant"), 160, 24);
    }

    @Override
    public IRecipeType<FluidCoolant> getRecipeType() {
        return TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FluidCoolant recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 5).setStandardSlotBackground().add(recipe.fluid());
    }

    @Override
    public void draw(FluidCoolant recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        var minecraft = Minecraft.getInstance();
        guiGraphics.text(minecraft.font, I18n.get("info.lollipop.temperature") + ": "
                + I18n.get("info.lollipop.temperature.c", "" + ChatFormatting.DARK_AQUA + recipe.coldness()), 30, 9, 0xFF444444, false);
    }
}
