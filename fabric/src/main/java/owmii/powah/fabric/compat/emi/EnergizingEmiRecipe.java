package owmii.powah.fabric.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.block.energizing.EnergizingRecipe;
import owmii.powah.lib.util.Util;

import java.util.List;

public class EnergizingEmiRecipe implements EmiRecipe {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/energizing.png");

    private final EnergizingRecipe recipe;
    private final List<EmiIngredient> inputs;
    private final EmiStack output;
    public EnergizingEmiRecipe(EnergizingRecipe recipe) {
        this.recipe = recipe;

        inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
        output = EmiStack.of(recipe.getResultItem());
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PowahEMIPlugin.ENERGIZING_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return recipe.getId();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 153;
    }

    @Override
    public int getDisplayHeight() {
        return 32;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(GUI_BACK, 0, 0, 153, 20, 2, 2);
        for (int i = 0; i < inputs.size(); i++) {
            widgets.addSlot(inputs.get(i), i * 20 + 1, 1)
                    .drawBack(false);
        }
        widgets.addSlot(output, 134, 1)
                .drawBack(false)
                .recipeContext(this);
        widgets.addText(Component.translatable("info.lollipop.fe", Util.addCommas(recipe.getEnergy())), 1, 23, 0x444444, false);
    }
}
