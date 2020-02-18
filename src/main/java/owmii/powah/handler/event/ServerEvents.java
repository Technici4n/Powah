package owmii.powah.handler.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import owmii.powah.api.recipe.energizing.EnergizingRecipeSorter;

@Mod.EventBusSubscriber
public class ServerEvents {
    @SubscribeEvent
    public static void stopped(FMLServerStoppedEvent event) {
        EnergizingRecipeSorter.sort();
    }
}
