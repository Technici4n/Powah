package owmii.powah.lib.client.util;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import owmii.powah.lib.logistics.energy.Energy;

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
                    float uMin = sprite.getU0();
                    float uMax = sprite.getU1();
                    float vMin = sprite.getV0();
                    float vMax = sprite.getV1();
                    uMax = uMax - n / 16.0F * (uMax - uMin);
                    vMin = vMin - m / 16.0F * (vMin - vMax);
                    Tesselator tessellator = Tesselator.getInstance();
                    BufferBuilder buffer = tessellator.getBuilder();
                    buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                    buffer.vertex(x, yy + 16, 0).uv(uMin, vMax).endVertex();
                    buffer.vertex(x + w, yy + 16, 0).uv(uMax, vMax).endVertex();
                    buffer.vertex(x + w, yy + m, 0).uv(uMax, vMin).endVertex();
                    buffer.vertex(x, yy + m, 0).uv(uMin, vMin).endVertex();
                    tessellator.end();
                }
            }
        }
    }

    public static void gaugeH(int x, int y, int w, int h, int uvX, int uvY, Energy energy) {
        gaugeH(x, y, w, h, uvX, uvY, energy.getCapacity(), energy.getStored());
    }

    public static void gaugeH(int x, int y, int w, int h, int uvX, int uvY, long cap, long cur) {
        if (cap > 0 && cur > 0) {
            w = (int) (((float) cur / cap) * w);
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder buffer = tessellator.getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            buffer.vertex(x, y + h, 0).uv(uvX, uvY + h).endVertex();
            buffer.vertex(x + w, y + h, 0).uv(uvX + w, uvY + h).endVertex();
            buffer.vertex(x + w, y, 0).uv(uvX + w, uvY).endVertex();
            buffer.vertex(x, y, 0).uv(uvX, uvY).endVertex();
            tessellator.end();
        }
    }

    public static void drawTexturedModalRect(PoseStack poseStack, int x, int y, int u, int v, int width, int height, float zLevel)
    {
        final float uScale = 1f / 0x100;
        final float vScale = 1f / 0x100;

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder wr = tessellator.getBuilder();
        wr.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        Matrix4f matrix = poseStack.last().pose();
        wr.vertex(matrix, x        , y + height, zLevel).uv( u          * uScale, ((v + height) * vScale)).endVertex();
        wr.vertex(matrix, x + width, y + height, zLevel).uv((u + width) * uScale, ((v + height) * vScale)).endVertex();
        wr.vertex(matrix, x + width, y         , zLevel).uv((u + width) * uScale, ( v           * vScale)).endVertex();
        wr.vertex(matrix, x        , y         , zLevel).uv( u          * uScale, ( v           * vScale)).endVertex();
        tessellator.end();
    }
}