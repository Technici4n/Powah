package owmii.powah.client;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import owmii.powah.client.book.PowahBook;
import owmii.powah.client.model.PowahLayerDefinitions;
import owmii.powah.client.render.entity.EntityRenderer;
import owmii.powah.client.render.tile.TileRenderer;
import owmii.powah.client.screen.Screens;

public final class PowahClient {
    public static void init() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(PowahLayerDefinitions::register);

        modEventBus.addListener(PowahClient::clientSetup);
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        EntityRenderer.register();

        event.enqueueWork(() -> {
            TileRenderer.register();
            Screens.register();
            ItemModelProperties.register();
            PowahBook.register();
        });
    }
}
