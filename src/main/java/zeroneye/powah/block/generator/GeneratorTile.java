package zeroneye.powah.block.generator;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import zeroneye.powah.block.PowahTile;

public abstract class GeneratorTile extends PowahTile {
    protected int perTick;
    public int nextGenCap;
    public int nextGen;


    public GeneratorTile(TileEntityType<?> type, int capacity, int transfer, int perTick) {
        super(type, capacity, 0, transfer, false);
        this.perTick = perTick;
    }

    @Override
    public void readStorable(CompoundNBT compound) {
        super.readStorable(compound);
        this.perTick = compound.getInt("PerTick");
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT compound) {
        compound.putInt("PerTick", this.perTick);
        return super.writeStorable(compound);
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        this.nextGenCap = compound.getInt("NextGenCap");
        this.nextGen = compound.getInt("NextGen");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        compound.putInt("NextGenCap", this.nextGenCap);
        compound.putInt("NextGen", this.nextGen);
        return super.writeSync(compound);
    }

    @Override
    protected boolean postTicks() {
        if (this.world == null) return false;
        boolean flag = isGenerating();
        boolean flag1 = super.postTicks();
        if (perTick() > 0) {
            int toGenerate = 0;
            if (this.nextGen >= perTick()) {
                toGenerate = perTick();
                this.nextGen -= perTick();
                if (this.nextGen <= 0) {
                    this.nextGenCap = 0;
                }
            } else {
                if (this.nextGen > 0) {
                    toGenerate = this.nextGen;
                    this.nextGen = 0;
                    this.nextGenCap = 0;
                }
            }
            generate();
            if (toGenerate > 0) {
                int stored = this.internal.getEnergyStored();
                int capacity = this.internal.getMaxEnergyStored();
                this.internal.setEnergy(stored + Math.min(capacity - stored, toGenerate));
                flag1 = true;
            }
        }
        if (flag != isGenerating() && getBlock() instanceof GeneratorBlock) {
            if (((GeneratorBlock) getBlock()).hasLitProp()) {
                flag1 = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(GeneratorBlock.LIT, isGenerating()), 3);
            }
        }
        return flag1;
    }

    @Override
    public int getChargingSlots() {
        return 1;
    }

    protected abstract void generate();

    protected boolean isGenerating() {
        return this.nextGen > 0;
    }

    public int perTick() {
        return perTick;
    }
}
