package owmii.lib.logistics.fluid;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class Tank extends FluidTank {
    private Runnable changed = () -> {
    };

    public Tank(int capacity) {
        this(capacity, e -> true);
    }

    public Tank(int capacity, Predicate<FluidStack> validator) {
        super(capacity, validator);
    }

    public FluidTank readFromNBT(CompoundNBT nbt, String key) {
        return super.readFromNBT(nbt.getCompound(key));
    }

    public CompoundNBT writeToNBT(CompoundNBT nbt, String key) {
        CompoundNBT compound = super.writeToNBT(new CompoundNBT());
        compound.put(key, nbt);
        return compound;
    }

    public Tank validate(Predicate<FluidStack> validator) {
        this.validator = validator;
        return this;
    }

    @Override
    public Tank setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public Tank setChange(Runnable changed) {
        this.changed = changed;
        return this;
    }

    @Override
    protected void onContentsChanged() {
        this.changed.run();
    }
}
