package owmii.powah.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;
import owmii.powah.compat.common.MagmatorFuel;

class EmiMagmatorRecipe extends BasicEmiRecipe {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");

    public static final PowahEmiCategory CATEGORY = new PowahEmiCategory(Powah.id("magmator"), EmiStack.of(Blcks.MAGMATOR.get(Tier.BASIC)),
            Component.translatable("gui.powah.jei.category.magmatic"));
    private final int heat;

    public EmiMagmatorRecipe(MagmatorFuel fuel) {
        super(CATEGORY, fuel.id(), 160, 26);

        List<EmiIngredient> fuels = new ArrayList<>(1 + fuel.buckets().size());
        fuels.add(EmiStack.of(fuel.fluid()));
        for (var bucket : fuel.buckets()) {
            fuels.add(EmiStack.of(bucket));
        }
        this.inputs.add(EmiIngredient.of(fuels));

        this.heat = fuel.heat();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(GUI_BACK, 0, 1, 160, 24, 0, 0);
        widgets.addSlot(this.inputs.get(0), 3, 4);
        widgets.addText(
                Component.literal(heat + " FE/100 mb"),
                27, 9, 0x444444, false);
    }
}
