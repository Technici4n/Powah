package owmii.powah.block.energizing;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import owmii.lib.block.BlockBase;
import owmii.lib.util.Ticker;
import owmii.powah.block.ITiles;
import owmii.powah.block.PowahTile;
import owmii.powah.config.Config;

import javax.annotation.Nullable;

public class EnergizingRodTile extends PowahTile {
    private BlockPos orbPos = BlockPos.ZERO;
    public int energizingSpeed;

    public final Ticker coolDown = new Ticker(15);

    public EnergizingRodTile(int capacity, int maxReceive) {
        super(ITiles.ENERGIZING_ROD, capacity, maxReceive, 0, false);
    }

    public EnergizingRodTile() {
        this(0, 0);
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        this.energizingSpeed = compound.getInt("EnergizingSpeed");
        this.orbPos = NBTUtil.readBlockPos(compound.getCompound("OrbPos"));
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        compound.putInt("EnergizingSpeed", this.energizingSpeed);
        compound.put("OrbPos", NBTUtil.writeBlockPos(this.orbPos));
        return super.writeSync(compound);
    }

    @Override
    protected void firstTick() {
        if (this.world == null) return;
        if (!this.world.isRemote) {
            if (getBlock() instanceof EnergizingRodBlock) {
                EnergizingRodBlock rodBlock = (EnergizingRodBlock) getBlock();
                this.energizingSpeed = rodBlock.getEnergizingSpeed();
            }
        }
        super.firstTick();
    }

    @Override
    protected boolean postTicks() {
        if (this.world == null) return false;
        boolean flag = false;
        if (!this.orbPos.equals(BlockPos.ZERO)) {
            TileEntity tileEntity = this.world.getTileEntity(this.orbPos);
            if (tileEntity instanceof EnergizingOrbTile) {
                EnergizingOrbTile orb = (EnergizingOrbTile) tileEntity;

                if (orb.containRecipe() && this.internal.hasEnergy()) {
                    this.coolDown.onward();
                    flag = true;
                } else if (this.coolDown.getTicks() > 0) {
                    this.coolDown.back();
                    flag = true;
                }

                if (orb.containRecipe()) {
                    if (this.coolDown.ended()) {
                        int fill = Math.min(this.internal.getEnergyStored(), this.energizingSpeed);
                        orb.fillEnergy(fill);
                        this.internal.consume(fill);
                        flag = true;
                    }
                }
            }
        }
        return super.postTicks() || flag;
    }

    public boolean hasOrb() {
        if (this.world == null) return false;
        return this.orbPos != BlockPos.ZERO && this.world.getTileEntity(this.orbPos) instanceof EnergizingOrbTile;
    }

    public BlockPos getOrbPos() {
        return this.orbPos;
    }

    public void setOrbPos(BlockPos orbPos) {
        this.orbPos = orbPos;
        sync(2);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        int range = Config.ENERGIZING_CONFIG.range.get();
        return new AxisAlignedBB(getPos()).grow(range);
    }

    @Override
    public boolean isEnergyPresent(@Nullable Direction side) {
        return side != null && side.equals(getBlockState().get(BlockBase.FACING));
    }
}
