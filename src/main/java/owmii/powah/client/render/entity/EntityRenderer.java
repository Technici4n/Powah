package owmii.powah.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import owmii.powah.entity.IEntities;

@OnlyIn(Dist.CLIENT)
public class EntityRenderer {
    public static void register() {
        final Minecraft mc = Minecraft.getInstance();
        RenderingRegistry.registerEntityRenderingHandler(IEntities.CHARGED_SNOWBALL, manager -> new SpriteRenderer<>(mc.getRenderManager(), mc.getItemRenderer()));
    }
}
