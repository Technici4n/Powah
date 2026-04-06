package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Arrays;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.block.cable.CableBlockEntity;
import owmii.powah.client.model.CableModel;
import owmii.powah.client.model.PowahLayerDefinitions;
import owmii.powah.lib.logistics.Transfer;
import owmii.powah.util.EnergyUtil;

public class CableRenderer implements BlockEntityRenderer<CableBlockEntity, CableRendererState> {
    private final CableModel model;

    protected CableRenderer(BlockEntityRendererProvider.Context context) {
        model = new CableModel(context.bakeLayer(PowahLayerDefinitions.CABLE));
    }

    @Override
    public CableRendererState createRenderState() {
        return new CableRendererState();
    }

    @Override
    public void extractRenderState(CableBlockEntity blockEntity, CableRendererState state, float partialTicks, Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        Arrays.fill(state.transfer, Transfer.NONE);
        state.tier = blockEntity.getBlock().getTier();

        if (blockEntity.getLevel() == null) {
            return;
        }

        for (Direction side : blockEntity.energySides) {
            final BlockPos pos = blockEntity.getBlockPos().relative(side);
            final BlockEntity tile = blockEntity.getLevel().getBlockEntity(pos);
            final Transfer config = blockEntity.getSideConfig().getType(side);
            if (!(tile instanceof CableBlockEntity) && EnergyUtil.hasEnergy(blockEntity.getLevel(), pos, side.getOpposite())
                    && (config.canExtract || config.canReceive)) {
                state.transfer[side.get3DDataValue()] = blockEntity.getSideConfig().getType(side);
            }
        }
    }

    private RenderType renderType(Tier tier, Transfer transfer) {
        var variant = tier.getSerializedName();
        var texture = switch (transfer) {
        case ALL -> Powah.id("textures/model/tile/energy_cable_%s_all.png".formatted(variant));
        case RECEIVE -> Powah.id("textures/model/tile/energy_cable_%s_out.png".formatted(variant));
        case EXTRACT -> Powah.id("textures/model/tile/energy_cable_%s_in.png".formatted(variant));
        case NONE -> throw new UnsupportedOperationException();
        };
        return model.renderType(texture);
    }

    @Override
    public void submit(CableRendererState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.translate(0.0, -0.125, 0.0);
        poseStack.scale(1.0F, -1.0F, -1.0F);

        for (var side : Direction.values()) {
            var idx = side.get3DDataValue();
            var transfer = state.transfer[idx];
            if (transfer != Transfer.NONE) {
                var renderType = renderType(state.tier, transfer);
                submitNodeCollector.submitModelPart(model.getIndicatorPart(side), poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY,
                        null);
                submitNodeCollector.submitModelPart(model.getPlatePart(side), poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY,
                        null);
            }
        }

        poseStack.popPose();
    }
}
