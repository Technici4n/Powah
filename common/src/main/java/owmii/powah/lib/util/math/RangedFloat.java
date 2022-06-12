package owmii.powah.lib.util.math;

import net.minecraft.nbt.CompoundTag;

public class RangedFloat {
    private float value;
    private float min;
    private float max;

    public RangedFloat(float size) {
        this(0.0F, size - 1.0F);
    }

    public RangedFloat(float min, float max) {
        this(0.0F, min, max);
    }

    public RangedFloat(float value, float min, float max) {
        this.value = value;
        this.min = min;
        this.max = max;
        if (min >= max) {
            throw new IllegalArgumentException("The min value: " + min + " should be smaller than max value: " + max);
        }
    }

    public RangedFloat read(CompoundTag nbt, String key) {
        this.value = nbt.getFloat(key);
        return this;
    }

    public CompoundTag writ(CompoundTag nbt, String key, float value) {
        nbt.putFloat(key, value);
        return nbt;
    }

    public float get() {
        return this.value;
    }

    public void set(float value) {
        this.value = Math.min(this.max, Math.max(this.min, value));
    }

    public float getMin() {
        return this.min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return this.max;
    }

    public void setMax(float max) {
        this.max = max;
    }
}
