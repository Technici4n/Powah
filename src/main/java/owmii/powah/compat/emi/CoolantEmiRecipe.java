package owmii.powah.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public class CoolantEmiRecipe extends PowahInfoEmiRecipe {
    private final int coldness;
    private final EmiIngredient input;

    public CoolantEmiRecipe(Fluid fluid, int coldness) {
        this.coldness = coldness;
        this.input = EmiStack.of(fluid);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PowahEMIPlugin.COOLANT_CATEGORY;
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
                .append(Component.translatable("info.lollipop.temperature.c", coldness)
                        .withStyle(ChatFormatting.DARK_AQUA));
    }
}
