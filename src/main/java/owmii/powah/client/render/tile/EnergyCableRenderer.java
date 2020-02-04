package owmii.powah.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.world.ClientWorld;
import owmii.lib.client.renderer.tile.AbstractTileRenderer;
import owmii.powah.block.cable.EnergyCableTile;
import owmii.powah.client.model.EnergyCableModel;

public class EnergyCableRenderer extends AbstractTileRenderer<EnergyCableTile> {
    private static final EnergyCableModel MODEL = new EnergyCableModel();

    public EnergyCableRenderer(final TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    public void render(final EnergyCableTile te, final float pt, final MatrixStack matrix, final IRenderTypeBuffer rtb, final Minecraft mc, final ClientWorld world, final ClientPlayerEntity player, final int light, final int ov) {
        matrix.push();
        matrix.translate(0.5, 1.5, 0.5);
        matrix.translate(0.0, -0.125, 0.0);
        matrix.scale(1.0f, -1.0f, -1.0f);
        EnergyCableRenderer.MODEL.render(te, this, matrix, rtb, light, ov);
        matrix.pop();
    }
}
