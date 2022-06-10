package owmii.powah.lib.util.math;

import com.mojang.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class V3d extends Vec3 {

    public static final V3d ZERO = new V3d(0.0D, 0.0D, 0.0D);

    public V3d(double x, double y, double z) {
        super(x, y, z);
    }

    public V3d(Vec3 vector) {
        this(vector.x, vector.y, vector.z);
    }

    public V3d(Vector3f vector) {
        super(vector);
    }

    public static V3d from(Vec3 vector) {
        return new V3d(vector);
    }

    public static V3d from(Vector3f vector) {
        return new V3d(vector);
    }

    public static V3d from(BlockPos pos) {
        return new V3d(pos.getX(), pos.getY(), pos.getZ());
    }

    public float distance(Vector3f vec3i) {
        return distance(new Vec3(vec3i));
    }

    public float distance(Vec3 vec3d) {
        float f = (float) (this.x - vec3d.x);
        float f1 = (float) (this.y - vec3d.y);
        float f2 = (float) (this.z - vec3d.z);
        return Mth.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public float distance(BlockPos pos) {
        float f = (float) (this.x - pos.getX());
        float f1 = (float) (this.y - pos.getY());
        float f2 = (float) (this.z - pos.getZ());
        return Mth.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public V3d up() {
        return up(1.0D);
    }

    public V3d up(double factor) {
        return add(0.0D, factor, 0.0D);
    }

    public V3d down() {
        return down(1.0D);
    }

    public V3d down(double factor) {
        return add(0.0D, -factor, 0.0D);
    }

    public V3d north() {
        return north(1.0D);
    }

    public V3d north(double factor) {
        return add(0.0D, 0.0D, -factor);
    }

    public V3d south() {
        return south(1.0D);
    }

    public V3d south(double factor) {
        return add(0.0D, 0.0D, factor);
    }

    public V3d east() {
        return east(1.0D);
    }

    public V3d east(double factor) {
        return add(factor, 0.0D, 0.0D);
    }

    public V3d west() {
        return west(1.0D);
    }

    public V3d west(double factor) {
        return add(-factor, 0.0D, 0.0D);
    }

    public V3d center() {
        return new V3d(((int) this.x) + 0.5D, ((int) this.y) + 0.5D, ((int) this.z) + 0.5D);
    }

    public V3d center(AABB bb) {
        return new V3d(
                bb.minX + (bb.maxX - bb.minX) * 0.5D,
                bb.minY + (bb.maxY - bb.minY) * 0.5D,
                bb.minZ + (bb.maxZ - bb.minZ) * 0.5D
        );
    }

    public V3d centerU() {
        return center().up(0.5D);
    }

    public V3d centerD() {
        return center().down(0.5D);
    }

    public V3d centerN() {
        return center().north(0.5D);
    }

    public V3d centerS() {
        return center().south(0.5D);
    }

    public V3d centerE() {
        return center().east(0.5D);
    }

    public V3d centerW() {
        return center().west(0.5D);
    }

    public V3d random(double dist) {
        double d0 = Math.random() < 0.5D ? -dist : dist;
        double d1 = Math.random() < 0.5D ? -dist : dist;
        double d2 = Math.random() < 0.5D ? -dist : dist;
        return add(Math.random() * d0, Math.random() * d1, Math.random() * d2);
    }

    @Override
    public V3d add(Vec3 vec) {
        return add(vec.x, vec.y, vec.z);
    }

    @Override
    public V3d add(double x, double y, double z) {
        return new V3d(this.x + x, this.y + y, this.z + z);
    }

    public double hMagSqrt() {
        return Math.sqrt(hMag());
    }

    public double hMag() {
        return this.x * this.x + this.z * this.z;
    }

    public double magSqrt() {
        return Math.sqrt(mag());
    }

    public double mag() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public List<V3d> circled(int count, double radius) {
        return circled(count, radius, 0, 0.0D);
    }

    public List<V3d> circled(int count, double radius, int ticks, double speed) {
        List<V3d> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            double slice = 2.0D * Math.PI / count;
            double angle = slice * i;
            double x = radius * Math.cos(angle + ticks * speed);
            double z = radius * Math.sin(angle + ticks * speed);
            list.add(add(x, 0.0D, z));
        }
        return list;
    }

    public V3d toOrigin() {
        return new V3d((int) this.x, (int) this.y, (int) this.z);
    }

    public V3d zero() {
        return ZERO;
    }

    public BlockPos toPos() {
        return new BlockPos(this);
    }
}
