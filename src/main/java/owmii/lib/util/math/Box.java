package owmii.lib.util.math;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class Box {
    private double down;
    private double up;
    private double north;
    private double south;
    private double west;
    private double east;
    private double downBase;
    private double upBase;
    private double northBase;
    private double southBase;
    private double westBase;
    private double eastBase;
    private final double min;
    private final double max;

    public Box(double dim, double min, double max) {
        this(dim, dim, dim, dim, dim, dim, min, max);
    }

    public Box(double down, double up, double north, double south, double west, double east, double min, double max) {
        this.down = down;
        this.up = up;
        this.north = north;
        this.south = south;
        this.west = west;
        this.east = east;
        this.downBase = down;
        this.upBase = up;
        this.northBase = north;
        this.southBase = south;
        this.westBase = west;
        this.eastBase = east;
        this.min = min;
        this.max = max;
    }

    public void read(CompoundNBT nbt, String key) {
        this.down = nbt.getDouble("down_" + key);
        this.up = nbt.getDouble("up_" + key);
        this.north = nbt.getDouble("north_" + key);
        this.south = nbt.getDouble("south_" + key);
        this.west = nbt.getDouble("west_" + key);
        this.east = nbt.getDouble("east_" + key);
    }

    public CompoundNBT write(CompoundNBT nbt, String key) {
        nbt.putDouble("down_" + key, this.down);
        nbt.putDouble("up_" + key, this.up);
        nbt.putDouble("north_" + key, this.north);
        nbt.putDouble("south_" + key, this.south);
        nbt.putDouble("west_" + key, this.west);
        nbt.putDouble("east_" + key, this.east);
        return nbt;
    }

    public AxisAlignedBB geAxis() {
        return geAxis(Vector3d.ZERO);
    }

    public AxisAlignedBB geAxis(BlockPos pos) {
        return geAxis(Vector3d.copy(pos));
    }

    public AxisAlignedBB geAxis(Vector3d vector3d) {
        return new AxisAlignedBB(-this.west, -this.down, -this.north, this.east + 1.0D, this.up + 1.0D, this.south + 1.0D).offset(vector3d);
    }

    public Box reset() {
        this.down = this.downBase;
        this.up = this.upBase;
        this.north = this.northBase;
        this.south = this.southBase;
        this.west = this.westBase;
        this.east = this.eastBase;
        return this;
    }


    public double get(Direction direction) {
        switch (direction) {
            case DOWN:
                return getDown();
            case UP:
                return getUp();
            case NORTH:
                return getNorth();
            case SOUTH:
                return getSouth();
            case WEST:
                return getWest();
            default:
                return getEast();
        }
    }

    public Box add(Direction direction, double value) {
        switch (direction) {
            case DOWN:
                down(value);
                break;
            case UP:
                up(value);
                break;
            case NORTH:
                north(value);
                break;
            case SOUTH:
                south(value);
                break;
            case WEST:
                west(value);
                break;
            case EAST:
                east(value);
                break;
        }
        return this;
    }

    public Box set(Direction direction, double value) {
        switch (direction) {
            case DOWN:
                setDown(value);
                break;
            case UP:
                setUp(value);
                break;
            case NORTH:
                setNorth(value);
                break;
            case SOUTH:
                setSouth(value);
                break;
            case WEST:
                setWest(value);
                break;
            case EAST:
                setEast(value);
                break;
        }
        return this;
    }

    public Box add(double value) {
        down(value);
        up(value);
        north(value);
        south(value);
        west(value);
        east(value);
        return this;
    }

    public Box set(double value) {
        setDown(value);
        setUp(value);
        setNorth(value);
        setSouth(value);
        setWest(value);
        setEast(value);
        return this;
    }

    public double getDown() {
        return this.down;
    }

    public Box down(double down) {
        setDown(this.down + down);
        return this;
    }

    public Box setDown(double down) {
        this.down = Math.min(this.max, Math.max(this.min, down));
        return this;
    }

    public double getUp() {
        return this.up;
    }

    public Box up(double up) {
        setUp(this.up + up);
        return this;
    }

    public Box setUp(double up) {
        this.up = Math.min(this.max, Math.max(this.min, up));
        return this;
    }

    public double getNorth() {
        return this.north;
    }

    public Box north(double north) {
        setNorth(this.north + north);
        return this;
    }

    public Box setNorth(double north) {
        this.north = Math.min(this.max, Math.max(this.min, north));
        return this;
    }

    public double getSouth() {
        return this.south;
    }

    public Box south(double south) {
        setSouth(this.south + south);
        return this;
    }

    public Box setSouth(double south) {
        this.south = Math.min(this.max, Math.max(this.min, south));
        return this;
    }

    public double getWest() {
        return this.west;
    }

    public Box west(double west) {
        setWest(this.west + west);
        return this;
    }

    public Box setWest(double west) {
        this.west = Math.min(this.max, Math.max(this.min, west));
        return this;
    }

    public double getEast() {
        return this.east;
    }

    public Box east(double east) {
        setEast(this.east + east);
        return this;
    }

    public Box setEast(double east) {
        this.east = Math.min(this.max, Math.max(this.min, east));
        return this;
    }
}
