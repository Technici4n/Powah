package owmii.powah.block.energizing;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import owmii.lib.block.AbstractBlock;
import owmii.lib.block.TileBase;
import owmii.lib.util.Ticker;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;

import javax.annotation.Nullable;

public class EnergizingRodTile extends TileBase.EnergyStorage<Tier, EnergizingRodBlock> {
    private BlockPos orbPos = BlockPos.ZERO;

    public final Ticker coolDown = new Ticker(15);

    public EnergizingRodTile(Tier variant) {
        super(ITiles.ENERGIZING_ROD, variant);
    }

    public EnergizingRodTile() {
        this(Tier.STARTER);
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        this.orbPos = NBTUtil.readBlockPos(compound.getCompound("OrbPos"));
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        compound.put("OrbPos", NBTUtil.writeBlockPos(this.orbPos));
        return super.writeSync(compound);
    }

    @Override
    protected boolean postTicks(World world) {
        boolean flag = false;
        if (!this.orbPos.equals(BlockPos.ZERO)) {
            TileEntity tileEntity = world.getTileEntity(this.orbPos);
            if (tileEntity instanceof EnergizingOrbTile) {
                EnergizingOrbTile orb = (EnergizingOrbTile) tileEntity;

                if (orb.containRecipe() && this.energy.hasEnergy()) {
                    this.coolDown.onward();
                    flag = true;
                } else if (this.coolDown.getTicks() > 0) {
                    this.coolDown.back();
                    flag = true;
                }

                if (orb.containRecipe()) {
                    if (this.coolDown.ended()) {
                        long fill = Math.min(this.energy.getEnergyStored(), defaultTransfer());
                        orb.fillEnergy(fill);
                        this.energy.consume(fill);
                        flag = true;
                    }
                }
            }
        }
        return super.postTicks(world) || flag;
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
        int range = Configs.ENERGIZING.range.get();
        return new AxisAlignedBB(getPos()).grow(range);
    }

    @Override
    public boolean isEnergyPresent(@Nullable Direction side) {
        return side != null && side.equals(getBlockState().get(AbstractBlock.FACING));
    }
}
