package owmii.powah.forge.compat;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import java.util.stream.Stream;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.runtime.IIngredientManager;
import owmii.powah.compat.jei.JeiEnvHandler;

public class ForgeJeiEnvHandler implements JeiEnvHandler {
    @Override
    public Stream<FluidStack> getAllFluidIngredients(IIngredientManager ingredientManager) {
        return ingredientManager.getAllIngredients(ForgeTypes.FLUID_STACK)
                .stream()
                .map(FluidStackHooksForge::fromForge);
    }
}
