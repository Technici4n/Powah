package owmii.powah.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

class PowahEmiCategory extends EmiRecipeCategory {
    private final Component name;

    public PowahEmiCategory(ResourceLocation id, EmiRenderable icon, Component name) {
        super(id, icon);
        this.name = name;
    }

    @Override
    public Component getName() {
        return name;
    }
}
