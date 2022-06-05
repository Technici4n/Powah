package owmii.powah.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import owmii.lib.client.model.AbstractModel;
import owmii.lib.client.util.Render;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.client.render.tile.ReactorRenderer;

// TODO PORT fix
public class ReactorModel extends AbstractModel<ReactorTile, ReactorRenderer> {
    //private final ModelPart reactor;

    public ReactorModel() {
        super(RenderType::entityTranslucent);
        /*
        this.texWidth = 256;
        this.texHeight = 128;
        this.reactor = new ModelPart(this, 0, 0);
        this.reactor.addBox(-24F, -32F, -24F, 48, 64, 48);
        this.reactor.setPos(0F, -8F, 0F);
        this.reactor.setTexSize(256, 128);
        this.reactor.mirror = true;
        setRotation(this.reactor, 0F, 0F, 0F);
         */
    }

    @Override
    public void render(ReactorTile te, ReactorRenderer renderer, PoseStack matrix, MultiBufferSource rtb, int light, int ov) {
        /*
        VertexConsumer buffer = rtb.getBuffer(renderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor.png")));
        this.reactor.render(matrix, buffer, light, ov);

        int i = Render.MAX_LIGHT - light;
        int i1 = (int) (i * te.bright.subSized());
        int br = light + i1;

        if (te.isRunning()) {
            VertexConsumer buffer_on = rtb.getBuffer(renderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor_on.png")));
            this.reactor.render(matrix, buffer_on, light, ov);
        }

        if (!te.fuel.isEmpty()) {
            VertexConsumer buffer_on = rtb.getBuffer(renderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor_filled.png")));
            this.reactor.render(matrix, buffer_on, br, ov);
        }

        if (te.getVariant() != Tier.STARTER) {
            VertexConsumer buffer_type = rtb.getBuffer(renderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor_" + te.getVariant().getName() + ".png")));
            this.reactor.render(matrix, buffer_type, light, ov);
        }
         */
    }
}
