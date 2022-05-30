package owmii.lib.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

class EmptyModel extends Model {
    public EmptyModel(Function<ResourceLocation, RenderType> function) {
        super(function);
    }

    @Override
    public void render(MatrixStack matrix, IVertexBuilder buffer, int light, int ov, float red, float green, float blue, float alpha) {
    }
}
