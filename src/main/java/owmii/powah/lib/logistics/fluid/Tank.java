package owmii.powah.lib.logistics.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import owmii.powah.EnvHandler;

public class Tank extends FluidTank {
    private Runnable changed = () -> {
    };
    private Object platformWrapper;

    public Tank(int capacity) {
        this(capacity, e -> true);
    }

    public Tank(int capacity, Predicate<FluidStack> validator) {
        super(capacity, validator);
    }

    public FluidTank readFromNBT(CompoundTag nbt, String key) {
        return super.readFromNBT(nbt.getCompound(key));
    }

    public CompoundTag writeToNBT(CompoundTag nbt, String key) {
        CompoundTag compound = super.writeToNBT(new CompoundTag());
        compound.put(key, nbt);
        return compound;
    }

    public Tank validate(Predicate<FluidStack> validator) {
        this.validator = validator;
        return this;
    }

    @Override
    public Tank setCapacity(long capacity) {
        this.capacity = capacity;
        return this;
    }

    public Tank setChange(Runnable changed) {
        this.changed = changed;
        return this;
    }

    private boolean sendUpdates = true;

    public void setSendUpdates(boolean sendUpdates) {
        this.sendUpdates = sendUpdates;
    }

    @Override
    public void onContentsChanged() {
        if (sendUpdates) {
            this.changed.run();
        }
    }

    public Object getPlatformWrapper() {
        if (platformWrapper == null) {
            platformWrapper = EnvHandler.INSTANCE.createTankWrapper(this);
        }
        return platformWrapper;
    }
}
