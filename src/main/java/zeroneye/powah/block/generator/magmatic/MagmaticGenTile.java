package zeroneye.powah.block.generator.magmatic;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import zeroneye.powah.api.PowahAPI;
import zeroneye.powah.block.ITiles;
import zeroneye.powah.block.generator.GeneratorTile;

import javax.annotation.Nullable;

public class MagmaticGenTile extends GeneratorTile {
    protected final FluidTank tank;
    private final LazyOptional<IFluidHandler> holder;

    public MagmaticGenTile(int capacity, int transfer, int perTick, int bucketCount) {
        super(ITiles.MAGMATIC_GENERATOR, capacity, transfer, perTick);
        this.tank = new FluidTank(FluidAttributes.BUCKET_VOLUME * bucketCount) {
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return PowahAPI.MAGMATIC_FLUIDS.containsKey(stack.getFluid()) && super.isFluidValid(stack);
            }

            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                MagmaticGenTile.this.setReadyToSync(true);
            }
        };

        this.holder = LazyOptional.of(() -> this.tank);
    }

    public MagmaticGenTile() {
        this(0, 0, 0, 0);
    }

    @Override
    public void readStorable(CompoundNBT compound) {
        this.tank.readFromNBT(compound);
        this.tank.setCapacity(compound.getInt("TanckCap"));
        super.readStorable(compound);
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT compound) {
        this.tank.writeToNBT(compound);
        compound.putInt("TanckCap", this.tank.getCapacity());
        return super.writeStorable(compound);
    }

    @Override
    public int toGenerate() {
        int i = 0;
        if (this.nextGen >= perTick()) {
            i = perTick();
            this.nextGen -= perTick();
        } else {
            if (this.nextGen > 0) {
                i = this.nextGen;
                this.nextGen = 0;
            }
        }
        if (this.nextGen <= 0 && !this.tank.isEmpty()) {
            FluidStack fluid = this.tank.getFluid();
            if (PowahAPI.MAGMATIC_FLUIDS.containsKey(fluid.getFluid())) {
                int fluidHeat = PowahAPI.getFluidHeat(fluid.getFluid());
                if (fluidHeat > 0 && this.perTick > 0) {
                    int fluidStored = this.tank.getFluidAmount();
                    int minStored = Math.min(fluidStored, 100);
                    int j = (minStored * fluidHeat / 100);
                    this.nextGen = j;
                    this.tank.drain(minStored, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
        return i;
    }

    public FluidTank getTank() {
        return tank;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return this.holder.cast();
        }
        return super.getCapability(cap, side);
    }
}