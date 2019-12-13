package zeroneye.powah.client.renderer.tile;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import zeroneye.powah.block.cable.CableTile;
import zeroneye.powah.block.energizing.EnergizingOrbTile;
import zeroneye.powah.block.energizing.EnergizingRodTile;
import zeroneye.powah.block.generator.magmatic.MagmaticGenTile;

public class TileRenderer {
    public static void register() {
        ClientRegistry.bindTileEntitySpecialRenderer(MagmaticGenTile.class, new MagmaticGenRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(CableTile.class, new CableRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(EnergizingOrbTile.class, new EnergizingOrbRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(EnergizingRodTile.class, new EnergizingRodRenderer());
    }
}
