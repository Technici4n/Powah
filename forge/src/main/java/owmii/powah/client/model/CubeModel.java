package owmii.powah.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;

public class CubeModel extends Model {
    private static final String CUBE = "cube";

    private final ModelPart cube;

    public CubeModel(Function<ResourceLocation, RenderType> renderLayer, ModelPart root) {
        super(renderLayer);
        this.cube = root.getChild(CUBE);
    }

    public static LayerDefinition createDefinition(int pixels) {
        float offset = -(pixels / 2.0F);

        var meshDefinition = new MeshDefinition();
        var root = meshDefinition.getRoot();
        root.addOrReplaceChild(CUBE, CubeListBuilder.create().mirror().addBox(offset, offset, offset, pixels, pixels, pixels), PartPose.ZERO);

        return LayerDefinition.create(meshDefinition, pixels * 4, pixels * 2);
    }

    @Override
    public void renderToBuffer(PoseStack matrix, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.cube.render(matrix, buffer, packedLight, packedOverlay);
    }
}
