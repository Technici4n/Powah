package owmii.powah.lib.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import owmii.powah.lib.util.math.V3d;

public class Cube {
    private final PoseStack matrix;
    private final VertexConsumer builder;
    private Set<Side> sides = new HashSet<>();
    private V3d pos = V3d.ZERO;
    private double size = 1.0D;
    private float r = 1.0F;
    private float b = 1.0F;
    private float g = 1.0F;
    private float a = 1.0F;
    private int light;

    Cube(PoseStack matrix, VertexConsumer builder) {
        this.matrix = matrix;
        this.builder = builder;
    }

    public static Cube create(PoseStack stack, VertexConsumer builder) {
        return new Cube(stack, builder);
    }

    public Cube size(double size) {
        this.size = size;
        return this;
    }

    public Cube pos(double x, double y, double z) {
        return pos(new V3d(x, y, z));
    }

    public Cube pos(V3d pos) {
        this.pos = pos;
        return this;
    }

    public Cube bright() {
        light(15728880);
        return this;
    }

    public Cube light(int light) {
        this.light = light;
        return this;
    }

    public Cube side(BlockState state) {
        this.sides.add(Side.from(state.getValue(BlockStateProperties.FACING)));
        return this;
    }

    public Cube side(Direction side) {
        this.sides.add(Side.from(side));
        return this;
    }

    public Cube side(Side side) {
        this.sides.add(side);
        return this;
    }

    public Cube color(int color) {
        return color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F);
    }

    public Cube color(float r, float g, float b) {
        return color(r, g, b, 1.0F);
    }

    public Cube color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    public Cube alpha(float alpha) {
        this.a = alpha;
        return this;
    }

    public void draw(TextureAtlasSprite sprite) {
        final float d = (float) (this.size / 2.0d);
        var matrix4f = this.matrix.last().pose();
        boolean isAll = this.sides.isEmpty();

        if (isAll || this.sides.contains(Side.UP)) {
            this.builder.vertex(matrix4f, (float) this.pos.x() - d, (float) this.pos.y() + d, (float) this.pos.z() + d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU0(), sprite.getV0()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() + d, (float) this.pos.y() + d, (float) this.pos.z() + d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU1(), sprite.getV0()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() + d, (float) this.pos.y() + d, (float) this.pos.z() - d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU1(), sprite.getV1()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() - d, (float) this.pos.y() + d, (float) this.pos.z() - d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU0(), sprite.getV1()).uv2(this.light).endVertex();
        }

        if (isAll || this.sides.contains(Side.DOWN)) {
            this.builder.vertex(matrix4f, (float) this.pos.x() - d, (float) this.pos.y() - d, (float) this.pos.z() - d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU0(), sprite.getV0()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() + d, (float) this.pos.y() - d, (float) this.pos.z() - d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU1(), sprite.getV0()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() + d, (float) this.pos.y() - d, (float) this.pos.z() + d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU1(), sprite.getV1()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() - d, (float) this.pos.y() - d, (float) this.pos.z() + d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU0(), sprite.getV1()).uv2(this.light).endVertex();
        }

        if (isAll || this.sides.contains(Side.NORTH)) {
            this.builder.vertex(matrix4f, (float) this.pos.x() + d, (float) this.pos.y() - d, (float) this.pos.z() + d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU0(), sprite.getV0()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() + d, (float) this.pos.y() + d, (float) this.pos.z() + d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU0(), sprite.getV1()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() - d, (float) this.pos.y() + d, (float) this.pos.z() + d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU1(), sprite.getV1()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() - d, (float) this.pos.y() - d, (float) this.pos.z() + d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU1(), sprite.getV0()).uv2(this.light).endVertex();
        }

        if (isAll || this.sides.contains(Side.SOUTH)) {
            this.builder.vertex(matrix4f, (float) this.pos.x() - d, (float) this.pos.y() - d, (float) this.pos.z() - d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU0(), sprite.getV0()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() - d, (float) this.pos.y() + d, (float) this.pos.z() - d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU0(), sprite.getV1()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() + d, (float) this.pos.y() + d, (float) this.pos.z() - d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU1(), sprite.getV1()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() + d, (float) this.pos.y() - d, (float) this.pos.z() - d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU1(), sprite.getV0()).uv2(this.light).endVertex();
        }

        if (isAll || this.sides.contains(Side.WEST)) {
            this.builder.vertex(matrix4f, (float) this.pos.x() - d, (float) this.pos.y() - d, (float) this.pos.z() + d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU1(), sprite.getV0()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() - d, (float) this.pos.y() + d, (float) this.pos.z() + d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU1(), sprite.getV1()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() - d, (float) this.pos.y() + d, (float) this.pos.z() - d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU0(), sprite.getV1()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() - d, (float) this.pos.y() - d, (float) this.pos.z() - d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU0(), sprite.getV0()).uv2(this.light).endVertex();
        }

        if (isAll || this.sides.contains(Side.EAST)) {
            this.builder.vertex(matrix4f, (float) this.pos.x() + d, (float) this.pos.y() - d, (float) this.pos.z() - d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU1(), sprite.getV0()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() + d, (float) this.pos.y() + d, (float) this.pos.z() - d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU1(), sprite.getV1()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() + d, (float) this.pos.y() + d, (float) this.pos.z() + d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU0(), sprite.getV1()).uv2(this.light).endVertex();
            this.builder.vertex(matrix4f, (float) this.pos.x() + d, (float) this.pos.y() - d, (float) this.pos.z() + d)
                    .color(this.r, this.g, this.b, this.a).uv(sprite.getU0(), sprite.getV0()).uv2(this.light).endVertex();
        }
    }

    public enum Side {
        ALL,
        DOWN,
        UP,
        NORTH,
        SOUTH,
        WEST,
        EAST;

        public static Side from(Direction side) {
            return values()[side.ordinal() + 1];
        }
    }
}
