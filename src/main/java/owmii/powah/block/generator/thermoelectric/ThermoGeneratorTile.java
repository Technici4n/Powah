package owmii.powah.block.generator.thermoelectric;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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

public class ThermoGeneratorTile extends GeneratorTile {
    protected final FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 4) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return PowahAPI.THERMO_COOLANTS.containsKey(stack.getFluid()) && super.isFluidValid(stack);
        }

        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            ThermoGeneratorTile.this.sync(5);
        }
    };
    private final LazyOptional<IFluidHandler> holder;

    public ThermoGeneratorTile(int capacity, int transfer, int perTick) {
        super(ITiles.THERMO_GENERATOR, capacity, transfer, perTick);
        this.holder = LazyOptional.of(() -> this.tank);
    }

    public ThermoGeneratorTile() {
        this(0, 0, 0);
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
    protected void generate() {
        if (this.world == null) return;
        if (this.nextGen <= 0 && !this.tank.isEmpty()) {
            FluidStack fluid = this.tank.getFluid();
            if (PowahAPI.THERMO_COOLANTS.containsKey(fluid.getFluid())) {
                int fluidCooling = PowahAPI.getThermoCoolant(fluid.getFluid());
                if (this.perTick > 0) {
                    BlockPos heatPos = this.pos.down();
                    BlockState state = this.world.getBlockState(heatPos);
                    Block block = state.getBlock();
                    if (PowahAPI.THERMO_HEAT_SOURCES.containsKey(block)) {
                        int heat = PowahAPI.getThermoHeatSource(block);
                        if (block instanceof FlowingFluidBlock) {
                            FlowingFluidBlock fluidBlock = (FlowingFluidBlock) block;
                            if (!fluidBlock.getFluidState(state).isSource()) {
                                int level = state.get(FlowingFluidBlock.LEVEL);
                                heat = (int) (heat / ((float) level + 1));
                            }
                        }
                        this.nextGenCap = (int) (((heat * (fluidCooling == 1 ? 1 : Math.max(1.1, (0.1 + Math.abs(fluidCooling)) * 1.1152D))) * this.perTick) / 1000.0D);
                        this.nextGen = this.nextGenCap;
                        if (this.world.getGameTime() % 40 == 0L) {
                            this.tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int perTick() {
        return this.nextGenCap > super.perTick() ? this.nextGenCap : super.perTick();
    }

    @Override
    public int getChargingSlots() {
        return 1;
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