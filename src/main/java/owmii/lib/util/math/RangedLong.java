package owmii.lib.util.math;

import net.minecraft.nbt.CompoundNBT;

public class RangedLong {
    private long value;
    private long min;
    private long max;

    public RangedLong(long size) {
        this(0, size - 1);
    }

    public RangedLong(long min, long max) {
        this(0, min, max);
    }

    public RangedLong(long value, long min, long max) {
        this.value = value;
        this.min = min;
        this.max = max;
        if (min >= max) {
            throw new IllegalArgumentException("The min value: " + min + " should be smaller than max value: " + max);
        }
    }

    public RangedLong read(CompoundNBT nbt, String key) {
        this.value = nbt.getLong(key);
        return this;
    }

    public CompoundNBT writ(CompoundNBT nbt, String key, long value) {
        nbt.putLong(key, value);
        return nbt;
    }

    public long get() {
        return this.value;
    }

    public void set(long value) {
        this.value = Math.min(this.max, Math.max(this.min, value));
    }

    public long getMin() {
        return this.min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getMax() {
        return this.max;
    }

    public void setMax(long max) {
        this.max = max;
    }
}
