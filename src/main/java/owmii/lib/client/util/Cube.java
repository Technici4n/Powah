package owmii.lib.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix4f;
import owmii.lib.util.math.V3d;

import java.util.HashSet;
import java.util.Set;

public class Cube {
    private final MatrixStack matrix;
    private final IVertexBuilder builder;
    private Set<Side> sides = new HashSet<>();
    private V3d pos = V3d.ZERO;
    private double size = 1.0D;
    private float r = 1.0F;
    private float b = 1.0F;
    private float g = 1.0F;
    private float a = 1.0F;
    private int light;

    Cube(MatrixStack matrix, IVertexBuilder builder) {
        this.matrix = matrix;
        this.builder = builder;
    }

    public static Cube create(MatrixStack stack, IVertexBuilder builder) {
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
        this.sides.add(Side.from(state.get(BlockStateProperties.FACING)));
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
        Matrix4f matrix4f = this.matrix.getLast().getMatrix();
        boolean isAll = this.sides.isEmpty();

        if (isAll || this.sides.contains(Side.UP)) {
            this.builder.pos(matrix4f, (float) this.pos.getX() - d, (float) this.pos.getY() + d, (float) this.pos.getZ() + d).color(this.r, this.g, this.b, this.a).tex(sprite.getMinU(), sprite.getMinV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() + d, (float) this.pos.getY() + d, (float) this.pos.getZ() + d).color(this.r, this.g, this.b, this.a).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() + d, (float) this.pos.getY() + d, (float) this.pos.getZ() - d).color(this.r, this.g, this.b, this.a).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() - d, (float) this.pos.getY() + d, (float) this.pos.getZ() - d).color(this.r, this.g, this.b, this.a).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(this.light).endVertex();
        }

        if (isAll || this.sides.contains(Side.DOWN)) {
            this.builder.pos(matrix4f, (float) this.pos.getX() - d, (float) this.pos.getY() - d, (float) this.pos.getZ() - d).color(this.r, this.g, this.b, this.a).tex(sprite.getMinU(), sprite.getMinV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() + d, (float) this.pos.getY() - d, (float) this.pos.getZ() - d).color(this.r, this.g, this.b, this.a).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() + d, (float) this.pos.getY() - d, (float) this.pos.getZ() + d).color(this.r, this.g, this.b, this.a).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() - d, (float) this.pos.getY() - d, (float) this.pos.getZ() + d).color(this.r, this.g, this.b, this.a).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(this.light).endVertex();
        }

        if (isAll || this.sides.contains(Side.NORTH)) {
            this.builder.pos(matrix4f, (float) this.pos.getX() + d, (float) this.pos.getY() - d, (float) this.pos.getZ() + d).color(this.r, this.g, this.b, this.a).tex(sprite.getMinU(), sprite.getMinV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() + d, (float) this.pos.getY() + d, (float) this.pos.getZ() + d).color(this.r, this.g, this.b, this.a).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() - d, (float) this.pos.getY() + d, (float) this.pos.getZ() + d).color(this.r, this.g, this.b, this.a).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() - d, (float) this.pos.getY() - d, (float) this.pos.getZ() + d).color(this.r, this.g, this.b, this.a).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(this.light).endVertex();
        }

        if (isAll || this.sides.contains(Side.SOUTH)) {
            this.builder.pos(matrix4f, (float) this.pos.getX() - d, (float) this.pos.getY() - d, (float) this.pos.getZ() - d).color(this.r, this.g, this.b, this.a).tex(sprite.getMinU(), sprite.getMinV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() - d, (float) this.pos.getY() + d, (float) this.pos.getZ() - d).color(this.r, this.g, this.b, this.a).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() + d, (float) this.pos.getY() + d, (float) this.pos.getZ() - d).color(this.r, this.g, this.b, this.a).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() + d, (float) this.pos.getY() - d, (float) this.pos.getZ() - d).color(this.r, this.g, this.b, this.a).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(this.light).endVertex();
        }

        if (isAll || this.sides.contains(Side.WEST)) {
            this.builder.pos(matrix4f, (float) this.pos.getX() - d, (float) this.pos.getY() - d, (float) this.pos.getZ() + d).color(this.r, this.g, this.b, this.a).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() - d, (float) this.pos.getY() + d, (float) this.pos.getZ() + d).color(this.r, this.g, this.b, this.a).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() - d, (float) this.pos.getY() + d, (float) this.pos.getZ() - d).color(this.r, this.g, this.b, this.a).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() - d, (float) this.pos.getY() - d, (float) this.pos.getZ() - d).color(this.r, this.g, this.b, this.a).tex(sprite.getMinU(), sprite.getMinV()).lightmap(this.light).endVertex();
        }

        if (isAll || this.sides.contains(Side.EAST)) {
            this.builder.pos(matrix4f, (float) this.pos.getX() + d, (float) this.pos.getY() - d, (float) this.pos.getZ() - d).color(this.r, this.g, this.b, this.a).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() + d, (float) this.pos.getY() + d, (float) this.pos.getZ() - d).color(this.r, this.g, this.b, this.a).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() + d, (float) this.pos.getY() + d, (float) this.pos.getZ() + d).color(this.r, this.g, this.b, this.a).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(this.light).endVertex();
            this.builder.pos(matrix4f, (float) this.pos.getX() + d, (float) this.pos.getY() - d, (float) this.pos.getZ() + d).color(this.r, this.g, this.b, this.a).tex(sprite.getMinU(), sprite.getMinV()).lightmap(this.light).endVertex();
        }
    }

    public enum Side {
        ALL, DOWN, UP, NORTH, SOUTH, WEST, EAST;

        public static Side from(Direction side) {
            return values()[side.ordinal() + 1];
        }
    }
}
