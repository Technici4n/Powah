package owmii.powah.block.reactor;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.util.NBT;

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

    public void setExtractor(boolean extractor) {
        this.extractor = extractor;
    }

    public boolean isExtractor() {
        return this.extractor;
    }

    public void setBuilt(boolean built) {
        this.built = built;
    }

    public boolean isBuilt() {
        return this.built;
    }
}
