package owmii.powah.block.thermo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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

public class ThermoGenTile extends TileBase.EnergyProvider<Tier, ThermoGenBlock> {
    protected final FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 4) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return PowahAPI.COOLANTS.containsKey(stack.getFluid()) && super.isFluidValid(stack);
        }

        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            ThermoGenTile.this.sync(5);
        }
    };

    private final LazyOptional<IFluidHandler> holder;

    public ThermoGenTile(Tier variant) {
        super(ITiles.THERMO_GEN, variant);
        this.holder = LazyOptional.of(() -> this.tank);
    }

    public ThermoGenTile() {
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
    protected void generate(World world) {
        if (this.nextBuff <= 0 && !this.tank.isEmpty()) {
            FluidStack fluid = this.tank.getFluid();
            if (PowahAPI.COOLANTS.containsKey(fluid.getFluid())) {
                int fluidCooling = PowahAPI.getCoolant(fluid.getFluid());
                BlockPos heatPos = this.pos.down();
                BlockState state = world.getBlockState(heatPos);
                Block block = state.getBlock();
                if (PowahAPI.HEAT_SOURCES.containsKey(block)) {
                    int heat = PowahAPI.getHeatSource(block);
                    if (block instanceof FlowingFluidBlock) {
                        FlowingFluidBlock fluidBlock = (FlowingFluidBlock) block;
                        if (!fluidBlock.getFluidState(state).isSource()) {
                            int level = state.get(FlowingFluidBlock.LEVEL);
                            heat = (int) (heat / ((float) level + 1));
                        }
                    }
                    this.buffer = (int) (((heat * (fluidCooling == 1 ? 1 : Math.max(1.1D, (0.1D + Math.abs(fluidCooling)) * 1.1152D))) * defaultGeneration()) / 1000.0D);
                    this.nextBuff = this.buffer;

                    if (world.getGameTime() % 40 == 0L) {
                        this.tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        }
    }

    @Override
    public boolean hasEnergyBuffer() {
        return true;
    }

    @Override
    public boolean keepEnergy() {
        return true;
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
