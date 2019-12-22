package owmii.powah.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import owmii.powah.entity.ChargedSnowballEntity;

public class EntityRenderer {
    public static void register() {
        final Minecraft mc = Minecraft.getInstance();
        RenderingRegistry.registerEntityRenderingHandler(ChargedSnowballEntity.class, manager -> new SpriteRenderer<>(mc.getRenderManager(), mc.getItemRenderer()));
    }
}
