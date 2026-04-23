package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.block.furnator.FurnatorBlockEntity;
import owmii.powah.lib.client.util.Cube;

public class FurnatorRenderer implements BlockEntityRenderer<FurnatorBlockEntity, FurnatorRendererState> {

    private static final Identifier FURNATOR_LIT = Powah.id("block/furnator_lit");
    private static final SpriteId LIT_MATERIAL = new SpriteId(TextureAtlas.LOCATION_BLOCKS, FURNATOR_LIT);
    private final SpriteGetter spriteSet;

    protected FurnatorRenderer(BlockEntityRendererProvider.Context context) {
        spriteSet = context.sprites();
    }

    @Override
    public FurnatorRendererState createRenderState() {
        return new FurnatorRendererState();
    }

    @Override
    public void extractRenderState(FurnatorBlockEntity blockEntity, FurnatorRendererState state, float partialTicks, Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.burning = blockEntity.isBurning();
        state.side = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public void submit(FurnatorRendererState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.burning) {
            var sprite = spriteSet.get(LIT_MATERIAL);

            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(Axis.XN.rotationDegrees(180.0f));
            poseStack.scale(0.97F, 0.97F, 0.97F);
            var renderType = RenderTypes.text(sprite.atlasLocation());
            submitNodeCollector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
                Cube.create(pose.pose(), buffer).side(state.side).bright().draw(sprite);
            });
            poseStack.popPose();
        }
    }
}
