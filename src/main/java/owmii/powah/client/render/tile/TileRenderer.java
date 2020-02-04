package owmii.powah.client.render.tile;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import owmii.powah.block.ITiles;

public class TileRenderer {
    public static void register() {
        ClientRegistry.bindTileEntityRenderer(ITiles.ENERGY_CABLE, EnergyCableRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ITiles.ENERGIZING_ORB, EnergizingOrbRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ITiles.ENERGIZING_ROD, EnergizingRodRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ITiles.MAGMATOR, MagmatorRenderer::new);
    }
}
