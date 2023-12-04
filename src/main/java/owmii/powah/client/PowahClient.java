package owmii.powah.client;

import owmii.powah.client.book.PowahBook;
import owmii.powah.client.handler.HudHandler;
import owmii.powah.client.model.PowahLayerDefinitions;
import owmii.powah.client.render.entity.EntityRenderer;
import owmii.powah.client.render.tile.TileRenderer;
import owmii.powah.client.screen.Screens;

public final class PowahClient {
    public static void init() {
        PowahLayerDefinitions.register();
        HudHandler.register();
        EntityRenderer.register();
    }

    public static void clientSetup() {
        TileRenderer.register();
        Screens.register();
        ItemModelProperties.register();
        PowahBook.register();
    }
}
