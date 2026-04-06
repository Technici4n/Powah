package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import owmii.powah.block.magmator.MagmatorBlockEntity;
import owmii.powah.client.ClientUtils;
import owmii.powah.lib.client.util.Render;
import owmii.powah.lib.logistics.fluid.Tank;

public class MagmatorRenderer implements BlockEntityRenderer<MagmatorBlockEntity, MagmatorRendererState> {
    protected MagmatorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public MagmatorRendererState createRenderState() {
        return new MagmatorRendererState();
    }

    @Override
    public void extractRenderState(MagmatorBlockEntity blockEntity, MagmatorRendererState state, float partialTicks, Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        Tank tank = blockEntity.getTank();
        state.tank = tank.getFluid();
        state.fill = (tank.getFluidAmount() * (0.45F)) / tank.getCapacity();
    }

    @Override
    public void submit(MagmatorRendererState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        var fluidStack = state.tank;
        if (fluidStack.isEmpty()) {
            return;
        }

        var sprite = ClientUtils.getStillTexture(fluidStack);
        int color = ClientUtils.getFluidColor(fluidStack);

        poseStack.pushPose();
        poseStack.translate(0.1875f, 0.51D + state.fill, 0.1875f);
        poseStack.scale(0.625f, 1.0F, 0.625f);
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.text(sprite.atlasLocation()), (pose, buffer) -> {
            float red = (color >> 16 & 0xFF) / 255.0F;
            float green = (color >> 8 & 0xFF) / 255.0F;
            float blue = (color & 0xFF) / 255.0F;
            Render.quad(pose.pose(), buffer, sprite, 1.0F, 1.0F, red, green, blue);
        });
        poseStack.popPose();
    }
}
