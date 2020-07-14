package owmii.powah.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import owmii.lib.client.model.AbstractModel;
import owmii.lib.client.util.Render;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.client.render.tile.ReactorRenderer;

public class ReactorModel extends AbstractModel<ReactorTile, ReactorRenderer> {
    private final ModelRenderer reactor;

    public ReactorModel() {
        super(RenderType::getEntityTranslucent);
        this.textureWidth = 256;
        this.textureHeight = 128;
        this.reactor = new ModelRenderer(this, 0, 0);
        this.reactor.addBox(-24F, -32F, -24F, 48, 64, 48);
        this.reactor.setRotationPoint(0F, -8F, 0F);
        this.reactor.setTextureSize(256, 128);
        this.reactor.mirror = true;
        setRotation(this.reactor, 0F, 0F, 0F);
    }

    @Override
    public void render(ReactorTile te, ReactorRenderer renderer, MatrixStack matrix, IRenderTypeBuffer rtb, int light, int ov) {
        IVertexBuilder buffer = rtb.getBuffer(getRenderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor.png")));
        this.reactor.render(matrix, buffer, light, ov);

        int i = Render.MAX_LIGHT - light;
        int i1 = (int) (i * te.bright.subSized());
        int br = light + i1;

        if (te.isRunning()) {
            IVertexBuilder buffer_on = rtb.getBuffer(getRenderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor_on.png")));
            this.reactor.render(matrix, buffer_on, light, ov);
        }

        if (!te.fuel.isEmpty()) {
            IVertexBuilder buffer_on = rtb.getBuffer(getRenderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor_filled.png")));
            this.reactor.render(matrix, buffer_on, br, ov);
        }

        if (te.getVariant() != Tier.STARTER) {
            IVertexBuilder buffer_type = rtb.getBuffer(getRenderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor_" + te.getVariant().getName() + ".png")));
            this.reactor.render(matrix, buffer_type, light, ov);
        }
    }
}
