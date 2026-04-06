package owmii.powah.lib.client.util;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import owmii.powah.lib.logistics.energy.Energy;

public class Draw {

    public static void gaugeV(GuiGraphicsExtractor gui, RenderPipeline pipeline, TextureAtlasSprite sprite, int x, int y, int w, int h, int cap,
            int cur, int color) {
        if (cap > 0 && cur > 0) {
            int i = (int) (((float) cur / cap) * h);
            final int j = i / 16;
            final int k = i - j * 16;
            for (int l = 0; l <= j; l++) {
                int height = l == j ? k : 16;
                int yy = (y - (l + 1) * 16) + h;
                if (height > 0) {
                    int m = 16 - height;
                    int n = 16 - w;
                    float uMin = sprite.getU0();
                    float uMax = sprite.getU1();
                    float vMin = sprite.getV0();
                    float vMax = sprite.getV1();
                    uMax = uMax - n / 16.0F * (uMax - uMin);
                    vMin = vMin - m / 16.0F * (vMin - vMax);
                    gui.innerBlit(
                            pipeline,
                            sprite.atlasLocation(),
                            x, x + w,
                            yy + m, yy + 16,
                            uMin, uMax,
                            vMin, vMax,
                            color);
                }
            }
        }
    }

    public static void gaugeH(GuiGraphicsExtractor gui, Identifier texture, int x, int y, int w, int h, int uvX, int uvY, Energy energy) {
        gaugeH(gui, texture, x, y, w, h, uvX, uvY, energy.getCapacity(), energy.getStored());
    }

    public static void gaugeH(GuiGraphicsExtractor gui, Identifier texture, int x, int y, int w, int h, int uvX, int uvY, long cap, long cur) {
        if (cap > 0 && cur > 0) {
            w = (int) (((float) cur / cap) * w);
            gui.blit(
                    RenderPipelines.GUI_TEXTURED,
                    texture,
                    x, y,
                    uvX, uvY,
                    w, h,
                    w, h,
                    256, 256,
                    -1);
        }
    }
}
