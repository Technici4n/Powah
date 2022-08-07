package owmii.powah.forge.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import owmii.powah.lib.client.screen.container.AbstractContainerScreen;

import javax.annotation.Nullable;

@JeiPlugin
public class JEI implements IModPlugin {
    public static final String ID = "jei";
    private static int loaded;

    @Nullable
    private static IJeiRuntime runtime = null;

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        JEI.runtime = jeiRuntime;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(AbstractContainerScreen.class, new GuiContainerHandler());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("lollipop", "main");
    }

    public static boolean isLoaded() {
        if (loaded == 0) {
            loaded = ModList.get().isLoaded(ID) ? 1 : -1;
        }
        return loaded == 1;
    }
}
