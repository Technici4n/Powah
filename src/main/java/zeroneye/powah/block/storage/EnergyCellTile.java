package zeroneye.powah.block.storage;

import zeroneye.powah.block.ITiles;
import zeroneye.powah.block.PowahTile;

public class EnergyCellTile extends PowahTile {
    public EnergyCellTile(int capacity, int transfer, boolean isCreative) {
        super(ITiles.ENERGY_CELL, capacity, transfer, transfer, isCreative);
    }

    public EnergyCellTile() {
        this(0, 0, false);
    }

    @Override
    public int getChargingSlots() {
        return 2;
    }
}
