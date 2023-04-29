package owmii.powah.fabric.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class HeatSourceEmiRecipe extends PowahInfoEmiRecipe {
    private final EmiIngredient input;
    private final int heat;

    public HeatSourceEmiRecipe(EmiIngredient input, int heat) {
        this.input = input;
        this.heat = heat;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PowahEMIPlugin.HEAT_SOURCE_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return null;
    }

    @Override
    protected EmiIngredient getInput() {
        return input;
    }

    @Override
    protected Component getInfoText() {
        return Component.translatable("info.lollipop.temperature")
                .append(": ")
                .append(Component.translatable("info.lollipop.temperature.c", heat)
                        .withStyle(s -> s.withColor(0xc43400)));
    }
}
