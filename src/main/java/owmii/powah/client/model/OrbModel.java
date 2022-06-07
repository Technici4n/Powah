package owmii.powah.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import owmii.powah.lib.client.util.RenderTypes;
import owmii.powah.Powah;
import owmii.powah.block.energizing.EnergizingOrbTile;
import owmii.powah.client.render.tile.EnergizingOrbRenderer;

public class OrbModel extends AbstractModel<EnergizingOrbTile, EnergizingOrbRenderer> {
    private static final String CUBE = "cube";

    final ModelPart cube;

    public OrbModel(ModelPart root) {
        super(RenderTypes::entityBlendedNoDept);
        this.cube = root.getChild(CUBE);
    }

    public static LayerDefinition createDefinition() {
        var meshDefinition = new MeshDefinition();
        var root = meshDefinition.getRoot();
        root.addOrReplaceChild(CUBE, CubeListBuilder.create().mirror().addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5), PartPose.ZERO);

        return LayerDefinition.create(meshDefinition, 20, 10);
    }

    public static final ResourceLocation TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/model/tile/energy_charge.png");

    @Override
    public void render(EnergizingOrbTile te, EnergizingOrbRenderer renderer, PoseStack matrix, MultiBufferSource rtb, int light, int ov) {
        this.cube.render(matrix, rtb.getBuffer(renderType(TEXTURE)), light, ov);
    }
}
