package owmii.powah.block.furnator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import owmii.powah.Powah;
import owmii.powah.block.Tiles;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.PowahBaseGeneratorBlockEntity;
import owmii.powah.util.ChargeUtil;
import owmii.powah.util.Ticker;

public class FurnatorBlockEntity extends PowahBaseGeneratorBlockEntity<FurnatorBlock> implements IInventoryHolder {
    protected final Ticker carbon = Ticker.empty();
    protected boolean burning;

    public FurnatorBlockEntity(BlockPos pos, BlockState state) {
        super(Tiles.FURNATOR.get(), pos, state);
    }

    @Override
    protected int getInternalInventorySize() {
        return 2;
    }

    @Override
    public void readStorable(ValueInput input) {
        super.readStorable(input);
        this.carbon.read(input, "carbon");
    }

    @Override
    public void writeStorable(ValueOutput output) {
        this.carbon.write(output, "carbon");
        super.writeStorable(output);
    }

    @Override
    public void readSync(ValueInput input) {
        super.readSync(input);
        this.burning = input.getBooleanOr("burning", false);
    }

    @Override
    public void writeSync(ValueOutput output) {
        output.putBoolean("burning", this.burning);
        super.writeSync(output);
    }

    @Override
    protected int postTick(Level world) {
        if (!isRemote() && checkRedstone()) {
            boolean flag = false;
            if (this.carbon.isEmpty()) {
                ItemStack stack = this.inv.getStackInSlot(1);
                if (!stack.isEmpty()) {
                    int burnTime = stack.getBurnTime(RecipeType.SMELTING, getLevel().fuelValues());
                    if (burnTime > 0) {
                        long perFuelTick = Powah.config().general.energy_per_fuel_tick;
                        this.carbon.setAll(burnTime * perFuelTick);
                        if (stack.getCraftingRemainder() != null) {
                            this.inv.setStackInSlot(1, stack.getCraftingRemainder().create());
                        } else {
                            stack.shrink(1);
                        }
                        sync(4);
                    }
                }
            }
            if (!this.carbon.isEmpty()) {
                if (!getEnergy().isFull()) {
                    long toProduce = Math.min(getEnergy().getEmpty(), Math.min(getEnergyGeneration(), (long) this.carbon.getTicks()));
                    getEnergy().produce(toProduce);
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
        return index == 1 && stack.getBurnTime(RecipeType.SMELTING, getLevel().fuelValues()) > 0
                || index == 0 && ChargeUtil.isChargeableItem(stack);
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
