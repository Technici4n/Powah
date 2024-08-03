package owmii.powah.lib.client.util;

import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import owmii.powah.lib.logistics.energy.Energy;

public class Draw {
    public static void gaugeV(TextureAtlasSprite sprite, int x, int y, int w, int h, int cap, int cur) {
        if (cap > 0 && cur > 0) {
            var buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
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
                    buffer.addVertex(x, yy + 16, 0).setUv(uMin, vMax);
                    buffer.addVertex(x + w, yy + 16, 0).setUv(uMax, vMax);
                    buffer.addVertex(x + w, yy + m, 0).setUv(uMax, vMin);
                    buffer.addVertex(x, yy + m, 0).setUv(uMin, vMin);
                }
            }
            var mesh = buffer.build();
            if (mesh != null) {
                BufferUploader.drawWithShader(mesh);
            }
        }
    }

    public static void gaugeH(int x, int y, int w, int h, int uvX, int uvY, Energy energy) {
        gaugeH(x, y, w, h, uvX, uvY, energy.getCapacity(), energy.getStored());
    }

    public static void gaugeH(int x, int y, int w, int h, int uvX, int uvY, long cap, long cur) {
        if (cap > 0 && cur > 0) {
            w = (int) (((float) cur / cap) * w);
            var buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            buffer.addVertex(x, y + h, 0).setUv(uvX, uvY + h);
            buffer.addVertex(x + w, y + h, 0).setUv(uvX + w, uvY + h);
            buffer.addVertex(x + w, y, 0).setUv(uvX + w, uvY);
            buffer.addVertex(x, y, 0).setUv(uvX, uvY);
            BufferUploader.drawWithShader(buffer.buildOrThrow());
        }
    }

    public static void drawTexturedModalRect(GuiGraphics gui, int x, int y, int u, int v, int width, int height, float zLevel) {
        final float uScale = 1f / 0x100;
        final float vScale = 1f / 0x100;

        var wr = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        var matrix = gui.pose().last().pose();
        wr.addVertex(matrix, x, y + height, zLevel).setUv(u * uScale, ((v + height) * vScale));
        wr.addVertex(matrix, x + width, y + height, zLevel).setUv((u + width) * uScale, ((v + height) * vScale));
        wr.addVertex(matrix, x + width, y, zLevel).setUv((u + width) * uScale, (v * vScale));
        wr.addVertex(matrix, x, y, zLevel).setUv(u * uScale, (v * vScale));
        BufferUploader.drawWithShader(wr.buildOrThrow());
    }
}
