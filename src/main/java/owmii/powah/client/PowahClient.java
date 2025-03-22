package owmii.powah.client;

import guideme.Guide;
import guideme.compiler.TagCompiler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import owmii.powah.Powah;
import owmii.powah.client.book.PowahTagCompiler;
import owmii.powah.client.handler.HudHandler;
import owmii.powah.client.handler.ReactorOverlayHandler;
import owmii.powah.client.model.PowahLayerDefinitions;
import owmii.powah.client.render.entity.EntityRenderer;
import owmii.powah.client.render.tile.BlockEntityRenderers;
import owmii.powah.client.screen.Screens;
import owmii.powah.item.PowahBookItem;

@Mod(value = Powah.MOD_ID, dist = Dist.CLIENT)
public final class PowahClient {

    public PowahClient(IEventBus modEventBus) {
        modEventBus.addListener(PowahClient::clientSetup);

        modEventBus.addListener(PowahLayerDefinitions::register);
        HudHandler.register();
        modEventBus.addListener(EntityRenderer::register);
        modEventBus.addListener(Screens::register);
        modEventBus.addListener(BlockEntityRenderers::register);

        NeoForge.EVENT_BUS.addListener((RenderLevelStageEvent event) -> {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
                ReactorOverlayHandler.onRenderLast(event.getPoseStack(), event.getCamera());
            }
        });

        Guide.builder(PowahBookItem.GUIDE_ID)
                .defaultLanguage("en_us")
                .extension(TagCompiler.EXTENSION_POINT, new PowahTagCompiler())
                .build();
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(PowahClient::clientSetupSequential);
    }

    public static void clientSetupSequential() {
        ItemModelProperties.register();
    }
}
