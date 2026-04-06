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
import net.minecraft.world.level.block.Blocks;
import owmii.powah.Powah;
import owmii.powah.compat.common.PassiveHeatSource;

public class JeiHeatSourceCategory extends AbstractCategory<PassiveHeatSource> {
    public static final IRecipeType<PassiveHeatSource> TYPE = IRecipeType.create(Powah.MOD_ID, "heat_source", PassiveHeatSource.class);

    public JeiHeatSourceCategory(IGuiHelper guiHelper) {
        super(guiHelper, Blocks.MAGMA_BLOCK, Component.translatable("gui.powah.jei.category.heat.sources"), 160, 24);
    }

    @Override
    public IRecipeType<PassiveHeatSource> getRecipeType() {
        return TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PassiveHeatSource recipe, IFocusGroup focuses) {
        var input = builder.addSlot(RecipeIngredientRole.INPUT, 4, 5).setStandardSlotBackground();

        if (recipe.block() != null) {
            input.add(recipe.block());
        }
        if (recipe.fluid() != null) {
            input.add(recipe.fluid());
        }
    }

    @Override
    public void draw(PassiveHeatSource recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        var minecraft = Minecraft.getInstance();
        guiGraphics.text(minecraft.font, ChatFormatting.DARK_GRAY + I18n.get("info.lollipop.temperature") + ": "
                + ChatFormatting.RESET + I18n.get("info.lollipop.temperature.c", recipe.heat()), 30, 9, 0xFFc43400, false);
    }
}
