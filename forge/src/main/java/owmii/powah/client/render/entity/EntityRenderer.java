package owmii.powah.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.powah.entity.Entities;

@OnlyIn(Dist.CLIENT)
public class EntityRenderer {
    public static void register() {
        EntityRenderers.register(Entities.CHARGED_SNOWBALL.get(), ThrownItemRenderer::new);
    }
}
