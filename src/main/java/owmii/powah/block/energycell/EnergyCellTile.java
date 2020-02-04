package owmii.powah.block.energycell;

import net.minecraft.util.Direction;
import net.minecraft.world.World;
import owmii.lib.block.TileBase;
import owmii.lib.energy.SideConfig;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;

import javax.annotation.Nullable;

public class EnergyCellTile extends TileBase.EnergyStorage<Tier, EnergyCellBlock> {
    public EnergyCellTile(final Tier tier) {
        super(ITiles.ENERGY_CELL, tier);
    }

    public EnergyCellTile() {
        this(Tier.BASIC);
    }

    protected void onFirstTick(final World world) {
        super.onFirstTick(world);
        if (this.isCreative()) {
            this.energy.setStored(this.defaultEnergyCapacity());
        }
    }

    public long extractEnergy(final int maxExtract, final boolean simulate, @Nullable final Direction side) {
        return super.extractEnergy(maxExtract, this.isCreative(), side);
    }

    public int getChargingSlots() {
        return 2;
    }

    public boolean keepEnergy() {
        return !this.isCreative();
    }

    public SideConfig.Type getTransferType() {
        return this.isCreative() ? SideConfig.Type.OUT : super.getTransferType();
    }

    public boolean isCreative() {
        return this.getVariant().equals(Tier.CREATIVE);
    }
}
