package owmii.powah.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public class MagmatorEmiRecipe extends PowahInfoEmiRecipe {
    private final int heat;
    private final EmiIngredient input;

    public MagmatorEmiRecipe(Fluid fluid, int heat) {
        this.heat = heat;
        this.input = EmiStack.of(fluid);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PowahEMIPlugin.MAGMATOR_CATEGORY;
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
        return Component.literal(heat + " FE/100 mb");
    }
}
