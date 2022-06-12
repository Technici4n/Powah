package owmii.powah.block.thermo;

import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import owmii.powah.lib.block.AbstractEnergyProvider;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.ITankHolder;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;

public class ThermoTile extends AbstractEnergyProvider<ThermoBlock> implements IInventoryHolder, ITankHolder {
    public long generating;

    public ThermoTile(BlockPos pos, BlockState state, Tier variant) {
        super(Tiles.THERMO_GEN.get(), pos, state, variant);
        this.tank.setCapacity(FluidStack.bucketAmount() * 4)
                .validate(stack -> PowahAPI.COOLANTS.containsKey(stack.getFluid()))
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
        int i = 0;
        if (!isRemote() && checkRedstone() && !this.tank.isEmpty()) {
            FluidStack fluid = this.tank.getFluid();
            if (PowahAPI.COOLANTS.containsKey(fluid.getFluid())) {
                int fluidCooling = PowahAPI.getCoolant(fluid.getFluid());
                BlockPos heatPos = this.worldPosition.below();
                BlockState state = world.getBlockState(heatPos);
                Block block = state.getBlock();
                if (!this.energy.isFull() && PowahAPI.HEAT_SOURCES.containsKey(block)) {
                    int heat = PowahAPI.getHeatSource(block);
                    if (block instanceof LiquidBlock) {
                        LiquidBlock fluidBlock = (LiquidBlock) block;
                        if (!fluidBlock.getFluidState(state).isSource()) {
                            int level = state.getValue(LiquidBlock.LEVEL);
                            heat = (int) (heat / ((float) level + 1));
                        }
                    }
                    this.generating = (int) (((heat * (fluidCooling == 1 ? 1 : Math.max(1.1D, (0.1D + Math.abs(fluidCooling)) * 1.1152D))) * getGeneration()) / 1000.0D);
                    this.energy.produce(this.generating);
                    if (world.getGameTime() % 40 == 0L) {
                        this.tank.drain(1, false);
                    }
                }
            }
        }

        return flag || this.generating > 0 ? 5 : -1;
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
}
