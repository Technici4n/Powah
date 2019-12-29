package owmii.powah.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import owmii.powah.block.PowahTile;

import javax.annotation.Nullable;

import static owmii.powah.energy.PowerMode.*;

public class SideConfig {
    private final PowerMode[] powerMode = new PowerMode[]{NON, NON, NON, NON, NON, NON};
    private final Direction[] sides = Direction.values();
    private final PowahTile tile;

    public SideConfig(PowahTile tile) {
        this.tile = tile;
    }

    public void init() {
        for (Direction side : Direction.values()) {
            if (this.tile.isEnergyPresent(side)) {
                boolean e = this.tile.internal.canExtract() && this.tile.canExtractFromSides();
                boolean r = this.tile.internal.canReceive();
                setPowerMode(side, r && e ? ALL : r ? IN : e ? OUT : NON);
            }
        }
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
        return NON;
    }

    public void setPowerMode(@Nullable Direction side, PowerMode powerMode) {
        if (side != null) {
            this.powerMode[side.getIndex()] = powerMode;
        }
    }

    public void nextPowerModeAllSides() {
        int i = -1;
        for (Direction side : Direction.values()) {
            if (this.tile.isEnergyPresent(side)) {
                i = i == -1 ? getPowerMode(side).ordinal() + 1 : i;
                PowerMode mode = PowerMode.values()[i > 3 ? 0 : i];
                if ((!this.tile.internal.canExtract() || !this.tile.canExtractFromSides()) && mode.isOut())
                    mode = IN;
                else if (!this.tile.internal.canReceive() && mode.isIn())
                    mode = NON.equals(mode) || ALL.equals(mode) ? OUT : NON;
                setPowerMode(side, mode);
            }
        }
    }

    public void nextPowerMode(@Nullable Direction side) {
        if (this.tile.isEnergyPresent(side)) {
            int i = getPowerMode(side).ordinal() + 1;
            PowerMode mode = PowerMode.values()[i > 3 ? 0 : i];
            if ((!this.tile.internal.canExtract() || !this.tile.canExtractFromSides()) && mode.isOut())
                mode = IN;
            else if (!this.tile.internal.canReceive() && mode.isIn())
                mode = NON.equals(mode) || ALL.equals(mode) ? OUT : NON;
            setPowerMode(side, mode);
        }
    }

    public PowerMode getPowerMode(int i) {
        return this.powerMode[i];
    }
}
