package owmii.powah.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import owmii.lib.client.renderer.tile.AbstractTileRenderer;
import owmii.lib.client.util.RenderTypes;
import owmii.lib.util.math.V3d;
import owmii.powah.Powah;
import owmii.powah.api.wrench.IWrench;
import owmii.powah.block.energizing.EnergizingOrbTile;
import owmii.powah.block.energizing.EnergizingRodTile;

public class EnergizingRodRenderer extends AbstractTileRenderer<EnergizingRodTile> {
    public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/model/tile/beam.png");
    private static final RenderType RENDER_TYPE = RenderTypes.entityBlendedNoDept(BEAM_TEXTURE);

    public EnergizingRodRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(EnergizingRodTile te, float pt, MatrixStack matrix, IRenderTypeBuffer rtb, Minecraft mc, ClientWorld world, ClientPlayerEntity player, int light, int ov) {

        boolean flag = false;

        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getHeldItem(hand);
            if (stack.getItem() instanceof IWrench) {
                IWrench wrench = (IWrench) stack.getItem();
                if (wrench.getWrenchMode(stack).link()) {
                    flag = true;
                    break;
                }
            }
        }

        EnergizingOrbTile orb = te.getOrbTile();
        if (orb != null && (te.coolDown.ended() || flag)) {

            matrix.push();
            matrix.translate(0.5D, 0.5D, 0.5D);

            V3d pos = V3d.from(te.getPos()).center();
            V3d orbPos = V3d.from(orb.getOrbCenter());
            float f2 = 1.0F;
            float f3 = f2 * 0.5F % 1.0F;
            Vector3d vec3d2 = pos.subtract(orbPos);
            double d0 = vec3d2.length();
            vec3d2 = vec3d2.normalize();
            // Normalize sometimes gives vector with length > 1, which breaks acos if the y component is < -1 or > 1
            float f5 = (float) Math.acos(MathHelper.clamp(vec3d2.y, -1.0, 1.0));
            float f6 = (float) MathHelper.atan2(vec3d2.z, vec3d2.x);

            matrix.rotate(Vector3f.YP.rotationDegrees((((float) Math.PI / 2F) - f6) * (180F / (float) Math.PI)));
            matrix.rotate(Vector3f.XP.rotationDegrees(f5 * (180F / (float) Math.PI)));

            float d1 = f2 * 0.0F;

            float d12 = MathHelper.cos((float) (d1 + Math.PI)) * 0.12F;
            float d13 = MathHelper.sin((float) (d1 + Math.PI)) * 0.12F;
            float d14 = MathHelper.cos(d1) * 0.12F;
            float d15 = MathHelper.sin(d1) * 0.12F;

            float d16 = MathHelper.cos((float) (d1 + (Math.PI / 2D))) * 0.12F;
            float d17 = MathHelper.sin((float) (d1 + (Math.PI / 2D))) * 0.12F;
            float d18 = MathHelper.cos((float) (d1 + (Math.PI * 1.5D))) * 0.12F;
            float d19 = MathHelper.sin((float) (d1 + (Math.PI * 1.5D))) * 0.12F;

            float d22 = (f3 - 1.0F);
            float d23 = (float) (d0 * 5.05D + d22);
            IVertexBuilder builder = rtb.getBuffer(RENDER_TYPE);
            MatrixStack.Entry last = matrix.getLast();
            Matrix4f matrix4f = last.getMatrix();
            Matrix3f matrix3f = last.getNormal();

            int color = te.getVariant().getColor();
            int r = 0xFF & (color >> 16);
            int g = 0xFF & (color >> 8);
            int b = 0xFF & color;

            pos(builder, matrix4f, matrix3f, d12, 0.0F, d13, r, g, b, 1, d23);
            pos(builder, matrix4f, matrix3f, d12, (float) -d0, d13, r, g, b, 1, d22);
            pos(builder, matrix4f, matrix3f, d14, (float) -d0, d15, r, g, b, 0.0F, d22);
            pos(builder, matrix4f, matrix3f, d14, 0.0F, d15, r, g, b, 0.0F, d23);

            pos(builder, matrix4f, matrix3f, d16, 0.0F, d17, r, g, b, 1, d23);
            pos(builder, matrix4f, matrix3f, d16, (float) -d0, d17, r, g, b, 1, d22);
            pos(builder, matrix4f, matrix3f, d18, (float) -d0, d19, r, g, b, 0.0F, d22);
            pos(builder, matrix4f, matrix3f, d18, 0.0F, d19, r, g, b, 0.0F, d23);

            matrix.pop();
        }
    }

    private void pos(IVertexBuilder builder, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, float z, int r, int g, int b, float u, float v) {
        builder.pos(matrix4f, x, y, z).color(r, g, b, 255).tex(u, v).overlay(OverlayTexture.NO_OVERLAY).lightmap(15728880 / 2).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
