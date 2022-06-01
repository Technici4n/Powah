package owmii.lib.logistics.inventory;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

public class SidedHopper {
    private final Hopper[] hoppers = new Hopper[6];
    private final Inventory inv;

    public SidedHopper(Inventory inv) {
        this.inv = inv;
        for (int i = 0; i < this.hoppers.length; i++) {
            this.hoppers[i] = new Hopper(inv);
        }
    }

    public void read(CompoundTag nbt, String key) {
        for (int i = 0; i < this.hoppers.length; i++) {
            Direction side = Direction.values()[i];
            this.hoppers[i].read(nbt, key + "_" + side.getName());
        }
    }

    public CompoundTag write(CompoundTag nbt, String key) {
        for (int i = 0; i < this.hoppers.length; i++) {
            Direction side = Direction.values()[i];
            this.hoppers[i].write(nbt, key + "_" + side.getName());
        }
        return nbt;
    }

    public void switchPull(Direction side) {
        getHopper(side).switchPull();
    }

    public void switchPush(Direction side) {
        getHopper(side).switchPush();
    }

    public boolean canPull(Direction side) {
        return getHopper(side).canPull();
    }

    public boolean canPush(Direction side) {
        return getHopper(side).canPush();
    }

    public void setPull(Direction side, boolean pull) {
        getHopper(side).setPull(pull);
    }

    public void setPush(Direction side, boolean push) {
        getHopper(side).setPush(push);
    }

    public Map<Direction, Hopper> getActiveHoppers() {
        Map<Direction, Hopper> hoppers = new HashMap<>();
        for (int i = 0; i < this.hoppers.length; i++) {
            Hopper hopper = this.hoppers[i];
            if (hopper.isActive()) {
                hoppers.put(Direction.from3DDataValue(i), hopper);
            }
        }
        return hoppers;
    }

    public Hopper getHopper(Direction side) {
        return this.hoppers[side.get3DDataValue()];
    }

    public Hopper[] getHoppers() {
        return this.hoppers;
    }
}
