package owmii.powah.block.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.util.NBT;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;

import javax.annotation.Nullable;
import java.util.Optional;

public class ReactorPartTile extends AbstractTileEntity<Tier, ReactorBlock> {
    private BlockPos corePos = BlockPos.ZERO;
    private boolean extractor;
    private boolean built;

    public ReactorPartTile(BlockPos pos, BlockState state, Tier variant) {
        super(Tiles.REACTOR_PART.get(), pos, state, variant);
    }

    public ReactorPartTile(BlockPos pos, BlockState state) {
        this(pos, state, Tier.STARTER);
    }

    @Override
    public void readSync(CompoundTag compound) {
        super.readSync(compound);
        this.built = compound.getBoolean("built");
        this.extractor = compound.getBoolean("extractor");
        this.corePos = NBT.readPos(compound, "core_pos");
    }

    @Override
    public CompoundTag writeSync(CompoundTag compound) {
        compound.putBoolean("built", this.built);
        compound.putBoolean("extractor", this.extractor);
        NBT.writePos(compound, this.corePos, "core_pos");
        return super.writeSync(compound);
    }

    public void demolish(Level world) {
        BlockEntity tile = world.getBlockEntity(this.corePos);
        if (tile instanceof ReactorTile) {
            ReactorTile reactor = (ReactorTile) tile;
            reactor.demolish(world);
        }
    }

    public Optional<ReactorTile> core() {
        if (this.level != null) {
            BlockEntity tile = this.level.getBlockEntity(this.corePos);
            if (tile instanceof ReactorTile) {
                return Optional.of((ReactorTile) tile);
            }
        }
        return Optional.empty();
    }

    public BlockPos getCorePos() {
        return this.corePos;
    }

    public void setCorePos(BlockPos corePos) {
        this.corePos = corePos;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (core().isPresent()) {
            if (cap != CapabilityEnergy.ENERGY || this.extractor) {
                return core().get().getCapability(cap, side);
            }
        }
        return super.getCapability(cap, side);
    }

    public void setExtractor(boolean extractor) {
        this.extractor = extractor;
    }

    public void setBuilt(boolean built) {
        this.built = built;
    }

    public boolean isBuilt() {
        return this.built;
    }
}
