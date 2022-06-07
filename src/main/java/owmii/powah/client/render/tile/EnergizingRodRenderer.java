package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import owmii.powah.lib.client.renderer.tile.AbstractTileRenderer;
import owmii.powah.lib.client.util.RenderTypes;
import owmii.powah.lib.util.math.V3d;
import owmii.powah.Powah;
import owmii.powah.api.wrench.IWrench;
import owmii.powah.block.energizing.EnergizingOrbTile;
import owmii.powah.block.energizing.EnergizingRodTile;

public class EnergizingRodRenderer extends AbstractTileRenderer<EnergizingRodTile> {
    public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/model/tile/beam.png");
    private static final RenderType RENDER_TYPE = RenderTypes.entityBlendedNoDept(BEAM_TEXTURE);

    protected EnergizingRodRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EnergizingRodTile te, float pt, PoseStack matrix, MultiBufferSource rtb, Minecraft mc, ClientLevel world, LocalPlayer player, int light, int ov) {

        boolean flag = false;

        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof IWrench wrench) {
                if (wrench.getWrenchMode(stack).link()) {
                    flag = true;
                    break;
                }
            }
        }

        EnergizingOrbTile orb = te.getOrbTile();
        if (orb != null && (te.coolDown.ended() || flag)) {

            matrix.pushPose();
            matrix.translate(0.5D, 0.5D, 0.5D);

            V3d pos = V3d.from(te.getBlockPos()).center();
            V3d orbPos = V3d.from(orb.getOrbCenter());
            float f2 = 1.0F;
            float f3 = f2 * 0.5F % 1.0F;
            Vec3 vec3d2 = pos.subtract(orbPos);
            double d0 = vec3d2.length();
            vec3d2 = vec3d2.normalize();
            // Normalize sometimes gives vector with length > 1, which breaks acos if the y component is < -1 or > 1
            float f5 = (float) Math.acos(Mth.clamp(vec3d2.y, -1.0, 1.0));
            float f6 = (float) Mth.atan2(vec3d2.z, vec3d2.x);

            matrix.mulPose(Vector3f.YP.rotationDegrees((((float) Math.PI / 2F) - f6) * (180F / (float) Math.PI)));
            matrix.mulPose(Vector3f.XP.rotationDegrees(f5 * (180F / (float) Math.PI)));

            float d1 = f2 * 0.0F;

            float d12 = Mth.cos((float) (d1 + Math.PI)) * 0.12F;
            float d13 = Mth.sin((float) (d1 + Math.PI)) * 0.12F;
            float d14 = Mth.cos(d1) * 0.12F;
            float d15 = Mth.sin(d1) * 0.12F;

            float d16 = Mth.cos((float) (d1 + (Math.PI / 2D))) * 0.12F;
            float d17 = Mth.sin((float) (d1 + (Math.PI / 2D))) * 0.12F;
            float d18 = Mth.cos((float) (d1 + (Math.PI * 1.5D))) * 0.12F;
            float d19 = Mth.sin((float) (d1 + (Math.PI * 1.5D))) * 0.12F;

            float d22 = (f3 - 1.0F);
            float d23 = (float) (d0 * 5.05D + d22);
            VertexConsumer builder = rtb.getBuffer(RENDER_TYPE);
            PoseStack.Pose last = matrix.last();
            Matrix4f matrix4f = last.pose();
            Matrix3f matrix3f = last.normal();

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

            matrix.popPose();
        }
    }

    private void pos(VertexConsumer builder, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, float z, int r, int g, int b, float u, float v) {
        builder.vertex(matrix4f, x, y, z).color(r, g, b, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880 / 2).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
