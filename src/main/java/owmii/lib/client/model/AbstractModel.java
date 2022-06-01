package owmii.lib.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.client.renderer.tile.AbstractTileRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Function;

public abstract class AbstractModel<T extends AbstractTileEntity<?, ?>, R extends AbstractTileRenderer<T>> extends EmptyModel {
    public AbstractModel(Function<ResourceLocation, RenderType> function) {
        super(function);
    }

    public abstract void render(T te, R renderer, PoseStack matrix, MultiBufferSource rtb, int light, int ov);

    protected void setRotation(ModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}
