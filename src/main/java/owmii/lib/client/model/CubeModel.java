package owmii.lib.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;

// TODO PORT Fix
public class CubeModel extends Model {
    //private final ModelPart cube;

    public CubeModel(int pixels, Function<ResourceLocation, RenderType> type) {
        super(type);
        /*
        this.texWidth = pixels * 4;
        this.texHeight = pixels * 2;
        this.cube = new ModelPart(this, 0, 0);
        float offset = -(pixels / 2.0F);
        this.cube.addBox(offset, offset, offset, pixels, pixels, pixels);
        this.cube.setPos(0F, 0F, 0F);
        this.cube.setTexSize(this.texWidth, this.texHeight);
        this.cube.mirror = true;
         */
    }

    @Override
    public void renderToBuffer(PoseStack matrix, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        //this.cube.render(matrix, buffer, packedLight, packedOverlay);
    }
}
