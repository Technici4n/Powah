package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import owmii.powah.block.energizing.EnergizingOrbBlockEntity;
import owmii.powah.client.model.OrbModel;
import owmii.powah.client.model.PowahLayerDefinitions;
import owmii.powah.lib.logistics.inventory.Inventory;
import owmii.powah.util.math.V3d;

public class EnergizingOrbRenderer implements BlockEntityRenderer<EnergizingOrbBlockEntity, EnergizingOrbRendererState> {
    private final OrbModel model;
    private final ItemModelResolver itemModelResolver;

    protected EnergizingOrbRenderer(BlockEntityRendererProvider.Context context) {
        model = new OrbModel(context.bakeLayer(PowahLayerDefinitions.ORB));
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public EnergizingOrbRendererState createRenderState() {
        return new EnergizingOrbRendererState();
    }

    @Override
    public void extractRenderState(EnergizingOrbBlockEntity blockEntity, EnergizingOrbRendererState state, float partialTicks, Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        Inventory inv = blockEntity.getInventory();
        if (!inv.isEmpty()) {
            state.ticks = (blockEntity.ticks + partialTicks) / 200.0F;
            var output = inv.getStackInSlot(0);
            if (!output.isEmpty()) {
                itemModelResolver.updateForTopItem(state.outputItem, output, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
                state.inputItems = new ItemStackRenderState[0];
            } else {
                state.outputItem.clear();
                var nonEmptyStacks = inv.getNonEmptyStacks();
                state.inputItems = Arrays.copyOf(state.inputItems, nonEmptyStacks.size());

                for (int i = 0; i < nonEmptyStacks.size(); i++) {
                    if (state.inputItems[i] == null) {
                        state.inputItems[i] = new ItemStackRenderState();
                    }
                    itemModelResolver.updateForTopItem(state.inputItems[i], nonEmptyStacks.get(i), ItemDisplayContext.FIXED, blockEntity.getLevel(),
                            null, 0);
                }
            }
        }

        state.rotation = blockEntity.getOrbUp().getRotation();
    }

    @Override
    public void submit(EnergizingOrbRendererState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.outputItem.isEmpty() || state.inputItems.length > 0) {
            if (!state.outputItem.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0.5D, 0.6D, 0.5D);
                poseStack.mulPose(Axis.YP.rotationDegrees(-state.ticks * 360.0F));
                poseStack.scale(0.35F, 0.35F, 0.35F);
                state.outputItem.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            } else {
                var inputs = state.inputItems;
                List<V3d> circled = V3d.from(Vec3.ZERO).circled(inputs.length, 0.1D);
                for (int i = 0; i < circled.size(); i++) {
                    V3d v3d1 = circled.get(i);
                    poseStack.pushPose();
                    if (inputs.length == 1) {
                        poseStack.translate(0.5D, 0.6D, 0.5D);
                    } else {
                        poseStack.translate(v3d1.x + 0.5D, v3d1.y + 0.6D, v3d1.z + 0.5D);
                    }
                    poseStack.scale(0.35F, 0.35F, 0.35F);
                    poseStack.mulPose(Axis.YP.rotationDegrees(-state.ticks * 360.0F));
                    inputs[i].submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                    poseStack.popPose();
                }
            }
        }

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(state.rotation);
        poseStack.translate(0.0D, 0.1D, 0.0D);
        poseStack.scale(1.8F, 1.8F, 1.8F);
        var renderType = model.renderType(OrbModel.TEXTURE);
        submitNodeCollector.submitModel(model, state, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, null);
        poseStack.popPose();
    }
}
