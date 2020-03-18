package owmii.powah.handler.event;

import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.powah.api.recipe.energizing.EnergizingRecipeSorter;

@Mod.EventBusSubscriber
public class ResourceHandler implements IResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        EnergizingRecipeSorter.sort();
    }

    @SubscribeEvent
    public static void recipesUpdated(RecipesUpdatedEvent event) {
        EnergizingRecipeSorter.sort();
    }
}
