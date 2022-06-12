package owmii.powah.client.render.entity;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import owmii.powah.entity.Entities;

public class EntityRenderer {
    public static void register() {
        EntityRendererRegistry.register(Entities.CHARGED_SNOWBALL, ThrownItemRenderer::new);
    }
}
