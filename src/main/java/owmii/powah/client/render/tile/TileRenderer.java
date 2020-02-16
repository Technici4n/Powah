package owmii.powah.client.render.tile;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import owmii.lib.client.renderer.item.TEItemRenderer;
import owmii.powah.block.IBlocks;
import owmii.powah.block.ITiles;

public class TileRenderer {
    public static void register() {
        ClientRegistry.bindTileEntityRenderer(ITiles.ENERGY_CABLE, EnergyCableRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ITiles.ENERGIZING_ORB, EnergizingOrbRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ITiles.ENERGIZING_ROD, EnergizingRodRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ITiles.MAGMATOR, MagmatorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ITiles.REACTOR, ReactorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ITiles.REACTOR_PART, ReactorPartRenderer::new);

        TEItemRenderer.register(
                IBlocks.REACTOR_STARTER,
                IBlocks.REACTOR_BASIC,
                IBlocks.REACTOR_HARDENED,
                IBlocks.REACTOR_BLAZING,
                IBlocks.REACTOR_NIOTIC,
                IBlocks.REACTOR_SPIRITED,
                IBlocks.REACTOR_NITRO
        );
    }
}
