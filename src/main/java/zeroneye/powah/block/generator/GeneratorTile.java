package zeroneye.powah.block.generator;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import zeroneye.powah.block.PowahTile;

public abstract class GeneratorTile extends PowahTile {
    protected int perTick;
    protected int nextGen;


    public GeneratorTile(TileEntityType<?> type, int capacity, int transfer, int perTick) {
        super(type, capacity, 0, transfer, false);
        this.perTick = perTick;
    }

    @Override
    public void readStorable(CompoundNBT compound) {
        super.readStorable(compound);
        this.perTick = compound.getInt("PerTick");
        this.nextGen = compound.getInt("NextGen");
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT compound) {
        compound.putInt("PerTick", this.perTick);
        compound.putInt("NextGen", this.nextGen);
        return super.writeStorable(compound);
    }

    @Override
    protected boolean postTicks() {
        super.postTicks();
        if (this.world == null) return false;
        boolean flag = isGenerating();
        boolean flag1 = false;
        if (!this.internal.isFull() && perTick() > 0) {
            int toGenerate = toGenerate();
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
        return flag1 && super.postTicks();
    }

    protected abstract int toGenerate();

    protected boolean isGenerating() {
        return this.nextGen > 0;
    }

    public int perTick() {
        return perTick;
    }
}
