package owmii.powah.block.generator.panel.solar;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import owmii.lib.util.Misc;
import owmii.lib.util.Time;
import owmii.powah.block.ITiles;
import owmii.powah.block.generator.GeneratorTile;

import javax.annotation.Nullable;

public class SolarPanelTile extends GeneratorTile {
    private boolean canSeeSunLight;
    private int noSkyDelay;

    public SolarPanelTile(int capacity, int transfer, int perTick) {
        super(ITiles.SOLAR_PANEL, capacity, transfer, perTick);
    }

    public SolarPanelTile() {
        this(0, 0, 0);
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        this.canSeeSunLight = compound.getBoolean("CanSeeSunLight");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        compound.putBoolean("CanSeeSunLight", this.canSeeSunLight);
        return super.writeSync(compound);
    }

    @Override
    protected void generate() {
        if (this.world == null) return;
        if (this.world.isRemote) return;

        if (!Time.isDay(this.world) && this.canSeeSunLight || this.ticks % 40L == 0L) {
            this.canSeeSunLight = Time.isDay(this.world) && Misc.canBlockSeeSky(this.world, this.pos);
            markDirtyAndSync();
        }

        if (this.internal.isFull()) return;

        if (this.nextGen <= 0) {
            if (this.canSeeSunLight) {
                this.nextGen = this.perTick;
            }
        }
    }

    public boolean canSeeSunLight() {
        return this.canSeeSunLight;
    }

    @Override
    public int getChargingSlots() {
        return 1;
    }

    @Override
    public boolean isEnergyPresent(@Nullable Direction side) {
        return side != Direction.UP;
    }
}
