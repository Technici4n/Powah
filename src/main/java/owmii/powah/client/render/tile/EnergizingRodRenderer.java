package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.api.wrench.IWrench;
import owmii.powah.block.energizing.EnergizingRodBlockEntity;
import owmii.powah.lib.client.util.RenderTypes;
import owmii.powah.util.math.V3d;

public class EnergizingRodRenderer implements BlockEntityRenderer<EnergizingRodBlockEntity, EnergizingRodRendererState> {
    public static final Identifier BEAM_TEXTURE = Powah.id("textures/model/tile/beam.png");
    private static final RenderType RENDER_TYPE = RenderTypes.entityBlendedNoDepthWrite(BEAM_TEXTURE);

    protected EnergizingRodRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public EnergizingRodRendererState createRenderState() {
        return new EnergizingRodRendererState();
    }

    @Override
    public void extractRenderState(EnergizingRodBlockEntity blockEntity, EnergizingRodRendererState state, float partialTicks, Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        boolean wrenchInLinkMode = false;
        var player = Minecraft.getInstance().player;
        if (player != null) {
            for (var hand : InteractionHand.values()) {
                var stack = Minecraft.getInstance().player.getItemInHand(hand);
                if (stack.getItem() instanceof IWrench wrench) {
                    if (wrench.getWrenchMode(stack).link()) {
                        wrenchInLinkMode = true;
                        break;
                    }
                }
            }
        }

        state.tier = blockEntity.getTier();

        var orb = blockEntity.getOrbTile();
        if (orb != null && (blockEntity.coolDown.ended() || wrenchInLinkMode)) {
            state.orbCenter = orb.getOrbCenter();
        } else {
            state.orbCenter = null;
        }
    }

    @Override
    public void submit(EnergizingRodRendererState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {

        var orbCenter = state.orbCenter;
        if (orbCenter != null) {

            poseStack.pushPose();
            poseStack.translate(0.5D, 0.5D, 0.5D);

            V3d pos = V3d.from(state.blockPos).center();
            V3d orbPos = V3d.from(orbCenter);
            float f2 = 1.0F;
            float f3 = f2 * 0.5F % 1.0F;
            Vec3 vec3d2 = pos.subtract(orbPos);
            double d0 = vec3d2.length();
            vec3d2 = vec3d2.normalize();
            // Normalize sometimes gives vector with length > 1, which breaks acos if the y component is < -1 or > 1
            float f5 = (float) Math.acos(Mth.clamp(vec3d2.y, -1.0, 1.0));
            float f6 = (float) Mth.atan2(vec3d2.z, vec3d2.x);

            poseStack.mulPose(Axis.YP.rotationDegrees((((float) Math.PI / 2F) - f6) * (180F / (float) Math.PI)));
            poseStack.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float) Math.PI)));

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

            submitNodeCollector.submitCustomGeometry(poseStack, RENDER_TYPE, (pose, buffer) -> {
                int color = state.tier.getColor();
                int r = 0xFF & (color >> 16);
                int g = 0xFF & (color >> 8);
                int b = 0xFF & color;

                pos(buffer, pose, d12, 0.0F, d13, r, g, b, 1, d23);
                pos(buffer, pose, d12, (float) -d0, d13, r, g, b, 1, d22);
                pos(buffer, pose, d14, (float) -d0, d15, r, g, b, 0.0F, d22);
                pos(buffer, pose, d14, 0.0F, d15, r, g, b, 0.0F, d23);

                pos(buffer, pose, d16, 0.0F, d17, r, g, b, 1, d23);
                pos(buffer, pose, d16, (float) -d0, d17, r, g, b, 1, d22);
                pos(buffer, pose, d18, (float) -d0, d19, r, g, b, 0.0F, d22);
                pos(buffer, pose, d18, 0.0F, d19, r, g, b, 0.0F, d23);
            });

            poseStack.popPose();
        }
    }

    private void pos(VertexConsumer builder, PoseStack.Pose pose, float x, float y, float z, int r, int g, int b, float u, float v) {
        builder.addVertex(pose, x, y, z).setColor(r, g, b, 255).setUv(u, v);
    }

    @Override
    public AABB getRenderBoundingBox(EnergizingRodBlockEntity blockEntity) {
        int range = Powah.config().general.energizing_range;
        return new AABB(blockEntity.getBlockPos()).inflate(range);
    }
}
