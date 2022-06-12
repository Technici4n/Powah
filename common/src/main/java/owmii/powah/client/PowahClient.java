package owmii.powah.client;

import owmii.powah.client.book.PowahBook;
import owmii.powah.client.handler.TextureHandler;
import owmii.powah.client.model.PowahLayerDefinitions;
import owmii.powah.client.render.tile.TileRenderer;
import owmii.powah.client.screen.Screens;
import owmii.powah.client.handler.HudHandler;

public final class PowahClient {
    public static void init() {
        PowahLayerDefinitions.register();
        HudHandler.register();
    }

    public static void clientSetup() {
        TileRenderer.register();
        Screens.register();
        ItemModelProperties.register();
        TextureHandler.register();
        PowahBook.register();
    }
}
