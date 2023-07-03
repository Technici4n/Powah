package owmii.powah.forge.client;

import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import owmii.powah.client.PowahClient;
import owmii.powah.client.handler.ReactorOverlayHandler;

public class PowahForgeClient {
    public static void init() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(PowahForgeClient::clientSetup);

        PowahClient.init();

        MinecraftForge.EVENT_BUS.addListener((RenderLevelStageEvent event) -> {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
                ReactorOverlayHandler.onRenderLast(event.getPoseStack());
            }
        });
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(PowahClient::clientSetup);
    }
}
