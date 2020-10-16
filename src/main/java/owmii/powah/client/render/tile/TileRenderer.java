package owmii.powah.client.render.tile;

import net.minecraft.block.Block;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import owmii.lib.client.renderer.item.TEItemRenderer;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tiles;

public class TileRenderer {
    public static void register() {
        ClientRegistry.bindTileEntityRenderer(Tiles.CABLE, CableRenderer::new);
        ClientRegistry.bindTileEntityRenderer(Tiles.ENERGIZING_ORB, EnergizingOrbRenderer::new);
        ClientRegistry.bindTileEntityRenderer(Tiles.ENERGIZING_ROD, EnergizingRodRenderer::new);
        ClientRegistry.bindTileEntityRenderer(Tiles.FURNATOR, FurnatorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(Tiles.MAGMATOR, MagmatorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(Tiles.REACTOR, ReactorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(Tiles.REACTOR_PART, ReactorPartRenderer::new);

        TEItemRenderer.register(Blcks.REACTOR.getArr(Block[]::new));
    }
}
