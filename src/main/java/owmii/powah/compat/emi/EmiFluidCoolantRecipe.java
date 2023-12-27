package owmii.powah.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.compat.common.FluidCoolant;

class EmiFluidCoolantRecipe implements EmiRecipe {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");

    public static final PowahEmiCategory CATEGORY = new PowahEmiCategory(Powah.id("coolant"), EmiStack.of(Items.WATER_BUCKET),
            Component.translatable("gui.powah.jei.category.coolant"));

    private final FluidCoolant recipe;

    private final EmiIngredient input;

    public EmiFluidCoolantRecipe(FluidCoolant recipe) {
        this.recipe = recipe;

        var inputs = new ArrayList<EmiStack>(1 + recipe.buckets().size());
        inputs.add(EmiStack.of(recipe.fluid()));
        recipe.buckets().stream().map(EmiStack::of).forEach(inputs::add);
        this.input = EmiIngredient.of(inputs);
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
        return 158;
    }

    @Override
    public int getDisplayHeight() {
        return 26;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(GUI_BACK, 0, 1, 160, 24, 0, 0);
        widgets.addSlot(input, 3, 4).drawBack(false);

        var coloredTemperature = Component.literal(String.valueOf(recipe.coldness())).withStyle(ChatFormatting.DARK_AQUA);
        var label = Component.translatable("info.lollipop.temperature")
                .append(": ")
                .append(Component.translatable("info.lollipop.temperature.c", coloredTemperature));
        widgets.addText(label, 30, 9, 0x444444, false);
    }
}
