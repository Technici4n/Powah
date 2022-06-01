package owmii.powah.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import owmii.lib.client.model.AbstractModel;
import owmii.lib.client.util.RenderTypes;
import owmii.powah.Powah;
import owmii.powah.block.energizing.EnergizingOrbTile;
import owmii.powah.client.render.tile.EnergizingOrbRenderer;

public class OrbModel extends AbstractModel<EnergizingOrbTile, EnergizingOrbRenderer> {
    final ModelPart cube;

    public OrbModel() {
        super(RenderTypes::entityBlendedNoDept);
        this.texWidth = 20;
        this.texHeight = 10;

        this.cube = new ModelPart(this, 0, 0);
        this.cube.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5);
        this.cube.setPos(0F, 0F, 0F);
        this.cube.setTexSize(20, 10);
        this.cube.mirror = true;
        setRotation(this.cube, 0F, 0F, 0F);
    }

    public static final ResourceLocation TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/model/tile/energy_charge.png");

    @Override
    public void render(EnergizingOrbTile te, EnergizingOrbRenderer renderer, PoseStack matrix, MultiBufferSource rtb, int light, int ov) {
        this.cube.render(matrix, rtb.getBuffer(renderType(TEXTURE)), light, ov);
    }
}
