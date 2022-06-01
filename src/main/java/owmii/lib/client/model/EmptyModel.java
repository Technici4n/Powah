package owmii.lib.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;

class EmptyModel extends Model {
    public EmptyModel(Function<ResourceLocation, RenderType> function) {
        super(function);
    }

    @Override
    public void renderToBuffer(PoseStack matrix, VertexConsumer buffer, int light, int ov, float red, float green, float blue, float alpha) {
    }
}
