package owmii.lib.logistics.inventory;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import owmii.lib.logistics.Transfer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static owmii.lib.logistics.Transfer.ALL;
import static owmii.lib.logistics.Transfer.NONE;

public class SidedHopperConfig {
    private final Transfer[] transfers = new Transfer[6];
    private final ISidedHopper hopper;

    public SidedHopperConfig(ISidedHopper hopper) {
        this.hopper = hopper;
        Arrays.fill(this.transfers, NONE);
    }

    public void read(CompoundTag nbt) {
        if (nbt.contains("hopper_transfer_type", Tag.TAG_INT_ARRAY)) {
            int[] arr = nbt.getIntArray("hopper_transfer_type");
            for (int i = 0; i < arr.length; i++) {
                setType(Direction.from3DDataValue(i), Transfer.values()[arr[i]]);
            }
        }
    }

    public CompoundTag write(CompoundTag nbt) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0, valuesLength = this.transfers.length; i < valuesLength; i++) {
            list.add(i, this.transfers[i].ordinal());
        }
        nbt.putIntArray("hopper_transfer_type", list);
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
        int first = this.transfers[0].ordinal();
        for (int i = 1; i < 6; i++) {
            if (this.transfers[i].ordinal() != first) {
                return false;
            }
        }
        return true;
    }

    public void nextType(@Nullable Direction side) {
        setType(side, getType(side).next(this.hopper.getItemTransfer()));
    }

    public Transfer getType(@Nullable Direction side) {
        if (side != null) {
            return this.transfers[side.get3DDataValue()];
        }
        return NONE;
    }

    public void setType(@Nullable Direction side, Transfer type) {
        if (side == null || this.hopper.getItemTransfer().equals(NONE))
            return;
        this.transfers[side.get3DDataValue()] = type;
        this.hopper.getSidedHopper().setPush(side, type.canExtract);
        this.hopper.getSidedHopper().setPull(side, type.canReceive);
    }
}
