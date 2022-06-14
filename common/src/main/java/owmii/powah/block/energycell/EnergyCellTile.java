package owmii.powah.block.energycell;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import owmii.powah.config.v2.types.EnergyConfig;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.logistics.Transfer;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;

import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EnergyCellTile extends AbstractEnergyStorage<EnergyConfig, EnergyCellBlock> implements IInventoryHolder {
    public EnergyCellTile(BlockPos pos, BlockState state, Tier tier) {
        super(Tiles.ENERGY_CELL.get(), pos, state, tier);
        this.inv.add(2);
    }

    public EnergyCellTile(BlockPos pos, BlockState state) {
        this(pos, state, Tier.STARTER);
    }

    @Override
    protected void onFirstTick(Level world) {
        super.onFirstTick(world);
        if (isCreative()) {
            this.energy.setStored(getEnergyCapacity());
        }
    }

    @Override
    protected int postTick(Level world) {
        return chargeItems(2) + extractFromSides(world) > 0 ? 10 : -1;
    }

    @Override
    public long extractEnergy(long maxExtract, boolean simulate, @Nullable Direction side) {
        return super.extractEnergy(maxExtract, simulate || isCreative(), side);
    }

    @Override
    public long receiveEnergy(long maxReceive, boolean simulate, @Nullable Direction side) {
        return super.receiveEnergy(maxReceive, simulate, side);
    }

    @Override
    public boolean canExtractEnergy(@Nullable Direction side) {
        return checkRedstone() && super.canExtractEnergy(side);
    }

    @Override
    public boolean canReceiveEnergy(@Nullable Direction side) {
        return checkRedstone() && super.canReceiveEnergy(side);
    }

    @Override
    public boolean keepEnergy() {
        return !isCreative();
    }

    @Override
    public Transfer getTransferType() {
        return isCreative() ? Transfer.EXTRACT : super.getTransferType();
    }

    public boolean isCreative() {
        return this.getVariant().equals(Tier.CREATIVE);
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
