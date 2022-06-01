package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import owmii.lib.client.model.CubeModel;
import owmii.lib.client.renderer.tile.AbstractTileRenderer;
import owmii.powah.Powah;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.client.model.ReactorModel;

public class ReactorRenderer extends AbstractTileRenderer<ReactorTile> {
    public static final CubeModel CUBE_MODEL = new CubeModel(16, RenderType::entitySolid);
    private static final ReactorModel REACTOR_MODEL = new ReactorModel();

    protected ReactorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ReactorTile te, float pt, PoseStack matrix, MultiBufferSource rtb, Minecraft mc, ClientLevel world, LocalPlayer player, int light, int ov) {
        matrix.pushPose();
        matrix.translate(0.5, 0.5, 0.5);
        matrix.scale(1.0f, -1.0f, -1.0f);
        if (!te.isBuilt()) {
            VertexConsumer buffer = rtb.getBuffer(CUBE_MODEL.renderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor_block_" + te.getVariant().getName() + ".png")));
            CUBE_MODEL.renderToBuffer(matrix, buffer, light, ov, 1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            matrix.translate(0.0D, -1.0D, 0.0D);
            REACTOR_MODEL.render(te, this, matrix, rtb, light, ov);
        }
        matrix.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(ReactorTile te) {
        return te.isBuilt();
    }
}
