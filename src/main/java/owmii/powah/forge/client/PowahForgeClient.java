package owmii.powah.forge.client;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import owmii.powah.client.PowahClient;
import owmii.powah.client.handler.ReactorOverlayHandler;

public class PowahForgeClient {
    public static void init() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(PowahForgeClient::clientSetup);

        PowahClient.init();

        NeoForge.EVENT_BUS.addListener((RenderLevelStageEvent event) -> {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
                ReactorOverlayHandler.onRenderLast(event.getPoseStack());
            }
        });
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(PowahClient::clientSetup);
    }
}
