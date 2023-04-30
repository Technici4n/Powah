package owmii.powah.lib.logistics.energy;

import static owmii.powah.lib.logistics.Transfer.ALL;
import static owmii.powah.lib.logistics.Transfer.NONE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.lib.logistics.Transfer;

public class SideConfig {
    private final Transfer[] transfers = new Transfer[6];
    private final AbstractEnergyStorage storage;
    private boolean isSetFromNBT;

    public SideConfig(AbstractEnergyStorage storage) {
        this.storage = storage;
        Arrays.fill(this.transfers, NONE);
    }

    public void init() {
        if (!this.isSetFromNBT) {
            for (Direction side : Direction.values()) {
                setType(side, this.storage.getTransferType());
            }
        }
    }

    public void read(CompoundTag nbt) {
        if (nbt.contains("side_transfer_type", Tag.TAG_INT_ARRAY)) {
            int[] arr = nbt.getIntArray("side_transfer_type");
            for (int i = 0; i < arr.length; i++) {
                this.transfers[i] = Transfer.values()[arr[i]];
            }
            this.isSetFromNBT = true;
        }
    }

    public CompoundTag write(CompoundTag nbt) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0, valuesLength = this.transfers.length; i < valuesLength; i++) {
            list.add(i, this.transfers[i].ordinal());
        }
        nbt.putIntArray("side_transfer_type", list);
        return nbt;
    }

    public void nextTypeAll() {
        if (isAllEquals()) {
            for (Direction side : Direction.values()) {
                nextType(side);
            }
        } else {
            for (Direction side : Direction.values()) {
                setType(side, ALL);
            }
        }
    }

    public boolean isAllEquals() {
        boolean flag = true;
        int first = -1;
        for (int i = 1; i < 6; i++) {
            if (this.storage.isEnergyPresent(Direction.from3DDataValue(i))) {
                if (first < 0) {
                    first = this.transfers[i].ordinal();
                } else if (this.transfers[i].ordinal() != first) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    public void nextType(@Nullable Direction side) {
        setType(side, getType(side).next(this.storage.getTransferType()));
    }

    public Transfer getType(@Nullable Direction side) {
        if (side != null) {
            return this.transfers[side.get3DDataValue()];
        }
        return NONE;
    }

    public void setType(@Nullable Direction side, Transfer type) {
        if (side == null || this.storage.getTransferType().equals(NONE))
            return;
        if (!this.storage.isEnergyPresent(side))
            return;
        this.transfers[side.get3DDataValue()] = type;
    }
}
