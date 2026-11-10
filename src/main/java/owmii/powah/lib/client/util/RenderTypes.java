package owmii.powah.lib.client.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.renderer.BindGroupLayouts;
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

    public static RenderPipeline REACTOR_OVERLAY = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withLocation(Powah.id("reactor_overlay"))
            .build();

    public static RenderPipeline BLENDED_NO_DEPTH = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
            .withCull(false)
            // 26.2 uses reverse-Z depth, so the old LESS_THAN_OR_EQUAL test becomes GREATER_THAN_OR_EQUAL
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withLocation(Powah.id("blended_no_depth"))
            .build();

    public static RenderType entityBlendedNoDepthWrite(Identifier location) {
        return RenderType.create("powah_blended_no_depth", RenderSetup.builder(BLENDED_NO_DEPTH)
                .withTexture("Sampler0", location)
                .sortOnUpload()
                .affectsCrumbling()
                .createRenderSetup());
    }

    public static RenderType createReactorOverlay(Identifier location) {
        return RenderType.create("powah_reactor_overlay", RenderSetup.builder(REACTOR_OVERLAY)
                .withTexture("Sampler0", location)
                .sortOnUpload()
                .createRenderSetup());
    }
}
