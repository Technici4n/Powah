package owmii.powah.block.magmator;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import owmii.lib.block.AbstractEnergyProvider;
import owmii.lib.block.IInventoryHolder;
import owmii.lib.block.ITankHolder;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.logistics.fluid.Tank;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.config.generator.MagmatorConfig;

public class MagmatorTile extends AbstractEnergyProvider<Tier, MagmatorConfig, MagmatorBlock> implements IInventoryHolder, ITankHolder {
    protected final Energy buffer = Energy.create(0);
    protected boolean burning;

    public MagmatorTile(Tier variant) {
        super(Tiles.MAGMATOR, variant);
        this.tank.setCapacity(FluidAttributes.BUCKET_VOLUME * 4)
                .validate(stack -> PowahAPI.MAGMATIC_FLUIDS.containsKey(stack.getFluid()))
                .setChange(() -> MagmatorTile.this.sync(10));
        this.inv.add(1);
    }

    public MagmatorTile() {
        this(Tier.STARTER);
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        super.readSync(nbt);
        this.energy.read(nbt, "energy_buffer", true, false);
        this.burning = nbt.getBoolean("burning");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        this.energy.write(nbt, "energy_buffer", true, false);
        nbt.putBoolean("burning", this.burning);
        return super.writeSync(nbt);
    }

    @Override
    protected int postTick(World world) {
        if (!isRemote() && checkRedstone()) {
            boolean flag = false;
            if (this.buffer.isEmpty() && !this.tank.isEmpty()) {
                FluidStack fluid = this.tank.getFluid();
                if (PowahAPI.MAGMATIC_FLUIDS.containsKey(fluid.getFluid())) {
                    int fluidHeat = PowahAPI.getMagmaticFluidHeat(fluid.getFluid());
                    if (fluidHeat > 0) {
                        int minStored = Math.min(this.tank.getFluidAmount(), 100);
                        this.buffer.setStored(minStored * fluidHeat / 100);
                        this.buffer.setCapacity(minStored * fluidHeat / 100);
                        this.tank.drain(minStored, IFluidHandler.FluidAction.EXECUTE);
                    }
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
            }//TODO
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
