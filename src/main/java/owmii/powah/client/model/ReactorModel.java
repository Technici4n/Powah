package owmii.powah.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import owmii.lib.client.model.AbstractModel;
import owmii.powah.Powah;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.client.render.tile.ReactorRenderer;

public class ReactorModel extends AbstractModel<ReactorTile, ReactorRenderer> {
    private final ModelRenderer base;
    private final ModelRenderer core;
    private final ModelRenderer top;
    private final ModelRenderer out0;
    private final ModelRenderer out1;
    private final ModelRenderer out2;
    private final ModelRenderer out3;

    public ReactorModel() {
        super(RenderType::getEntitySolid);
        this.textureWidth = 256;
        this.textureHeight = 256;

        this.base = new ModelRenderer(this, 0, 0);
        this.base.addBox(-24F, 0F, -24F, 48, 6, 48);
        this.base.setRotationPoint(0F, 18F, 0F);
        this.base.setTextureSize(256, 256);
        this.base.mirror = true;
        setRotation(this.base, 0F, 0F, 0F);
        this.core = new ModelRenderer(this, 0, 12);
        this.core.addBox(-22F, 0F, -22F, 44, 52, 44);
        this.core.setRotationPoint(0F, -34F, 0F);
        this.core.setTextureSize(256, 256);
        this.core.mirror = true;
        setRotation(this.core, 0F, 0F, 0F);
        this.top = new ModelRenderer(this, 0, 110);
        this.top.addBox(-24F, 0F, -24F, 48, 6, 48);
        this.top.setRotationPoint(0F, -40F, 0F);
        this.top.setTextureSize(256, 256);
        this.top.mirror = true;
        setRotation(this.top, 0F, 0F, 0F);
        this.out0 = new ModelRenderer(this, 178, 57);
        this.out0.addBox(-8F, 0F, 23F, 16, 10, 1);
        this.out0.setRotationPoint(0F, 8F, 0F);
        this.out0.setTextureSize(256, 256);
        this.out0.mirror = true;
        setRotation(this.out0, 0F, 0F, 0F);
        this.out1 = new ModelRenderer(this, 178, 57);
        this.out1.addBox(-8F, 0F, -24F, 16, 10, 1);
        this.out1.setRotationPoint(0F, 8F, 0F);
        this.out1.setTextureSize(256, 256);
        this.out1.mirror = true;
        setRotation(this.out1, 0F, 0F, 0F);
        this.out2 = new ModelRenderer(this, 178, 69);
        this.out2.addBox(-24F, 0F, -8F, 1, 10, 16);
        this.out2.setRotationPoint(0F, 8F, 0F);
        this.out2.setTextureSize(256, 256);
        this.out2.mirror = true;
        setRotation(this.out2, 0F, 0F, 0F);
        this.out3 = new ModelRenderer(this, 178, 69);
        this.out3.addBox(23F, 4F, -8F, 1, 10, 16);
        this.out3.setRotationPoint(0F, 4F, 0F);
        this.out3.setTextureSize(256, 256);
        this.out3.mirror = true;
        setRotation(this.out3, 0F, 0F, 0F);
    }

    @Override
    public void render(ReactorTile te, ReactorRenderer renderer, MatrixStack matrix, IRenderTypeBuffer rtb, int light, int ov) {
        IVertexBuilder buffer = rtb.getBuffer(getRenderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor_" + te.getVariant().getName() + ".png")));
        this.base.render(matrix, buffer, light, ov);
        matrix.push();
        matrix.scale(1.05F, 1.0F, 1.05F);
        this.core.render(matrix, buffer, light, ov);
        matrix.pop();
        this.top.render(matrix, buffer, light, ov);
        this.out0.render(matrix, buffer, light, ov);
        this.out1.render(matrix, buffer, light, ov);
        this.out2.render(matrix, buffer, light, ov);
        this.out3.render(matrix, buffer, light, ov);
    }
}
