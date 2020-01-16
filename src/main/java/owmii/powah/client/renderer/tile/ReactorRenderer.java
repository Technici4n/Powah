package owmii.powah.client.renderer.tile;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.model.CubeModel;
import owmii.powah.Powah;
import owmii.powah.block.generator.reactor.ReactorTile;
import owmii.powah.client.model.ReactorModel;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class ReactorRenderer extends TileEntityRenderer<ReactorTile> {
    public static final CubeModel CUBE_MODEL = new CubeModel(16);
    private static final ReactorModel REACTOR_MODEL = new ReactorModel();

    @Override
    public void render(ReactorTile te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (!te.hasWorld()) return;
        Minecraft mc = Minecraft.getInstance();
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        GlStateManager.translated(0.5D, 0.5D, 0.5D);
        GlStateManager.scalef(1.0F, -1.0F, -1.0F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        String suffix = Objects.requireNonNull(te.getBlock().getRegistryName()).getPath().substring(7);
        if (!te.isBuilt()) {
            bindTexture(new ResourceLocation(Powah.MOD_ID, "textures/ter/reactor_block" + suffix + ".png"));
            CUBE_MODEL.render();
        } else if (te.isCore()) {
            GlStateManager.translated(0.0D, -1.0D, 0.0D);
            bindTexture(new ResourceLocation(Powah.MOD_ID, "textures/ter/reactor" + suffix + ".png"));
            REACTOR_MODEL.render();
        }
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public boolean isGlobalRenderer(ReactorTile te) {
        return te.isCore() && te.isBuilt();
    }
}
