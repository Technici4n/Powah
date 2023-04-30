package owmii.powah.compat.jei;

import dev.architectury.fluid.FluidStack;
import dev.architectury.platform.Platform;
import java.util.stream.Stream;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.Util;

public interface JeiEnvHandler {
    JeiEnvHandler INSTANCE = Util.make(() -> {
        try {
            var klass = Class
                    .forName(Platform.isForge() ? "owmii.powah.forge.compat.ForgeJeiEnvHandler" : "owmii.powah.fabric.compat.FabricJeiEnvHandler");
            return (JeiEnvHandler) klass.getConstructor().newInstance();
        } catch (Exception exception) {
            throw new RuntimeException("Failed to setup JEI env handler", exception);
        }
    });

    Stream<FluidStack> getAllFluidIngredients(IIngredientManager ingredientManager);
}
