package owmii.powah.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.compat.common.PassiveHeatSource;

class EmiHeatSourceRecipe implements EmiRecipe {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");

    public static final PowahEmiCategory CATEGORY = new PowahEmiCategory(Powah.id("passive_heat_sources"), EmiStack.of(Blocks.MAGMA_BLOCK),
            Component.translatable("gui.powah.jei.category.heat.sources"));

    private final PassiveHeatSource recipe;

    private final EmiIngredient input;

    public EmiHeatSourceRecipe(PassiveHeatSource recipe) {
        this.recipe = recipe;

        if (recipe.fluid() == null) {
            input = EmiStack.of(recipe.block());
        } else {
            input = EmiStack.of(recipe.fluid());
        }
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return recipe.id();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    @Override
    public int getDisplayWidth() {
        return 150;
    }

    @Override
    public int getDisplayHeight() {
        return 26;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(GUI_BACK, 0, 1, 160, 24, 0, 0);
        widgets.addSlot(input, 3, 4).drawBack(false);

        var label = Component.translatable("info.lollipop.temperature")
                .append(": ")
                .append(Component.translatable("info.lollipop.temperature.c", recipe.heat()));
        widgets.addText(label, 30, 9, 0xc43400, false);
    }
}
