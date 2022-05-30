package owmii.lib.client.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

public class RenderTypes extends RenderType {
    protected static final RenderState.TransparencyState BLENDED = new RenderState.TransparencyState("blended", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    protected static final RenderState.TransparencyState BLENDED_NO_DEPT = new RenderState.TransparencyState("blended_no_dept", () -> {
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
    });

    public static RenderType entityBlended(ResourceLocation location) {
        return makeBlend(location, true);
    }

    public static RenderType makeBlend(ResourceLocation location, boolean b) {
        State state = State.getBuilder().texture(new TextureState(location, false, false))
                .transparency(BLENDED).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED)
                .lightmap(LIGHTMAP_DISABLED).build(true);
        return makeType("blend", DefaultVertexFormats.POSITION_COLOR_TEX, 7, 256, true, true, state);
    }

    public static RenderType entityBlendedNoDept(ResourceLocation location) {
        return makeBlendNoDept(location, true);
    }

    public static RenderType makeBlendNoDept(ResourceLocation location, boolean b) {
        State state = State.getBuilder().texture(new TextureState(location, false, false))
                .transparency(BLENDED_NO_DEPT).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED)
                .lightmap(LIGHTMAP_DISABLED).build(true);
        return makeType("blend_bo_dept", DefaultVertexFormats.POSITION_COLOR_TEX, 7, 256, true, true, state);
    }

    public static RenderType getTextBlended(ResourceLocation locationIn) {
        State state = State.getBuilder().texture(new RenderState.TextureState(locationIn, false, false))
                .alpha(DEFAULT_ALPHA)
                .transparency(BLENDED)
                .lightmap(LIGHTMAP_DISABLED).build(false);
        return makeType("text_blended", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, 7, 256, false, true, state);
    }

    public RenderTypes(String s, VertexFormat format, int i, int i1, boolean b, boolean b1, Runnable runnable, Runnable runnable1) {
        super(s, format, i, i1, b, b1, runnable, runnable1);
    }
}
