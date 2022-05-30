package owmii.lib.util.math;

import net.minecraft.nbt.CompoundNBT;

public class RangedDouble {
    private double value;
    private double min;
    private double max;

    public RangedDouble(double size) {
        this(0.0D, size - 1.0D);
    }

    public RangedDouble(double min, double max) {
        this(0.0D, min, max);
    }

    public RangedDouble(double value, double min, double max) {
        this.value = value;
        this.min = min;
        this.max = max;
        if (min >= max) {
            throw new IllegalArgumentException("The min value: " + min + " should be smaller than max value: " + max);
        }
    }

    public RangedDouble read(CompoundNBT nbt, String key) {
        this.value = nbt.getDouble(key);
        return this;
    }

    public CompoundNBT writ(CompoundNBT nbt, String key, double value) {
        nbt.putDouble(key, value);
        return nbt;
    }

    public double get() {
        return this.value;
    }

    public void set(double value) {
        this.value = Math.min(this.max, Math.max(this.min, value));
    }

    public double getMin() {
        return this.min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return this.max;
    }

    public void setMax(double max) {
        this.max = max;
    }
}
