package owmii.powah.client;

import owmii.powah.client.book.PowahBook;
import owmii.powah.client.handler.TextureHandler;
import owmii.powah.client.model.PowahLayerDefinitions;
import owmii.powah.client.render.tile.TileRenderer;
import owmii.powah.client.screen.Screens;

public final class PowahClient {
    public static void clientSetup() {
        PowahLayerDefinitions.register();
        TileRenderer.register();
        Screens.register();
        ItemModelProperties.register();
        TextureHandler.register();
        PowahBook.register();
    }
}
