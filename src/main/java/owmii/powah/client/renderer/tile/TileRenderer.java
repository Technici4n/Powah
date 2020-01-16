package owmii.powah.client.renderer.tile;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import owmii.lib.client.renderer.item.TEItemRenderer;
import owmii.powah.block.IBlocks;
import owmii.powah.block.cable.CableTile;
import owmii.powah.block.energizing.EnergizingOrbTile;
import owmii.powah.block.energizing.EnergizingRodTile;
import owmii.powah.block.generator.magmatic.MagmaticGenTile;
import owmii.powah.block.generator.reactor.ReactorTile;

public class TileRenderer {
    public static void register() {
        ClientRegistry.bindTileEntitySpecialRenderer(MagmaticGenTile.class, new MagmaticGenRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(CableTile.class, new CableRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(EnergizingOrbTile.class, new EnergizingOrbRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(EnergizingRodTile.class, new EnergizingRodRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(ReactorTile.class, new ReactorRenderer());

        TEItemRenderer.register(IBlocks.REACTORS);
    }
}
