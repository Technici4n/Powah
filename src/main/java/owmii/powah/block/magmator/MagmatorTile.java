package owmii.powah.block.magmator;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import owmii.lib.block.TileBase;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;

import javax.annotation.Nullable;

public class MagmatorTile extends TileBase.EnergyProvider<Tier, MagmatorBlock> {
    protected final FluidTank tank;
    private final LazyOptional<IFluidHandler> holder;

    public MagmatorTile(Tier variant) {
        super(ITiles.MAGMATOR, variant);
        this.tank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 4) {
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return PowahAPI.MAGMATIC_FLUIDS.containsKey(stack.getFluid()) && super.isFluidValid(stack);
            }

            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                MagmatorTile.this.sync(10);
            }
        };
        this.holder = LazyOptional.of(() -> this.tank);
    }

    public MagmatorTile() {
        this(Tier.STARTER);
    }

    @Override
    public void readStorable(CompoundNBT compound) {
        this.tank.readFromNBT(compound);
        super.readStorable(compound);
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT compound) {
        this.tank.writeToNBT(compound);
        return super.writeStorable(compound);
    }

    @Override
    public void generate(World world) {
        if (this.nextBuff <= 0 && !this.tank.isEmpty()) {
            FluidStack fluid = this.tank.getFluid();
            if (PowahAPI.MAGMATIC_FLUIDS.containsKey(fluid.getFluid())) {
                int fluidHeat = PowahAPI.getMagmaticFluidHeat(fluid.getFluid());
                if (fluidHeat > 0) {
                    int minStored = Math.min(this.tank.getFluidAmount(), 100);
                    this.buffer = (minStored * fluidHeat / 100);
                    this.nextBuff = this.buffer;
                    this.tank.drain(minStored, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    public FluidTank getTank() {
        return this.tank;
    }

    @Override
    public boolean hasEnergyBuffer() {
        return true;
    }

    @Override
    public int getChargingSlots() {
        return 1;
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return this.holder.cast();
        }
        return super.getCapability(cap, side);
    }
}
