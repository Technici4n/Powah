package owmii.powah.block.solar;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import owmii.lib.block.AbstractEnergyProvider;
import owmii.lib.block.IInventoryHolder;
import owmii.lib.util.Misc;
import owmii.lib.util.Time;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;
import owmii.powah.config.generator.SolarConfig;
import owmii.powah.item.IItems;

import javax.annotation.Nullable;

public class SolarTile extends AbstractEnergyProvider<Tier, SolarConfig, SolarBlock> implements IInventoryHolder {
    private boolean canSeeSunLight;
    private boolean hasLensOfEnder;

    public SolarTile(Tier variant) {
        super(ITiles.SOLAR_PANEL, variant);
        this.inv.add(1);
    }

    public SolarTile() {
        this(Tier.STARTER);
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        this.canSeeSunLight = compound.getBoolean("can_see_sun_light");
        this.hasLensOfEnder = compound.getBoolean("has_lens_of_ender");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        compound.putBoolean("can_see_sun_light", this.canSeeSunLight);
        compound.putBoolean("has_lens_of_ender", this.hasLensOfEnder);
        return super.writeSync(compound);
    }

    @Override
    protected int postTick(World world) {
        if (isRemote()) return -1;
        boolean flag = chargeItems(1) + extractFromSides(world) > 0;
        if (checkRedstone()) {
            if (!this.hasLensOfEnder && (!Time.isDay(world) && this.canSeeSunLight || this.ticks % 40L == 0L)) {
                this.canSeeSunLight = Time.isDay(world) && Misc.canBlockSeeSky(world, this.pos);
                sync();
            }
            if (!this.energy.isFull()) {
                if (this.hasLensOfEnder && Time.isDay(world) || this.canSeeSunLight) {
                    this.energy.produce(getGeneration());
                    flag = true;
                }
            }
        }
        return flag ? 5 : -1;
    }

    @Override
    public void onRemoved(World world, BlockState state, BlockState newState, boolean isMoving) {
        super.onRemoved(world, state, newState, isMoving);
        if (state.getBlock() != newState.getBlock()) {
            if (this.hasLensOfEnder) {
                Block.spawnAsEntity(world, this.pos, new ItemStack(IItems.LENS_OF_ENDER));
            }
        }
    }

    public boolean canSeeSunLight() {
        return this.canSeeSunLight;
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public boolean isEnergyPresent(@Nullable Direction side) {
        return Direction.DOWN.equals(side);
    }

    public boolean hasLensOfEnder() {
        return this.hasLensOfEnder;
    }

    public void setHasLensOfEnder(boolean hasLensOfEnder) {
        this.hasLensOfEnder = hasLensOfEnder;
        sync();
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }
}
