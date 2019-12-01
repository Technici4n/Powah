package zeroneye.powah.client.renderer.tile;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import zeroneye.powah.block.cable.CableTile;
import zeroneye.powah.block.generator.magmatic.MagmaticGenTile;

public class ITileRnderers {
    public static void register() {
        ClientRegistry.bindTileEntitySpecialRenderer(MagmaticGenTile.class, new MagmaticGenRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(CableTile.class, new CableRenderer());
    }
}
