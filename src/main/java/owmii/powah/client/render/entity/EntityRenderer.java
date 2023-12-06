package owmii.powah.client.render.entity;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import owmii.powah.entity.Entities;

public class EntityRenderer {
    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Entities.CHARGED_SNOWBALL.get(), ThrownItemRenderer::new);
    }
}
