package owmii.lib.util.math;

import net.minecraft.nbt.CompoundNBT;

public class RangedInt {
    private int value;
    private int min;
    private int max;

    public RangedInt(int size) {
        this(0, size - 1);
    }

    public RangedInt(int min, int max) {
        this(0, min, max);
    }

    public RangedInt(int value, int min, int max) {
        this.value = value;
        this.min = min;
        this.max = max;
        if (min >= max) {
            throw new IllegalArgumentException("The min value: " + min + " should be smaller than max value: " + max);
        }
    }

    public RangedInt read(CompoundNBT nbt, String key) {
        this.value = nbt.getInt(key);
        return this;
    }

    public CompoundNBT writ(CompoundNBT nbt, String key) {
        nbt.putInt(key, this.value);
        return nbt;
    }

    public int get() {
        return this.value;
    }

    public void set(int value) {
        this.value = Math.min(this.max, Math.max(this.min, value));
    }

    public int getMin() {
        return this.min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
