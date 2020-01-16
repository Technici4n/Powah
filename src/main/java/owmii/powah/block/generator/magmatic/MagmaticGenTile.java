package owmii.powah.block.generator.magmatic;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.ITiles;
import owmii.powah.block.generator.GeneratorTile;

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
                MagmaticGenTile.this.sync(5);
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
        this.tank.setCapacity(compound.getInt("TankCap"));
        super.readStorable(compound);
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT compound) {
        this.tank.writeToNBT(compound);
        compound.putInt("TankCap", this.tank.getCapacity());
        return super.writeStorable(compound);
    }

    @Override
    protected void firstTick() {
        super.firstTick();
        if (this.world == null) return;
        if (!this.world.isRemote) {
            if (getBlock() instanceof MagmaticGenBlock) {
                MagmaticGenBlock magmaticGen = (MagmaticGenBlock) getBlock();
                this.tank.setCapacity(FluidAttributes.BUCKET_VOLUME * magmaticGen.getBuckets());
                sync(getSyncTicks());
            }
        }
    }

    @Override
    public void generate() {
        if (this.nextGen <= 0 && !this.tank.isEmpty()) {
            FluidStack fluid = this.tank.getFluid();
            if (PowahAPI.MAGMATIC_FLUIDS.containsKey(fluid.getFluid())) {
                int fluidHeat = PowahAPI.getMagmaticFluidHeat(fluid.getFluid());
                if (fluidHeat > 0 && this.perTick > 0) {
                    int minStored = Math.min(this.tank.getFluidAmount(), 100);
                    this.nextGenCap = (minStored * fluidHeat / 100);
                    this.nextGen = this.nextGenCap;
                    this.tank.drain(minStored, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    public FluidTank getTank() {
        return this.tank;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return this.holder.cast();
        }
        return super.getCapability(cap, side);
    }
}