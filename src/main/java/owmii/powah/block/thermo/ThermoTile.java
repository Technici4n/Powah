package owmii.powah.block.thermo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import owmii.lib.block.AbstractEnergyProvider;
import owmii.lib.block.IInventoryHolder;
import owmii.lib.block.ITankHolder;
import owmii.lib.logistics.energy.Energy;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.config.generator.ThermoConfig;

public class ThermoTile extends AbstractEnergyProvider<Tier, ThermoConfig, ThermoBlock> implements IInventoryHolder, ITankHolder {
    public long generating;

    public ThermoTile(Tier variant) {
        super(Tiles.THERMO_GEN, variant);
        this.tank.setCapacity(FluidAttributes.BUCKET_VOLUME * 4)
                .validate(stack -> PowahAPI.COOLANTS.containsKey(stack.getFluid()))
                .setChange(() -> ThermoTile.this.sync(10));
        this.inv.add(1);
    }

    public ThermoTile() {
        this(Tier.STARTER);
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        super.readSync(nbt);
        this.generating = nbt.getLong("generating");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        nbt.putLong("generating", this.generating);
        return super.writeSync(nbt);
    }

    @Override
    protected int postTick(World world) {
        boolean flag = chargeItems(1) + extractFromSides(world) > 0;
        int i = 0;
        if (!isRemote() && checkRedstone() && !this.tank.isEmpty()) {
            FluidStack fluid = this.tank.getFluid();
            if (PowahAPI.COOLANTS.containsKey(fluid.getFluid())) {
                int fluidCooling = PowahAPI.getCoolant(fluid.getFluid());
                BlockPos heatPos = this.pos.down();
                BlockState state = world.getBlockState(heatPos);
                Block block = state.getBlock();
                if (!this.energy.isFull() && PowahAPI.HEAT_SOURCES.containsKey(block)) {
                    int heat = PowahAPI.getHeatSource(block);
                    if (block instanceof FlowingFluidBlock) {
                        FlowingFluidBlock fluidBlock = (FlowingFluidBlock) block;
                        if (!fluidBlock.getFluidState(state).isSource()) {
                            int level = state.get(FlowingFluidBlock.LEVEL);
                            heat = (int) (heat / ((float) level + 1));
                        }
                    }
                    this.generating = (int) (((heat * (fluidCooling == 1 ? 1 : Math.max(1.1D, (0.1D + Math.abs(fluidCooling)) * 1.1152D))) * getGeneration()) / 1000.0D);
                    this.energy.produce(this.generating);
                    if (world.getGameTime() % 40 == 0L) {
                        this.tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
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
    protected boolean keepFluid() {
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
