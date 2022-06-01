package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import owmii.lib.client.renderer.tile.AbstractTileRenderer;
import owmii.powah.block.cable.CableTile;
import owmii.powah.client.model.CableModel;

public class CableRenderer extends AbstractTileRenderer<CableTile> {
    private static final CableModel MODEL = new CableModel();

    protected CableRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public void render(CableTile te, final float pt, final PoseStack matrix, final MultiBufferSource rtb, final Minecraft mc, final ClientLevel world, final LocalPlayer player, final int light, final int ov) {
        matrix.pushPose();
        matrix.translate(0.5, 1.5, 0.5);
        matrix.translate(0.0, -0.125, 0.0);
        matrix.scale(1.0f, -1.0f, -1.0f);
        MODEL.render(te, this, matrix, rtb, light, ov);
        matrix.popPose();
    }
}
