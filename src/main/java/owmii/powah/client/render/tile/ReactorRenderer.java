package owmii.powah.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import owmii.lib.client.model.CubeModel;
import owmii.lib.client.renderer.tile.AbstractTileRenderer;
import owmii.powah.Powah;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.client.model.ReactorModel;

public class ReactorRenderer extends AbstractTileRenderer<ReactorTile> {
    public static final CubeModel CUBE_MODEL = new CubeModel(16, RenderType::getEntitySolid);
    private static final ReactorModel REACTOR_MODEL = new ReactorModel();

    public ReactorRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(ReactorTile te, float pt, MatrixStack matrix, IRenderTypeBuffer rtb, Minecraft mc, ClientWorld world, ClientPlayerEntity player, int light, int ov) {
        matrix.push();
        matrix.translate(0.5, 0.5, 0.5);
        matrix.scale(1.0f, -1.0f, -1.0f);
        if (!te.isBuilt()) {
            IVertexBuilder buffer = rtb.getBuffer(CUBE_MODEL.getRenderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor_block_" + te.getVariant().getName() + ".png")));
            CUBE_MODEL.render(matrix, buffer, light, ov, 1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            matrix.translate(0.0D, -1.0D, 0.0D);
            REACTOR_MODEL.render(te, this, matrix, rtb, light, ov);
        }
        matrix.pop();
    }

    @Override
    public boolean isGlobalRenderer(ReactorTile te) {
        return te.isBuilt();
    }
}
