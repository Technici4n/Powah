package zeroneye.powah.block.generator.panel.solar;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import zeroneye.powah.block.ITiles;
import zeroneye.powah.block.generator.GeneratorTile;

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

        if (!this.world.isDaytime() && this.canSeeSunLight || this.world.getGameTime() % 40L == 0L) {
            this.canSeeSunLight = this.world.isDaytime() && this.world.canBlockSeeSky(this.pos);
            markDirtyAndSync();
        }

        if (this.internal.isFull()) return;

        if (this.nextGen <= 0 && this.noSkyDelay <= 0) {
            if (this.canSeeSunLight) {
                this.nextGen = this.perTick;
            } else {
                this.noSkyDelay = 40;
            }
        } else {
            if (this.noSkyDelay > 0) {
                this.noSkyDelay--;
            }
        }
    }

    public boolean canSeeSunLight() {
        return canSeeSunLight;
    }

    @Override
    public int getChargingSlots() {
        return 1;
    }

    @Override
    public boolean canExtract(@Nullable Direction side) {
        return side != Direction.UP && super.canExtract(side);
    }
}
