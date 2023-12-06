package owmii.powah.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.lib.client.renderer.tile.AbstractTileRenderer;

public abstract class AbstractModel<T extends AbstractTileEntity<?, ?>, R extends AbstractTileRenderer<T>> extends Model {
    public AbstractModel(Function<ResourceLocation, RenderType> function) {
        super(function);
    }

    public abstract void render(T te, R renderer, PoseStack matrix, MultiBufferSource rtb, int light, int ov);

    @Override
    public void renderToBuffer(PoseStack matrix, VertexConsumer buffer, int light, int ov, float red, float green, float blue, float alpha) {
    }
}
