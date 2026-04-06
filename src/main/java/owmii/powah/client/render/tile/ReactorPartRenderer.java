package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.block.reactor.ReactorPartBlockEntity;
import owmii.powah.client.model.CubeModel;
import owmii.powah.client.model.PowahLayerDefinitions;

public class ReactorPartRenderer implements BlockEntityRenderer<ReactorPartBlockEntity, ReactorRendererState> {
    private final CubeModel reactorPartModel;

    protected ReactorPartRenderer(BlockEntityRendererProvider.Context context) {
        reactorPartModel = new CubeModel(RenderTypes::entitySolid, context.bakeLayer(PowahLayerDefinitions.REACTOR_PART));
    }

    public static Identifier getTexture(Tier tier) {
        return Powah.id("textures/model/tile/reactor_block_" + tier.getSerializedName() + ".png");
    }

    @Override
    public ReactorRendererState createRenderState() {
        return new ReactorRendererState();
    }

    @Override
    public void extractRenderState(ReactorPartBlockEntity blockEntity, ReactorRendererState state, float partialTicks, Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.update(blockEntity);
    }

    @Override
    public void submit(ReactorRendererState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.built)
            return;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.scale(1.0f, -1.0f, -1.0f);
        var renderType = RenderTypes.entitySolid(getTexture(state.tier));
        submitNodeCollector.submitModel(reactorPartModel, state, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, null);
        poseStack.popPose();
    }
}
