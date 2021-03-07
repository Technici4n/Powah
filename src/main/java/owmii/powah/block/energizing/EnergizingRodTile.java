package owmii.powah.block.energizing;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import owmii.lib.block.AbstractEnergyStorage;
import owmii.lib.util.NBT;
import owmii.lib.util.Ticker;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.config.Configs;
import owmii.powah.config.EnergizingConfig;

import javax.annotation.Nullable;

public class EnergizingRodTile extends AbstractEnergyStorage<Tier, EnergizingConfig, EnergizingRodBlock> {
    private BlockPos orbPos = BlockPos.ZERO;
    public final Ticker coolDown = new Ticker(20);

    public EnergizingRodTile(Tier variant) {
        super(Tiles.ENERGIZING_ROD, variant);
    }

    public EnergizingRodTile() {
        this(Tier.STARTER);
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        super.readSync(nbt);
        this.orbPos = NBT.readPos(nbt, "OrbPos");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        NBT.writePos(nbt, this.orbPos, "OrbPos");
        return super.writeSync(nbt);
    }

    @Override
    protected int postTick(World world) {
        boolean flag = false;
        EnergizingOrbTile orb = getOrbTile();
        if (orb != null) {
            if (orb.containRecipe() && this.energy.hasEnergy()) {
                this.coolDown.onward();
                flag = true;
            } else if (this.coolDown.getTicks() > 0) {
                this.coolDown.back();
                flag = true;
            }

            if (this.coolDown.ended()) {
                long fill = Math.min(this.energy.getEnergyStored(), getBlock().getConfig().getTransfer(getVariant()));
                this.energy.consume(orb.fillEnergy(fill));
                flag = true;
            }
        }
        return flag ? 10 : -1;
    }

    @Nullable
    public EnergizingOrbTile getOrbTile() {
        if (this.world != null && this.orbPos != BlockPos.ZERO && world.isBlockPresent(this.orbPos)) {
            TileEntity tile = this.world.getTileEntity(this.orbPos);
            if (tile instanceof EnergizingOrbTile) {
                return (EnergizingOrbTile) tile;
            }
        }
        return null;
    }

    public boolean hasOrb() {
        return getOrbTile() != null;
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
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public boolean isEnergyPresent(@Nullable Direction side) {
        return side != null && side.equals(getBlockState().get(BlockStateProperties.FACING));
    }
}
