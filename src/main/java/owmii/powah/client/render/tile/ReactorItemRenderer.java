package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Consumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import org.joml.Vector3fc;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.client.model.CubeModel;
import owmii.powah.client.model.PowahLayerDefinitions;

public class ReactorItemRenderer implements NoDataSpecialModelRenderer {
    public static final Identifier ID = Powah.id("reactor");

    private final Tier tier;
    private final CubeModel reactorPartModel;

    public ReactorItemRenderer(BakingContext context, Tier tier) {
        this.tier = tier;
        reactorPartModel = new CubeModel(RenderTypes::entitySolid, context.entityModelSet().bakeLayer(PowahLayerDefinitions.REACTOR_PART));
    }

    @Override
    public void submit(PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            int overlayCoords,
            boolean hasFoil,
            int outlineColor) {

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.scale(1.0f, -1.0f, -1.0f);
        var renderType = RenderTypes.entitySolid(ReactorPartRenderer.getTexture(tier));
        submitNodeCollector.submitModel(reactorPartModel, new Object(), poseStack, renderType, lightCoords, overlayCoords, outlineColor, null);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        var poseStack = new PoseStack();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.scale(1.0f, -1.0f, -1.0f);
        reactorPartModel.root().getExtentsForGui(poseStack, output);
    }

    public record Unbaked(Tier tier) implements SpecialModelRenderer.Unbaked<Void> {
        public static final MapCodec<ReactorItemRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
                Tier.CODEC.fieldOf("tier").forGetter(Unbaked::tier)).apply(builder, Unbaked::new));

        @Override
        public MapCodec<ReactorItemRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ReactorItemRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new ReactorItemRenderer(context, tier);
        }
    }
}
