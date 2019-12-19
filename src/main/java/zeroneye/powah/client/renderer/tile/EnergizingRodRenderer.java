package zeroneye.powah.client.renderer.tile;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import zeroneye.lib.util.math.V3d;
import zeroneye.powah.Powah;
import zeroneye.powah.api.wrench.IWrench;
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

        PlayerEntity player = mc.player;
        ItemStack mainHand = player.getHeldItemMainhand();
        ItemStack offHand = player.getHeldItemOffhand();
        boolean flag = false;

        if (mainHand.getItem() instanceof IWrench) {
            flag = ((IWrench) mainHand.getItem()).getWrenchMode(mainHand).link();
        }

        if (offHand.getItem() instanceof IWrench && !flag) {
            flag = ((IWrench) offHand.getItem()).getWrenchMode(offHand).link();
        }

        V3d pos = V3d.from(rod.getPos());
        V3d orbPos = V3d.from(rod.getOrbPos()).up(0.1D);
        if (rod.hasOrb() && rod.coolDown.ended() || flag) {
            if (!rod.getOrbPos().equals(BlockPos.ZERO)) {
                bindTexture(BEAM_TEXTURE);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                GlStateManager.texParameter(3553, 10242, 10497);
                GlStateManager.texParameter(3553, 10243, 10497);
                GlStateManager.disableCull();
                GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
                float f2 = 1.0F;
                float f3 = f2 * 0.5F % 1.0F;
                Vec3d vec3d2 = pos.subtract(orbPos);
                double d0 = vec3d2.length();
                vec3d2 = vec3d2.normalize();
                float f5 = (float) Math.acos(vec3d2.y);
                float f6 = (float) Math.atan2(vec3d2.z, vec3d2.x);
                GlStateManager.rotatef((((float) Math.PI / 2F) - f6) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(f5 * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
                double d1 = (double) f2 * 0.0D;

                double d12 = Math.cos(d1 + Math.PI) * 0.12D;
                double d13 = Math.sin(d1 + Math.PI) * 0.12D;
                double d14 = Math.cos(d1) * 0.12D;
                double d15 = Math.sin(d1) * 0.12D;

                double d16 = Math.cos(d1 + (Math.PI / 2D)) * 0.12D;
                double d17 = Math.sin(d1 + (Math.PI / 2D)) * 0.12D;
                double d18 = Math.cos(d1 + (Math.PI * 1.5D)) * 0.12D;
                double d19 = Math.sin(d1 + (Math.PI * 1.5D)) * 0.12D;

                double d22 = (double) (f3 - 1.0F);
                double d23 = d0 * 5.05D + d22;

                bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos(d12, 0.0D, d13).tex(1, d23).color(255, 255, 255, 255).endVertex();
                bufferbuilder.pos(d12, -d0, d13).tex(1, d22).color(255, 255, 255, 255).endVertex();
                bufferbuilder.pos(d14, -d0, d15).tex(0.0D, d22).color(255, 255, 255, 255).endVertex();
                bufferbuilder.pos(d14, 0.0D, d15).tex(0.0D, d23).color(255, 255, 255, 255).endVertex();

                bufferbuilder.pos(d16, 0.0D, d17).tex(1, d23).color(255, 255, 255, 255).endVertex();
                bufferbuilder.pos(d16, -d0, d17).tex(1, d22).color(255, 255, 255, 255).endVertex();
                bufferbuilder.pos(d18, -d0, d19).tex(0.0D, d22).color(255, 255, 255, 255).endVertex();
                bufferbuilder.pos(d18, 0.0D, d19).tex(0.0D, d23).color(255, 255, 255, 255).endVertex();

                tessellator.draw();
                GlStateManager.enableCull();
            }
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
