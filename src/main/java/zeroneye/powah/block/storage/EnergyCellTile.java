package zeroneye.powah.block.storage;

import zeroneye.powah.block.ITiles;
import zeroneye.powah.block.PowahTile;

public class EnergyCellTile extends PowahTile {
    public EnergyCellTile(int capacity, int transfer, boolean canReceive, boolean canExtract, boolean isCreative) {
        super(ITiles.ENERGY_CELL, capacity, transfer, transfer, canReceive, canExtract, isCreative);
    }

    public EnergyCellTile() {
        super(ITiles.ENERGY_CELL);
    }

    @Override
    public int getChargingSlots() {
        return 2;
    }
}
