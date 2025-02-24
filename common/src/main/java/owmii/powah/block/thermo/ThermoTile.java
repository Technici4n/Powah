package owmii.powah.block.thermo;

import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.lib.block.AbstractEnergyProvider;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.ITankHolder;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.util.Util;

public class ThermoTile extends AbstractEnergyProvider<ThermoBlock> implements IInventoryHolder, ITankHolder {
    public long generating;
    public boolean active = false;

    public ThermoTile(BlockPos pos, BlockState state, Tier variant) {
        super(Tiles.THERMO_GEN.get(), pos, state, variant);
        this.tank.setCapacity(FluidStack.bucketAmount() * 4)
                .validate(stack -> PowahAPI.getCoolant(stack.getFluid()) != 0)
                .setChange(() -> ThermoTile.this.sync(10));
        this.inv.add(1);
    }

    public ThermoTile(BlockPos pos, BlockState state) {
        this(pos, state, Tier.STARTER);
    }

    @Override
    public void readSync(CompoundTag nbt) {
        super.readSync(nbt);
        this.generating = nbt.getLong("generating");
    }

    @Override
    public CompoundTag writeSync(CompoundTag nbt) {
        nbt.putLong("generating", this.generating);
        return super.writeSync(nbt);
    }

    @Override
    protected int postTick(Level world) {
        boolean flag = chargeItems(1) + extractFromSides(world) > 0;

        if (!isRemote() && checkRedstone() && !this.tank.isEmpty()) {
            FluidStack fluid = this.tank.getFluid();
            BlockPos heatPos = this.worldPosition.below();
            BlockState state = world.getBlockState(heatPos);

            if (canGenerate(state)) {
                this.active = true;
                int fluidCooling = PowahAPI.getCoolant(fluid.getFluid());
                int heat = PowahAPI.getHeatSource(state);

                if (!this.energy.isFull() && heat != 0) {
                    if (state.hasProperty(LiquidBlock.LEVEL)) {
                        heat = (int) (heat / ((float) state.getValue(LiquidBlock.LEVEL) + 1));
                    }

                    this.generating = (int) ((heat * Math.max(1D, (1D + fluidCooling) / 2D) * getGeneration()) / 1000.0D);
                    this.energy.produce(this.generating);

                    if (world.getGameTime() % 40 == 0L) {
                        this.tank.drain(Util.millibucketAmount(), false);
                    }
                }
            } else if (this.active) {
                resetGeneration();
            }
        }

        return flag || this.generating > 0 ? 5 : -1;
    }

    public boolean canGenerate(BlockState state) {
        boolean coolant = !this.tank.isEmpty() && PowahAPI.getCoolant(this.tank.getFluid().getFluid()) != 0;
        boolean heat = PowahAPI.getHeatSource(state) != 0;
        return !isRemote() && checkRedstone() && coolant && heat;
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public boolean keepFluid() {
        return true;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return Energy.chargeable(stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }

    public void resetGeneration() {
        this.active = false;
        this.generating = 0;
        this.energy.produce(0);
    }
}
