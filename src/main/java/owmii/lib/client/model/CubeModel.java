package owmii.lib.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public class CubeModel extends Model {
    private final ModelRenderer cube;

    public CubeModel(int pixels, Function<ResourceLocation, RenderType> type) {
        super(type);
        this.textureWidth = pixels * 4;
        this.textureHeight = pixels * 2;
        this.cube = new ModelRenderer(this, 0, 0);
        float offset = -(pixels / 2.0F);
        this.cube.addBox(offset, offset, offset, pixels, pixels, pixels);
        this.cube.setRotationPoint(0F, 0F, 0F);
        this.cube.setTextureSize(this.textureWidth, this.textureHeight);
        this.cube.mirror = true;
    }

    @Override
    public void render(MatrixStack matrix, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.cube.render(matrix, buffer, packedLight, packedOverlay);
    }
}
