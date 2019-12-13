package zeroneye.powah.client.renderer.tile;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import zeroneye.lib.util.math.V3d;
import zeroneye.powah.Powah;
import zeroneye.powah.block.energizing.EnergizingRodTile;
import zeroneye.powah.client.model.EnergyChargeModel;

@OnlyIn(Dist.CLIENT)
public class EnergizingRodRenderer extends TileEntityRenderer<EnergizingRodTile> {
    public static final EnergyChargeModel CHARGE_MODEL = new EnergyChargeModel();
    public static final ResourceLocation CHARGE_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/ter/energy_charge.png");
    public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/ter/beam.png");

    @Override
    public void render(EnergizingRodTile rod, double x, double y, double z, float partialTicks, int destroyStage) {
        Minecraft mc = Minecraft.getInstance();
        EntityRendererManager rm = mc.getRenderManager();
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.depthMask(false);
        float f0 = Math.max(rod.coolDown.getTicks() / (float) (rod.coolDown.getMax()), 0.0F);
        float f1 = 0.5F + f0 / 5.0F;
        GlStateManager.translated(0.5D, 0.5D, 0.5D);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.scalef(f1, f1, f1);
        bindTexture(CHARGE_TEXTURE);
        CHARGE_MODEL.render(0.0625F);
        GlStateManager.popMatrix();

        if (rod.hasOrb() && rod.coolDown.ended()) {
            bindTexture(BEAM_TEXTURE);
            V3d pos = V3d.from(rod.getPos());
            V3d orbPos = V3d.from(rod.getOrbPos()).up(0.1D);
            Vec3d vec3d = pos.subtract(orbPos);

            double d0 = orbPos.x - (double) ((float) pos.x);
            double d1 = orbPos.z - (double) ((float) pos.z);
            double d2 = orbPos.y - (double) ((float) pos.y);

            GlStateManager.disableCull();
            BufferBuilder wr = Tessellator.getInstance().getBuffer();
            Tessellator ts = Tessellator.getInstance();
            wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

            wr.pos(0, -0.1F, 0).tex(0, 1).endVertex();
            wr.pos(d0, -0.1F + d2, d1).tex(1, 1).endVertex();
            wr.pos(d0, 0.1F + d2, d1).tex(0, 0).endVertex();
            wr.pos(0, 0.1F, 0).tex(1, 0).endVertex();

            ts.draw();
            GlStateManager.enableCull();
        }

        RenderHelper.enableStandardItemLighting();
        GlStateManager.depthMask(true);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public boolean isGlobalRenderer(EnergizingRodTile te) {
        return te.hasOrb() && te.coolDown.ended();
    }
}
