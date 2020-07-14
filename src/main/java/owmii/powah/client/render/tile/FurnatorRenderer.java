package owmii.powah.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.math.vector.Vector3f;
import owmii.lib.client.renderer.tile.AbstractTileRenderer;
import owmii.lib.client.util.Cube;
import owmii.powah.block.furnator.FurnatorTile;
import owmii.powah.client.handler.TextureHandler;

public class FurnatorRenderer extends AbstractTileRenderer<FurnatorTile> {
    public FurnatorRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(FurnatorTile te, float pt, MatrixStack matrix, IRenderTypeBuffer rtb, Minecraft mc, ClientWorld world, ClientPlayerEntity player, int light, int ov) {
        if (te.isBurning()) {
            TextureAtlasSprite sprite = mc.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(TextureHandler.FURNATOR_LIT);
            IVertexBuilder buffer = rtb.getBuffer(RenderType.getText(sprite.getAtlasTexture().getTextureLocation()));
            matrix.push();
            matrix.translate(0.5, 0.5, 0.5);
            matrix.rotate(Vector3f.XN.rotationDegrees(180.0f));
            matrix.scale(0.97F, 0.97F, 0.97F);
            Cube.create(matrix, buffer).side(te.getBlockState()).bright().draw(sprite);
            matrix.pop();
        }
    }
}
