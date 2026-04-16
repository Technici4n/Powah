package owmii.powah.lib.client.util;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import owmii.powah.Powah;

public class RenderTypes {
    public static final RenderPipeline GUI_TEXTURED_NOBLEND = RenderPipelines.GUI_TEXTURED.toBuilder()
            .withColorTargetState(ColorTargetState.DEFAULT)
            .withLocation(Powah.id("gui_textured_noblend"))
            .build();

    public static RenderPipeline REACTOR_OVERLAY = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withSampler("Sampler0")
            .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
            .withLocation(Powah.id("reactor_overlay"))
            .build();

    public static RenderPipeline BLENDED_NO_DEPTH = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withSampler("Sampler0")
            .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
            .withCull(false)
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
            .withLocation(Powah.id("blended_no_depth"))
            .build();

    public static RenderType entityBlendedNoDepthWrite(Identifier location) {
        return RenderType.create("powah_blended_no_depth", RenderSetup.builder(BLENDED_NO_DEPTH)
                .withTexture("Sampler0", location)
                .bufferSize(256)
                .sortOnUpload()
                .affectsCrumbling()
                .createRenderSetup());
    }

    public static RenderType createReactorOverlay(Identifier location) {
        return RenderType.create("powah_reactor_overlay", RenderSetup.builder(REACTOR_OVERLAY)
                .withTexture("Sampler0", location)
                .bufferSize(256)
                .sortOnUpload()
                .createRenderSetup());
    }
}
