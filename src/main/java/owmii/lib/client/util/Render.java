package owmii.lib.client.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import owmii.lib.util.math.V3d;

public class Render {
    public static final int MAX_LIGHT = 15728880;

    public static void quad(Matrix4f matrix4f, VertexConsumer buffer, TextureAtlasSprite sprite, float width, float height) {
        quad(matrix4f, buffer, sprite, width, height, MAX_LIGHT, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void quad(Matrix4f matrix4f, VertexConsumer buffer, TextureAtlasSprite sprite, float width, float height, int light) {
        quad(matrix4f, buffer, sprite, width, height, light, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void quad(Matrix4f matrix4f, VertexConsumer buffer, TextureAtlasSprite sprite, float width, float height, float r, float g, float b) {
        quad(matrix4f, buffer, sprite, width, height, MAX_LIGHT, r, g, b, 1.0F);
    }

    public static void quad(Matrix4f matrix4f, VertexConsumer buffer, TextureAtlasSprite sprite, float width, float height, float r, float g, float b, float a) {
        quad(matrix4f, buffer, sprite, width, height, MAX_LIGHT, r, g, b, a);
    }

    public static void quad(Matrix4f matrix4f, VertexConsumer buffer, TextureAtlasSprite sprite, float width, float height, int light, float r, float g, float b, float a) {
        buffer.vertex(matrix4f, 0, 0, height).color(r, g, b, a).uv(sprite.getU0(), sprite.getV1()).uv2(light).endVertex();
        buffer.vertex(matrix4f, width, 0, height).color(r, g, b, a).uv(sprite.getU1(), sprite.getV1()).uv2(light).endVertex();
        buffer.vertex(matrix4f, width, 0, 0).color(r, g, b, a).uv(sprite.getU1(), sprite.getV0()).uv2(light).endVertex();
        buffer.vertex(matrix4f, 0, 0, 0).color(r, g, b, a).uv(sprite.getU0(), sprite.getV0()).uv2(light).endVertex();
    }

    public static void quad(Matrix4f matrix4f, VertexConsumer buffer, float width, float height) {
        quad(matrix4f, buffer, width, height, MAX_LIGHT, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void quad(Matrix4f matrix4f, VertexConsumer buffer, float width, float height, int light) {
        quad(matrix4f, buffer, width, height, light, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void quad(Matrix4f matrix4f, VertexConsumer buffer, float width, float height, float r, float g, float b) {
        quad(matrix4f, buffer, width, height, MAX_LIGHT, r, g, b, 1.0F);
    }

    public static void quad(Matrix4f matrix4f, VertexConsumer buffer, float width, float height, float r, float g, float b, float a) {
        quad(matrix4f, buffer, width, height, MAX_LIGHT, r, g, b, a);
    }

    public static void quad(Matrix4f matrix4f, VertexConsumer buffer, float width, float height, int light, float r, float g, float b, float a) {
        buffer.vertex(matrix4f, 0, 0, height).color(r, g, b, a).uv(0.0F, 1.0F).uv2(light).endVertex();
        buffer.vertex(matrix4f, width, 0, height).color(r, g, b, a).uv(1.0F, 1.0F).uv2(light).endVertex();
        buffer.vertex(matrix4f, width, 0, 0).color(r, g, b, a).uv(1.0F, 0.0F).uv2(light).endVertex();
        buffer.vertex(matrix4f, 0, 0, 0).color(r, g, b, a).uv(0.0F, 0.0F).uv2(light).endVertex();
    }

    public static void cube(Matrix4f matrix4f, VertexConsumer builder, V3d v3d, TextureAtlasSprite sprite, double size, int light) {
        cube(matrix4f, builder, v3d, sprite, size, light, 1.0F);
    }

    public static void cube(Matrix4f matrix4f, VertexConsumer builder, V3d v3d, TextureAtlasSprite sprite, double size, int light, float a) {
        cube(matrix4f, builder, v3d, sprite, size, light, 1.0F, 1.0F, 1.0F, a);
    }

    public static void cube(Matrix4f matrix4f, VertexConsumer builder, V3d v3d, TextureAtlasSprite sprite, double size, int light, float r, float g, float b, float a) {
        final float d = (float) (size / 2.0d);
        builder.vertex(matrix4f, (float) v3d.x() - d, (float) v3d.y() - d, (float) v3d.z() - d).color(r, g, b, a).uv(sprite.getU0(), sprite.getV0()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() + d, (float) v3d.y() - d, (float) v3d.z() - d).color(r, g, b, a).uv(sprite.getU1(), sprite.getV0()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() + d, (float) v3d.y() - d, (float) v3d.z() + d).color(r, g, b, a).uv(sprite.getU1(), sprite.getV1()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() - d, (float) v3d.y() - d, (float) v3d.z() + d).color(r, g, b, a).uv(sprite.getU0(), sprite.getV1()).uv2(light).endVertex();

        builder.vertex(matrix4f, (float) v3d.x() - d, (float) v3d.y() + d, (float) v3d.z() + d).color(r, g, b, a).uv(sprite.getU0(), sprite.getV0()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() + d, (float) v3d.y() + d, (float) v3d.z() + d).color(r, g, b, a).uv(sprite.getU1(), sprite.getV0()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() + d, (float) v3d.y() + d, (float) v3d.z() - d).color(r, g, b, a).uv(sprite.getU1(), sprite.getV1()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() - d, (float) v3d.y() + d, (float) v3d.z() - d).color(r, g, b, a).uv(sprite.getU0(), sprite.getV1()).uv2(light).endVertex();

        builder.vertex(matrix4f, (float) v3d.x() - d, (float) v3d.y() - d, (float) v3d.z() + d).color(r, g, b, a).uv(sprite.getU1(), sprite.getV0()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() - d, (float) v3d.y() + d, (float) v3d.z() + d).color(r, g, b, a).uv(sprite.getU1(), sprite.getV1()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() - d, (float) v3d.y() + d, (float) v3d.z() - d).color(r, g, b, a).uv(sprite.getU0(), sprite.getV1()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() - d, (float) v3d.y() - d, (float) v3d.z() - d).color(r, g, b, a).uv(sprite.getU0(), sprite.getV0()).uv2(light).endVertex();

        builder.vertex(matrix4f, (float) v3d.x() + d, (float) v3d.y() - d, (float) v3d.z() - d).color(r, g, b, a).uv(sprite.getU1(), sprite.getV0()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() + d, (float) v3d.y() + d, (float) v3d.z() - d).color(r, g, b, a).uv(sprite.getU1(), sprite.getV1()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() + d, (float) v3d.y() + d, (float) v3d.z() + d).color(r, g, b, a).uv(sprite.getU0(), sprite.getV1()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() + d, (float) v3d.y() - d, (float) v3d.z() + d).color(r, g, b, a).uv(sprite.getU0(), sprite.getV0()).uv2(light).endVertex();

        builder.vertex(matrix4f, (float) v3d.x() + d, (float) v3d.y() - d, (float) v3d.z() + d).color(r, g, b, a).uv(sprite.getU0(), sprite.getV0()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() + d, (float) v3d.y() + d, (float) v3d.z() + d).color(r, g, b, a).uv(sprite.getU0(), sprite.getV1()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() - d, (float) v3d.y() + d, (float) v3d.z() + d).color(r, g, b, a).uv(sprite.getU1(), sprite.getV1()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() - d, (float) v3d.y() - d, (float) v3d.z() + d).color(r, g, b, a).uv(sprite.getU1(), sprite.getV0()).uv2(light).endVertex();

        builder.vertex(matrix4f, (float) v3d.x() - d, (float) v3d.y() - d, (float) v3d.z() - d).color(r, g, b, a).uv(sprite.getU0(), sprite.getV0()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() - d, (float) v3d.y() + d, (float) v3d.z() - d).color(r, g, b, a).uv(sprite.getU0(), sprite.getV1()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() + d, (float) v3d.y() + d, (float) v3d.z() - d).color(r, g, b, a).uv(sprite.getU1(), sprite.getV1()).uv2(light).endVertex();
        builder.vertex(matrix4f, (float) v3d.x() + d, (float) v3d.y() - d, (float) v3d.z() - d).color(r, g, b, a).uv(sprite.getU1(), sprite.getV0()).uv2(light).endVertex();

    }

    public static void cube(Matrix4f matrix4f, VertexConsumer builder, V3d v3d, double size, float u, float v, int light) {
        cube(matrix4f, builder, v3d, size, u, v, light, 1.0F);
    }

    public static void cube(Matrix4f matrix4f, VertexConsumer builder, V3d v3d, double size, float u, float v, int light, float a) {
        cube(matrix4f, builder, v3d, size, u, v, light, 1.0F, 1.0F, 1.0F, a);
    }

    public static void cube(Matrix4f matrix4f, VertexConsumer builder, V3d v3d, double size, float u, float v, int light, float r, float g, float b, float a) {
        double half = size / 2D;
        builder.vertex((float) v3d.x() - half, (float) v3d.y() - half, (float) v3d.z() - half).color(r, g, b, a).uv(0.0f, 0.0f).uv2(light).endVertex();
        builder.vertex((float) v3d.x() + half, (float) v3d.y() - half, (float) v3d.z() - half).color(r, g, b, a).uv(u, 0.0f).uv2(light).endVertex();
        builder.vertex((float) v3d.x() + half, (float) v3d.y() - half, (float) v3d.z() + half).color(r, g, b, a).uv(u, v).uv2(light).endVertex();
        builder.vertex((float) v3d.x() - half, (float) v3d.y() - half, (float) v3d.z() + half).color(r, g, b, a).uv(0.0f, v).uv2(light).endVertex();

        builder.vertex((float) v3d.x() - half, (float) v3d.y() + half, (float) v3d.z() + half).color(r, g, b, a).uv(0.0f, 0.0f).uv2(light).endVertex();
        builder.vertex((float) v3d.x() + half, (float) v3d.y() + half, (float) v3d.z() + half).color(r, g, b, a).uv(u, 0.0f).uv2(light).endVertex();
        builder.vertex((float) v3d.x() + half, (float) v3d.y() + half, (float) v3d.z() - half).color(r, g, b, a).uv(u, v).uv2(light).endVertex();
        builder.vertex((float) v3d.x() - half, (float) v3d.y() + half, (float) v3d.z() - half).color(r, g, b, a).uv(0.0f, v).uv2(light).endVertex();

        builder.vertex((float) v3d.x() - half, (float) v3d.y() - half, (float) v3d.z() + half).color(r, g, b, a).uv(u, 0.0f).uv2(light).endVertex();
        builder.vertex((float) v3d.x() - half, (float) v3d.y() + half, (float) v3d.z() + half).color(r, g, b, a).uv(u, v).uv2(light).endVertex();
        builder.vertex((float) v3d.x() - half, (float) v3d.y() + half, (float) v3d.z() - half).color(r, g, b, a).uv(0.0f, v).uv2(light).endVertex();
        builder.vertex((float) v3d.x() - half, (float) v3d.y() - half, (float) v3d.z() - half).color(r, g, b, a).uv(0.0f, 0.0f).uv2(light).endVertex();

        builder.vertex((float) v3d.x() + half, (float) v3d.y() - half, (float) v3d.z() - half).color(r, g, b, a).uv(u, 0.0f).uv2(light).endVertex();
        builder.vertex((float) v3d.x() + half, (float) v3d.y() + half, (float) v3d.z() - half).color(r, g, b, a).uv(u, v).uv2(light).endVertex();
        builder.vertex((float) v3d.x() + half, (float) v3d.y() + half, (float) v3d.z() + half).color(r, g, b, a).uv(0.0f, v).uv2(light).endVertex();
        builder.vertex((float) v3d.x() + half, (float) v3d.y() - half, (float) v3d.z() + half).color(r, g, b, a).uv(0.0f, 0.0f).uv2(light).endVertex();

        builder.vertex((float) v3d.x() + half, (float) v3d.y() - half, (float) v3d.z() - half).color(r, g, b, a).uv(0.0f, 0.0f).uv2(light).endVertex();
        builder.vertex((float) v3d.x() + half, (float) v3d.y() + half, (float) v3d.z() - half).color(r, g, b, a).uv(0.0f, v).uv2(light).endVertex();
        builder.vertex((float) v3d.x() - half, (float) v3d.y() + half, (float) v3d.z() - half).color(r, g, b, a).uv(u, v).uv2(light).endVertex();
        builder.vertex((float) v3d.x() - half, (float) v3d.y() - half, (float) v3d.z() - half).color(r, g, b, a).uv(u, 0.0f).uv2(light).endVertex();

        builder.vertex((float) v3d.x() - half, (float) v3d.y() - half, (float) v3d.z() + half).color(r, g, b, a).uv(0.0f, 0.0f).uv2(light).endVertex();
        builder.vertex((float) v3d.x() - half, (float) v3d.y() + half, (float) v3d.z() + half).color(r, g, b, a).uv(0.0f, v).uv2(light).endVertex();
        builder.vertex((float) v3d.x() + half, (float) v3d.y() + half, (float) v3d.z() + half).color(r, g, b, a).uv(u, v).uv2(light).endVertex();
        builder.vertex((float) v3d.x() + half, (float) v3d.y() - half, (float) v3d.z() + half).color(r, g, b, a).uv(u, 0.0f).uv2(light).endVertex();

    }
}
