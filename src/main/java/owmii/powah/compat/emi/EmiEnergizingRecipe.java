package owmii.powah.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.block.energizing.EnergizingRecipe;
import owmii.powah.util.Util;

class EmiEnergizingRecipe extends BasicEmiRecipe {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/energizing.png");

    public static final PowahEmiCategory CATEGORY = new PowahEmiCategory(Powah.id("energizizng"), EmiStack.of(Blcks.ENERGIZING_ORB.get()),
            Component.translatable("gui.powah.jei.category.energizing"));

    private final EnergizingRecipe recipe;

    public EmiEnergizingRecipe(RecipeHolder<EnergizingRecipe> holder) {
        super(CATEGORY, holder.id(), 158, 36);
        this.recipe = holder.value();

        for (var ingredient : recipe.getIngredients()) {
            this.inputs.add(EmiIngredient.of(ingredient));
        }
        this.outputs.add(EmiStack.of(recipe.getResultItem()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(GUI_BACK, 0, 1, 160, 38, 0, 0);
        for (int i = 0; i < inputs.size(); i++) {
            widgets.addSlot(inputs.get(i), i * 20 + 3, 4).drawBack(false);
        }
        if (!outputs.isEmpty()) {
            widgets.addSlot(outputs.get(0), 136, 4)
                    .drawBack(false)
                    .recipeContext(this);
        }

        var text = Component.translatable("info.lollipop.fe", Util.addCommas(recipe.getEnergy()));
        widgets.addText(text, 2, 29, 0x444444, false);
    }
}
