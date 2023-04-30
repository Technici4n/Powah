package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import owmii.powah.block.furnator.FurnatorTile;
import owmii.powah.client.handler.TextureHandler;
import owmii.powah.lib.client.renderer.tile.AbstractTileRenderer;
import owmii.powah.lib.client.util.Cube;

public class FurnatorRenderer extends AbstractTileRenderer<FurnatorTile> {
    protected FurnatorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(FurnatorTile te, float pt, PoseStack matrix, MultiBufferSource rtb, Minecraft mc, ClientLevel world, LocalPlayer player,
            int light, int ov) {
        if (te.isBurning()) {
            TextureAtlasSprite sprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TextureHandler.FURNATOR_LIT);
            VertexConsumer buffer = rtb.getBuffer(RenderType.text(sprite.atlas().location()));
            matrix.pushPose();
            matrix.translate(0.5, 0.5, 0.5);
            matrix.mulPose(Vector3f.XN.rotationDegrees(180.0f));
            matrix.scale(0.97F, 0.97F, 0.97F);
            Cube.create(matrix, buffer).side(te.getBlockState()).bright().draw(sprite);
            matrix.popPose();
        }
    }
}
