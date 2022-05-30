package owmii.lib.client.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.client.gui.GuiUtils;
import owmii.lib.logistics.energy.Energy;

public class Draw {
    public static void gaugeV(TextureAtlasSprite sprite, int x, int y, int w, int h, int cap, int cur) {
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
                    float uMin = sprite.getMinU();
                    float uMax = sprite.getMaxU();
                    float vMin = sprite.getMinV();
                    float vMax = sprite.getMaxV();
                    uMax = uMax - n / 16.0F * (uMax - uMin);
                    vMin = vMin - m / 16.0F * (vMin - vMax);
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder buffer = tessellator.getBuffer();
                    buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
                    buffer.pos(x, yy + 16, 0).tex(uMin, vMax).endVertex();
                    buffer.pos(x + w, yy + 16, 0).tex(uMax, vMax).endVertex();
                    buffer.pos(x + w, yy + m, 0).tex(uMax, vMin).endVertex();
                    buffer.pos(x, yy + m, 0).tex(uMin, vMin).endVertex();
                    tessellator.draw();
                }
            }
        }
    }

    public static void gaugeV(int x, int y, int w, int h, int uvX, int uvY, double cap, double cur) {
        if (cap > 0 && cur > 0) {
            int i = (int) (((float) cur / cap) * h);
            GuiUtils.drawTexturedModalRect(x, y + h - i, uvX, uvY + h - i, w, i, 0);
        }
    }

    public static void gaugeV(int x, int y, int w, int h, int uvX, int uvY, Energy energy) {
        gaugeV(x, y, w, h, uvX, uvY, energy.getCapacity(), energy.getStored());
    }

    public static void gaugeV(int x, int y, int w, int h, int uvX, int uvY, long cap, long cur) {
        if (cap > 0 && cur > 0) {
            int i = (int) (((float) cur / cap) * h);
            GuiUtils.drawTexturedModalRect(x, y + h - i, uvX, uvY + h - i, w, i, 0);
        }
    }

    public static void gaugeH(int x, int y, int w, int h, int uvX, int uvY, Energy energy) {
        gaugeH(x, y, w, h, uvX, uvY, energy.getCapacity(), energy.getStored());
    }

    public static void gaugeH(int x, int y, int w, int h, int uvX, int uvY, long cap, long cur) {
        if (cap > 0 && cur > 0) {
            int i = (int) (((float) cur / cap) * w);
            GuiUtils.drawTexturedModalRect(x, y, uvX, uvY, i, h, 0);
        }
    }
}