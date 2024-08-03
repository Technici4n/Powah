package owmii.powah.lib.client.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class RenderTypes extends RenderType {
    protected static final RenderStateShard.TransparencyStateShard BLENDED = new RenderStateShard.TransparencyStateShard("blended", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    protected static final RenderStateShard.TransparencyStateShard BLENDED_NO_DEPT = new RenderStateShard.TransparencyStateShard("blended_no_depth",
            () -> {
                RenderSystem.enableBlend();
                RenderSystem.depthMask(false);
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            }, () -> {
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
                RenderSystem.defaultBlendFunc();
            });

    public static RenderType entityBlendedNoDept(ResourceLocation location) {
        return makeBlendNoDept(location, true);
    }

    public static RenderType makeBlendNoDept(ResourceLocation location, boolean b) {
        CompositeState state = CompositeState.builder().setTextureState(new TextureStateShard(location, false, false))
                .setTransparencyState(BLENDED_NO_DEPT)
                .setShaderState(new RenderStateShard.ShaderStateShard(
                        GameRenderer::getPositionTexColorShader))
                .setCullState(NO_CULL)
                .setLightmapState(NO_LIGHTMAP).createCompositeState(true);
        return create("blend_bo_dept", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS, 256, true, true, state);
    }

    public static RenderType createReactorOverlay(ResourceLocation locationIn) {
        CompositeState state = CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(locationIn, false, false))
                .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                .setTransparencyState(BLENDED)
                .setLightmapState(NO_LIGHTMAP).createCompositeState(false);
        return create("powah_reactor_overlay", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, state);
    }

    public RenderTypes(String s, VertexFormat format, VertexFormat.Mode mode, int i1, boolean b, boolean b1, Runnable runnable, Runnable runnable1) {
        super(s, format, mode, i1, b, b1, runnable, runnable1);
    }
}
