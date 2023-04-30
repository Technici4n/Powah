package owmii.powah.fabric.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import owmii.powah.Powah;

public abstract class PowahInfoEmiRecipe implements EmiRecipe {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");

    @Override
    public int getDisplayWidth() {
        return 130;
    }

    @Override
    public int getDisplayHeight() {
        return 24;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(getInput());
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    protected abstract EmiIngredient getInput();

    protected abstract Component getInfoText();

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(GUI_BACK, 0, 0, 24, 24, 0, 0);
        widgets.addSlot(getInput(), 3, 3).drawBack(false);
        widgets.addText(getInfoText(), 27, 12, 0x444444, false).verticalAlign(TextWidget.Alignment.CENTER);
    }
}
