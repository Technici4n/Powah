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
import net.minecraft.world.level.block.Blocks;
import owmii.powah.Powah;
import owmii.powah.compat.common.PassiveHeatSource;

public class JeiHeatSourceCategory extends AbstractCategory<PassiveHeatSource> {
    public static final RecipeType<PassiveHeatSource> TYPE = RecipeType.create(Powah.MOD_ID, "heat_source", PassiveHeatSource.class);

    public JeiHeatSourceCategory(IGuiHelper guiHelper) {
        super(guiHelper, Blocks.MAGMA_BLOCK, Component.translatable("gui.powah.jei.category.heat.sources"),
                guiHelper.drawableBuilder(Assets.MISC, 0, 0, 160, 24).addPadding(1, 0, 0, 0).build());
    }

    @Override
    public RecipeType<PassiveHeatSource> getRecipeType() {
        return TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PassiveHeatSource recipe, IFocusGroup focuses) {
        var input = builder.addSlot(RecipeIngredientRole.INPUT, 4, 5);

        if (recipe.block() != null) {
            input.addItemLike(recipe.block());
        }
        if (recipe.fluid() != null) {
            input.addFluidStack(recipe.fluid());
        }
    }

    @Override
    public void draw(PassiveHeatSource recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        var minecraft = Minecraft.getInstance();
        guiGraphics.drawString(minecraft.font, ChatFormatting.DARK_GRAY + I18n.get("info.lollipop.temperature") + ": "
                + ChatFormatting.RESET + I18n.get("info.lollipop.temperature.c", recipe.heat()), 30, 9, 0xc43400, false);
    }
}
