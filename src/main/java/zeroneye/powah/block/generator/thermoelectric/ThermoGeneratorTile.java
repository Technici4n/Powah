package zeroneye.powah.block.generator.thermoelectric;

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
import zeroneye.powah.api.PowahAPI;
import zeroneye.powah.block.ITiles;
import zeroneye.powah.block.generator.GeneratorTile;

import javax.annotation.Nullable;

public class ThermoGeneratorTile extends GeneratorTile {
    protected final FluidTank tank;
    private final LazyOptional<IFluidHandler> holder;

    public ThermoGeneratorTile(int capacity, int transfer, int perTick, int buckets) {
        super(ITiles.THERMO_GENERATOR, capacity, transfer, perTick);
        this.tank = new FluidTank(FluidAttributes.BUCKET_VOLUME * buckets) {
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return PowahAPI.COOLANT_FLUIDS.containsKey(stack.getFluid()) && super.isFluidValid(stack);
            }

            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                ThermoGeneratorTile.this.setReadyToSync(true);
            }
        };
        this.holder = LazyOptional.of(() -> this.tank);
    }

    public ThermoGeneratorTile() {
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
    protected void onFirstTick() {
        super.onFirstTick();
        if (this.world == null) return;
        if (!this.world.isRemote) {
            if (getBlock() instanceof ThermoGeneratorBlock) {
                ThermoGeneratorBlock thermoGenerator = (ThermoGeneratorBlock) getBlock();
                this.tank.setCapacity(FluidAttributes.BUCKET_VOLUME * thermoGenerator.buckets);
                setReadyToSync(true);
            }
        }
    }

    @Override
    protected void generate() {
        if (this.world == null) return;
        if (this.nextGen <= 0 && !this.tank.isEmpty()) {
            FluidStack fluid = this.tank.getFluid();
            if (PowahAPI.COOLANT_FLUIDS.containsKey(fluid.getFluid())) {
                int fluidCooling = PowahAPI.getCoolantFluid(fluid.getFluid());
                if (this.perTick > 0) {
                    BlockPos heatPos = this.pos.down();
                    BlockState state = this.world.getBlockState(heatPos);
                    Block block = state.getBlock();
                    if (PowahAPI.HEAT_BLOCKS.containsKey(block)) {
                        int heat = PowahAPI.getBlockHeat(block);
                        if (block instanceof FlowingFluidBlock) {
                            FlowingFluidBlock fluidBlock = (FlowingFluidBlock) block;
                            if (!fluidBlock.getFluidState(state).isSource()) {
                                int level = state.get(FlowingFluidBlock.LEVEL);
                                heat = (int) (heat / ((float) level + 1));
                            }
                        }
                        int i = (int) (((heat * (fluidCooling == 1 ? 1 : Math.max(1.1, (0.1 + Math.abs(fluidCooling)) * 1.1152D))) * this.perTick) / 1000.0D);
                        this.nextGenCap = i;
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
