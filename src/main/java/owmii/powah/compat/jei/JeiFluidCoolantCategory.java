package owmii.powah.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import owmii.powah.Powah;
import owmii.powah.compat.common.FluidCoolant;

public class JeiFluidCoolantCategory extends AbstractCategory<FluidCoolant> {
    public static final RecipeType<FluidCoolant> TYPE = RecipeType.create(Powah.MOD_ID, "coolant", FluidCoolant.class);

    public JeiFluidCoolantCategory(IGuiHelper guiHelper) {
        super(guiHelper, Items.WATER_BUCKET, Component.translatable("gui.powah.jei.category.coolant"),
                guiHelper.drawableBuilder(Assets.MISC, 0, 0, 160, 24).addPadding(1, 0, 0, 0).build());
    }

    @Override
    public RecipeType<FluidCoolant> getRecipeType() {
        return TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FluidCoolant recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 5)
                .addFluidStack(recipe.fluid());
    }

    @Override
    public void draw(FluidCoolant recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        var minecraft = Minecraft.getInstance();
        guiGraphics.drawString(minecraft.font, I18n.get("info.lollipop.temperature") + ": "
                + I18n.get("info.lollipop.temperature.c", "" + ChatFormatting.DARK_AQUA + recipe.coldness()), 30, 9, 0x444444, false);
    }
}
