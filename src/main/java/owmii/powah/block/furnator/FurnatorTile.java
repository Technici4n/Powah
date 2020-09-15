package owmii.powah.block.furnator;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import owmii.lib.block.AbstractEnergyProvider;
import owmii.lib.block.IInventoryHolder;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.util.Ticker;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.config.Configs;
import owmii.powah.config.generator.FurnatorConfig;

public class FurnatorTile extends AbstractEnergyProvider<Tier, FurnatorConfig, FurnatorBlock> implements IInventoryHolder {
    protected final Ticker carbon = Ticker.empty();
    protected boolean burning;

    public FurnatorTile(Tier variant) {
        super(Tiles.FURNATOR, variant);
        this.inv.set(2);
    }

    public FurnatorTile() {
        this(Tier.STARTER);
    }

    @Override
    public void readStorable(CompoundNBT nbt) {
        super.readStorable(nbt);
        this.carbon.read(nbt, "carbon");
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT nbt) {
        this.carbon.write(nbt, "carbon");
        return super.writeStorable(nbt);
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        super.readSync(nbt);
        this.burning = nbt.getBoolean("burning");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        nbt.putBoolean("burning", this.burning);
        return super.writeSync(nbt);
    }

    @Override
    protected int postTick(World world) {
        if (!isRemote() && checkRedstone()) {
            boolean flag = false;
            if (this.carbon.isEmpty()) {
                ItemStack stack = this.inv.getStackInSlot(1);
                if (!stack.isEmpty()) {
                    int burnTime = ForgeHooks.getBurnTime(stack);
                    if (burnTime > 0) {
                        long perFuelTick = Configs.GENERAL.fuelTicks.get();
                        this.carbon.setAll(burnTime * perFuelTick);
                        if (stack.hasContainerItem()) {
                            this.inv.setStack(1, stack.getContainerItem());
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
        return index == 1 && ForgeHooks.getBurnTime(stack) > 0
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
