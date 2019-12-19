package zeroneye.powah.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import zeroneye.powah.block.PowahTile;

import javax.annotation.Nullable;

public class SideConfig {
    private final PowerMode[] powerMode = new PowerMode[Direction.values().length];
    private final Direction[] sides = Direction.values();
    private final PowahTile tile;

    public SideConfig(PowahTile tile) {
        this.tile = tile;
        for (Direction side : Direction.values()) {
            setPowerMode(side, PowerMode.NON);
        }
        nextPowerMode();
    }

    public CompoundNBT write(CompoundNBT compound) {
        for (int i = 0; i < this.sides.length; i++) {
            Direction side = this.sides[i];
            if (side != null) {
                compound.putInt("PowerMode_" + side.getIndex(), this.powerMode[i].ordinal());
            }
        }
        return compound;
    }

    public void read(CompoundNBT compound) {
        for (int i = 0; i < this.sides.length; i++) {
            Direction side = this.sides[i];
            String key = "PowerMode_" + side.getIndex();
            if (compound.contains(key, Constants.NBT.TAG_INT)) {
                setPowerMode(side, PowerMode.values()[compound.getInt("PowerMode_" + side.getIndex())]);
            }
        }
    }

    public PowerMode getPowerMode(@Nullable Direction side) {
        if (side != null) {
            return this.powerMode[side.getIndex()];
        }
        return PowerMode.NON;
    }

    public void setPowerMode(@Nullable Direction side, PowerMode powerMode) {
        if (side != null) {
            this.powerMode[side.getIndex()] = powerMode;
        }
    }

    public void nextPowerMode() {
        int i = -1;
        for (Direction side : Direction.values()) {
            i = i == -1 ? getPowerMode(side).ordinal() + 1 : i;
            PowerMode powerMode = PowerMode.values()[i > 3 ? 0 : i];
            if ((!this.tile.getInternal().canExtract() || !this.tile.canExtractFromSides()) && powerMode.isOut()) {
                powerMode = PowerMode.IN;
            } else if (!this.tile.getInternal().canReceive() && powerMode.isIn()) {
                powerMode = PowerMode.NON.equals(powerMode) || PowerMode.ALL.equals(powerMode) ? PowerMode.OUT : PowerMode.NON;
            }
            setPowerMode(side, powerMode);
        }
    }

    public void nextPowerMode(@Nullable Direction side) {
        int i = getPowerMode(side).ordinal() + 1;
        PowerMode powerMode = PowerMode.values()[i > 3 ? 0 : i];
        if ((!this.tile.getInternal().canExtract() || !this.tile.canExtractFromSides()) && powerMode.isOut()) {
            powerMode = PowerMode.IN;
        } else if (!this.tile.getInternal().canReceive() && powerMode.isIn()) {
            powerMode = PowerMode.NON.equals(powerMode) || PowerMode.ALL.equals(powerMode) ? PowerMode.OUT : PowerMode.NON;
        }
        setPowerMode(side, powerMode);
    }

    public PowerMode getPowerMode(int i) {
        return this.powerMode[i];
    }
}
