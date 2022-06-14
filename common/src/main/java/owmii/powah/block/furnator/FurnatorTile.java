package owmii.powah.block.furnator;

import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import owmii.powah.EnvHandler;
import owmii.powah.Powah;
import owmii.powah.lib.block.AbstractEnergyProvider;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.util.Ticker;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;

public class FurnatorTile extends AbstractEnergyProvider<FurnatorBlock> implements IInventoryHolder {
    protected final Ticker carbon = Ticker.empty();
    protected boolean burning;

    public FurnatorTile(BlockPos pos, BlockState state, Tier variant) {
        super(Tiles.FURNATOR.get(), pos, state, variant);
        this.inv.set(2);
    }

    public FurnatorTile(BlockPos pos, BlockState state) {
        this(pos, state, Tier.STARTER);
    }

    @Override
    public void readStorable(CompoundTag nbt) {
        super.readStorable(nbt);
        this.carbon.read(nbt, "carbon");
    }

    @Override
    public CompoundTag writeStorable(CompoundTag nbt) {
        this.carbon.write(nbt, "carbon");
        return super.writeStorable(nbt);
    }

    @Override
    public void readSync(CompoundTag nbt) {
        super.readSync(nbt);
        this.burning = nbt.getBoolean("burning");
    }

    @Override
    public CompoundTag writeSync(CompoundTag nbt) {
        nbt.putBoolean("burning", this.burning);
        return super.writeSync(nbt);
    }

    @Override
    protected int postTick(Level world) {
        if (!isRemote() && checkRedstone()) {
            boolean flag = false;
            if (this.carbon.isEmpty()) {
                ItemStack stack = this.inv.getStackInSlot(1);
                if (!stack.isEmpty()) {
                    int burnTime = FuelRegistry.get(stack);
                    if (burnTime > 0) {
                        long perFuelTick = Powah.config().general.energy_per_fuel_tick;
                        this.carbon.setAll(burnTime * perFuelTick);
                        if (EnvHandler.INSTANCE.hasContainerItem(stack)) {
                            this.inv.setStackInSlot(1, EnvHandler.INSTANCE.getContainerItem(stack));
                        } else {
                            stack.shrink(1);
                        }
                        sync(4);
                    }
                }
            }
            if (!this.carbon.isEmpty()) {
                if (!this.energy.isFull()) {
                    long toProduce = Math.min(this.energy.getEmpty(), Math.min(getGeneration(), (long) this.carbon.getTicks()));
                    this.energy.produce(toProduce);
                    this.carbon.back(toProduce);
                    if (this.carbon.isEmpty()) {
                        this.carbon.setAll(0);
                    }
                    flag = true;
                    sync(4);
                }
            }
            if (this.burning != flag) {
                this.burning = flag;
                sync(4);
            }
        }
        return chargeItems(1) + extractFromSides(world) > 0 ? 10 : -1;
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean canInsert(int index, ItemStack stack) {
        return index == 1 && FuelRegistry.get(stack) > 0
                || index == 0 && Energy.chargeable(stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }

    public Ticker getCarbon() {
        return this.carbon;
    }

    public boolean isBurning() {
        return this.burning;
    }
}
