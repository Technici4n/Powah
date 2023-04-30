package owmii.powah.fabric.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import owmii.powah.Powah;

import java.util.List;

public class SolidCoolantEmiRecipe implements EmiRecipe {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");
    private final EmiIngredient input;
    private final int amount;
    private final int coldness;
    public SolidCoolantEmiRecipe(Item item, int amount, int coldness) {
        this.input = EmiStack.of(item);
        this.amount = amount;
        this.coldness = coldness;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PowahEMIPlugin.SOLID_COOLANT_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return null;
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
        return 130;
    }

    @Override
    public int getDisplayHeight() {
        return 24;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(GUI_BACK, 0, 0, 24, 24, 0, 0);
        widgets.addSlot(input, 3, 3).drawBack(false);
        widgets.addText(Component.translatable("info.lollipop.amount").append(": ")
                .append(Component.translatable("info.lollipop.mb", amount)), 27, 7, 0x444444, false)
                .verticalAlign(TextWidget.Alignment.CENTER);
        widgets.addText(Component.translatable("info.lollipop.temperature").append(": ")
                .append(Component.translatable("info.lollipop.temperature.c", coldness)
                        .withStyle(ChatFormatting.DARK_AQUA)), 27, 17, 0x444444, false)
                .verticalAlign(TextWidget.Alignment.CENTER);
    }
}
