package owmii.powah.client;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import owmii.lib.api.IClient;
import owmii.powah.book.PowahBook;
import owmii.powah.client.render.entity.EntityRenderer;
import owmii.powah.client.render.tile.TileRenderer;
import owmii.powah.client.screen.Screens;

public enum Client implements IClient {
    INSTANCE;

    @Override
    public void client(FMLClientSetupEvent event) {
        TileRenderer.register();
        EntityRenderer.register();
        Screens.register();
        ItemModelProperties.register();
        PowahBook.register();
    }
}
