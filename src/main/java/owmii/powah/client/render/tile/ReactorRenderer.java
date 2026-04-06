package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.block.reactor.ReactorBlockEntity;
import owmii.powah.client.model.CubeModel;
import owmii.powah.client.model.PowahLayerDefinitions;
import owmii.powah.client.model.ReactorModel;

public class ReactorRenderer implements BlockEntityRenderer<ReactorBlockEntity, ReactorRendererState> {
    private final ReactorModel reactorModel;
    private final CubeModel reactorPartModel;

    protected ReactorRenderer(BlockEntityRendererProvider.Context context) {
        reactorModel = new ReactorModel(context.bakeLayer(PowahLayerDefinitions.REACTOR));
        reactorPartModel = new CubeModel(RenderTypes::entitySolid, context.bakeLayer(PowahLayerDefinitions.REACTOR_PART));
    }

    @Override
    public ReactorRendererState createRenderState() {
        return new ReactorRendererState();
    }

    @Override
    public void extractRenderState(ReactorBlockEntity blockEntity, ReactorRendererState state, float partialTicks, Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.update(blockEntity);
    }

    @Override
    public void submit(ReactorRendererState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.scale(1.0f, -1.0f, -1.0f);
        if (!state.built) {
            var renderType = reactorPartModel.renderType(ReactorPartRenderer.getTexture(state.tier));
            submitNodeCollector.submitModel(reactorPartModel, state, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, null);
        } else {
            poseStack.translate(0.0D, -1.0D, 0.0D);

            var renderType = reactorModel.renderType(Powah.id("textures/model/tile/reactor.png"));
            submitNodeCollector.submitModel(reactorModel, state, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, null);

            if (state.running) {
                var renderTypeOn = reactorModel.renderType(Powah.id("textures/model/tile/reactor.png"));
                submitNodeCollector.submitModel(reactorModel, state, poseStack, renderTypeOn, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, null);
            }

            if (state.hasFuel) {
                var pulsingLightCoords = LightCoordsUtil.addSmoothBlockEmission(state.lightCoords, state.lightPulse);
                var renderTypeFilled = reactorModel.renderType(Powah.id("textures/model/tile/reactor_filled.png"));
                submitNodeCollector.submitModel(reactorModel, state, poseStack, renderTypeFilled, pulsingLightCoords, OverlayTexture.NO_OVERLAY, 0,
                        null);
            }

            if (state.tier != Tier.STARTER) {
                var renderTypeTier = reactorModel.renderType(Powah.id("textures/model/tile/reactor_" + state.tier.getSerializedName() + ".png"));
                submitNodeCollector.submitModel(reactorModel, state, poseStack, renderTypeTier, state.lightCoords, OverlayTexture.NO_OVERLAY, 0,
                        null);
            }
        }
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(ReactorBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos()).inflate(1.0D, 3.0D, 1.0D);
    }
}
