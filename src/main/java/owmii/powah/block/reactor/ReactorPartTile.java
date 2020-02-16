package owmii.powah.block.reactor;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import owmii.lib.block.TileBase;
import owmii.lib.util.Data;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;

import javax.annotation.Nullable;
import java.util.Optional;

public class ReactorPartTile extends TileBase<Tier, ReactorBlock> {
    private BlockPos corePos = BlockPos.ZERO;
    private boolean extractor;
    private boolean built;

    public ReactorPartTile(Tier variant) {
        super(ITiles.REACTOR_PART, variant);
    }

    public ReactorPartTile() {
        this(Tier.STARTER);
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        this.built = compound.getBoolean("Built");
        this.extractor = compound.getBoolean("Extractor");
        this.corePos = Data.readPos(compound, "CorePos");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        compound.putBoolean("Built", this.built);
        compound.putBoolean("Extractor", this.extractor);
        Data.writePos(compound, this.corePos, "CorePos");
        return super.writeSync(compound);
    }

    public void demolish(World world) {
        TileEntity tile = world.getTileEntity(this.corePos);
        if (tile instanceof ReactorTile) {
            ReactorTile reactor = (ReactorTile) tile;
            reactor.demolish(world);
        }
    }

    public Optional<ReactorTile> core() {
        TileEntity tile = getTileEntity(this.corePos);
        if (tile instanceof ReactorTile) {
            return Optional.of((ReactorTile) tile);
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
