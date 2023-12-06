package owmii.powah.block.magmator;

import net.minecraft.core.BlockPos;
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
import owmii.powah.lib.logistics.fluid.Tank;
import owmii.powah.util.Util;

public class MagmatorTile extends AbstractEnergyProvider<MagmatorBlock> implements IInventoryHolder, ITankHolder {
    protected final Energy buffer = Energy.create(0);
    protected boolean burning;

    public MagmatorTile(BlockPos pos, BlockState state, Tier variant) {
        super(Tiles.MAGMATOR.get(), pos, state, variant);
        this.tank.setCapacity(Util.bucketAmount() * 4)
                .setValidator(stack -> PowahAPI.getMagmaticFluidHeat(stack.getFluid()) != 0)
                .setChange(() -> MagmatorTile.this.sync(10));
        this.inv.add(1);
    }

    public MagmatorTile(BlockPos pos, BlockState state) {
        this(pos, state, Tier.STARTER);
    }

    @Override
    public void readSync(CompoundTag nbt) {
        super.readSync(nbt);
        this.energy.read(nbt, "energy_buffer", true, false);
        this.burning = nbt.getBoolean("burning");
    }

    @Override
    public CompoundTag writeSync(CompoundTag nbt) {
        this.energy.write(nbt, "energy_buffer", true, false);
        nbt.putBoolean("burning", this.burning);
        return super.writeSync(nbt);
    }

    @Override
    protected int postTick(Level world) {
        if (!isRemote() && checkRedstone()) {
            boolean flag = false;
            if (this.buffer.isEmpty() && !this.tank.isEmpty()) {
                FluidStack fluid = this.tank.getFluid();
                int fluidHeat = PowahAPI.getMagmaticFluidHeat(fluid.getFluid());
                if (fluidHeat > 0) {
                    var amountPerDrain = 100 * Util.millibucketAmount();
                    var minStored = Math.min(this.tank.getFluidAmount(), amountPerDrain);
                    this.buffer.setStored((long) minStored * fluidHeat / amountPerDrain);
                    this.buffer.setCapacity((long) minStored * fluidHeat / amountPerDrain);
                    this.tank.drain(minStored, IFluidHandler.FluidAction.EXECUTE);
                }
            }

            long min = Math.min(getGeneration(), this.buffer.getStored());
            if (min > 0 && this.energy.getEmpty() >= min) {
                this.energy.produce(min);
                this.buffer.consume(min);
                flag = true;
                sync(4);
            }

            if (this.burning != flag) {
                this.burning = flag;
                sync(4);
            } // TODO
        }
        return chargeItems(1) + extractFromSides(world) > 0 ? 10 : -1;
    }

    public Tank getTank() {
        return this.tank;
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

    public boolean isBurning() {
        return this.burning;
    }
}
