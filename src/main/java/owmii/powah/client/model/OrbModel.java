package owmii.powah.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import owmii.lib.client.model.AbstractModel;
import owmii.lib.client.util.RenderTypes;
import owmii.powah.Powah;
import owmii.powah.block.energizing.EnergizingOrbTile;
import owmii.powah.client.render.tile.EnergizingOrbRenderer;

public class OrbModel extends AbstractModel<EnergizingOrbTile, EnergizingOrbRenderer> {
    final ModelRenderer cube;

    public OrbModel() {
        super(RenderTypes::entityBlendedNoDept);
        this.textureWidth = 20;
        this.textureHeight = 10;

        this.cube = new ModelRenderer(this, 0, 0);
        this.cube.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5);
        this.cube.setRotationPoint(0F, 0F, 0F);
        this.cube.setTextureSize(20, 10);
        this.cube.mirror = true;
        setRotation(this.cube, 0F, 0F, 0F);
    }

    public static final ResourceLocation TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/model/tile/energy_charge.png");

    @Override
    public void render(EnergizingOrbTile te, EnergizingOrbRenderer renderer, MatrixStack matrix, IRenderTypeBuffer rtb, int light, int ov) {
        this.cube.render(matrix, rtb.getBuffer(getRenderType(TEXTURE)), light, ov);
    }
}
