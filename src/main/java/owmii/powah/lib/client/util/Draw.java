package owmii.powah.lib.client.util;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
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
                    // Show only the left w columns and bottom `height` rows of the 16x16 sprite, unscaled.
                    // GuiGraphicsExtractor#innerBlit is private since 26.3, so clip a full blit instead
                    // (this is also how vanilla draws partial sprites).
                    int m = 16 - height;
                    gui.enableScissor(x, yy + m, x + w, yy + 16);
                    gui.blitSprite(pipeline, sprite, x, yy, 16, 16, color);
                    gui.disableScissor();
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
