package owmii.powah.block.thermo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.lib.block.AbstractEnergyProvider;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.ITankHolder;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.util.Util;

public class ThermoTile extends AbstractEnergyProvider<ThermoBlock> implements IInventoryHolder, ITankHolder {
    public long generating;

    public ThermoTile(BlockPos pos, BlockState state, Tier variant) {
        super(Tiles.THERMO_GEN.get(), pos, state, variant);
        this.tank.setCapacity(Util.bucketAmount() * 4)
                .setValidator(stack -> PowahAPI.getCoolant(stack.getFluid()).isPresent())
                .setChange(() -> ThermoTile.this.sync(10));
        this.inv.add(1);
    }

    public ThermoTile(BlockPos pos, BlockState state) {
        this(pos, state, Tier.STARTER);
    }

    @Override
    public void readSync(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readSync(nbt, registries);
        this.generating = nbt.getLong("generating");
    }

    @Override
    public CompoundTag writeSync(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putLong("generating", this.generating);
        return super.writeSync(nbt, registries);
    }

    @Override
    protected int postTick(Level world) {
        boolean flag = chargeItems(1) + extractFromSides(world) > 0;
        int i = 0;
        if (!isRemote() && checkRedstone() && !this.tank.isEmpty()) {
            FluidStack fluid = this.tank.getFluid();
            var fluidCooling = PowahAPI.getCoolant(fluid.getFluid());
            if (fluidCooling.isPresent()) {
                BlockPos heatPos = this.worldPosition.below();
                BlockState state = world.getBlockState(heatPos);
                int heat = PowahAPI.getHeatSource(state);
                if (!this.energy.isFull() && heat != 0) {
                    double heatRatio = heat / 1000.0;
                    // The formula I want is:
                    // (water) 0 °C -> ratio of 1
                    // (water) -10 °C -> ratio of 5
                    // (water) -20 °C -> ratio of 10
                    // and so on...
                    // So we do -temperature/2 with a max to handle the 0 °C case
                    double coolantRatio = Math.max(1D, -fluidCooling.getAsInt() / 2D);
                    this.generating = (int) (heatRatio * coolantRatio * getGeneration());
                    this.energy.produce(this.generating);
                    if (world.getGameTime() % 40 == 0L) {
                        this.tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
                    }
                } else {
                    this.generating = 0;
                }
            } else {
                this.generating = 0;
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
