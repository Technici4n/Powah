package owmii.powah.block.energizing;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.config.v2.types.EnergyConfig;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.lib.util.NBT;
import owmii.powah.lib.util.Ticker;

public class EnergizingRodTile extends AbstractEnergyStorage<EnergyConfig, EnergizingRodBlock> {
    private BlockPos orbPos = BlockPos.ZERO;
    public final Ticker coolDown = new Ticker(20);

    public EnergizingRodTile(BlockPos pos, BlockState state, Tier variant) {
        super(Tiles.ENERGIZING_ROD.get(), pos, state, variant);
    }

    public EnergizingRodTile(BlockPos pos, BlockState state) {
        this(pos, state, Tier.STARTER);
    }

    @Override
    public void readSync(CompoundTag nbt) {
        super.readSync(nbt);
        this.orbPos = NBT.readPos(nbt, "OrbPos");
    }

    @Override
    public CompoundTag writeSync(CompoundTag nbt) {
        NBT.writePos(nbt, this.orbPos, "OrbPos");
        return super.writeSync(nbt);
    }

    @Override
    protected int postTick(Level world) {
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
        if (this.level != null && this.orbPos != BlockPos.ZERO && level.isLoaded(this.orbPos)) {
            BlockEntity tile = this.level.getBlockEntity(this.orbPos);
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

    public AABB getRenderBoundingBox() {
        int range = Powah.config().general.energizing_range;
        return new AABB(getBlockPos()).inflate(range);
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }

    @Override
    public boolean isEnergyPresent(@Nullable Direction side) {
        return side != null && side.equals(getBlockState().getValue(BlockStateProperties.FACING));
    }
}
