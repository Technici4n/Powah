package owmii.powah.block.reactor;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.util.NBT;

public class ReactorPartTile extends AbstractTileEntity<Tier, ReactorBlock> {
    @Nullable
    private BlockPos corePos;
    private boolean extractor;
    private boolean built;

    // Cache capabilities of the core-tile to forward capability lookups to it more quickly
    @Nullable
    private BlockCapabilityCache<IEnergyStorage, Direction> coreEnergyCache;
    @Nullable
    private BlockCapabilityCache<IItemHandler, Direction> coreItemCache;
    @Nullable
    private BlockCapabilityCache<IFluidHandler, Direction> coreFluidCache;

    public ReactorPartTile(BlockPos pos, BlockState state, Tier variant) {
        super(Tiles.REACTOR_PART.get(), pos, state, variant);
    }

    public ReactorPartTile(BlockPos pos, BlockState state) {
        this(pos, state, Tier.STARTER);
    }

    @Override
    public void readSync(CompoundTag compound, HolderLookup.Provider registries) {
        super.readSync(compound, registries);
        this.built = compound.getBoolean("built");
        this.extractor = compound.getBoolean("extractor");
        setCorePos(NbtUtils.readBlockPos(compound, "core_pos").orElse(null));
    }

    @Override
    public CompoundTag writeSync(CompoundTag compound, HolderLookup.Provider registries) {
        compound.putBoolean("built", this.built);
        compound.putBoolean("extractor", this.extractor);
        if (this.corePos != null) {
            NBT.writePos(compound, this.corePos, "core_pos");
        }
        return super.writeSync(compound, registries);
    }

    public void demolish(Level world) {
        core().ifPresent(reactor -> reactor.demolish(world));
    }

    @Nullable
    public IEnergyStorage getCoreEnergyStorage() {
        if (corePos == null)
            return null;
        if (this.level instanceof ServerLevel serverLevel) {
            if (coreEnergyCache == null) {
                coreEnergyCache = BlockCapabilityCache.create(Capabilities.EnergyStorage.BLOCK, serverLevel, getCorePos(), null);
            }
            return coreEnergyCache.getCapability();
        } else {
            return level.getCapability(Capabilities.EnergyStorage.BLOCK, getCorePos(), null);
        }
    }

    @Nullable
    public IItemHandler getCoreItemHandler() {
        if (corePos == null)
            return null;
        if (this.level instanceof ServerLevel serverLevel) {
            if (coreItemCache == null) {
                coreItemCache = BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, serverLevel, getCorePos(), null);
            }
            return coreItemCache.getCapability();
        } else {
            return level.getCapability(Capabilities.ItemHandler.BLOCK, getCorePos(), null);
        }
    }

    @Nullable
    public IFluidHandler getCoreFluidHandler() {
        if (corePos == null)
            return null;
        if (this.level instanceof ServerLevel serverLevel) {
            if (coreFluidCache == null) {
                coreFluidCache = BlockCapabilityCache.create(Capabilities.FluidHandler.BLOCK, serverLevel, getCorePos(), null);
            }
            return coreFluidCache.getCapability();
        } else {
            return level.getCapability(Capabilities.FluidHandler.BLOCK, getCorePos(), null);
        }
    }

    public Optional<ReactorTile> core() {
        if (this.level != null && this.corePos != null) {
            BlockEntity tile = this.level.getBlockEntity(this.corePos);
            if (tile instanceof ReactorTile reactorTile) {
                return Optional.of(reactorTile);
            }
        }
        return Optional.empty();
    }

    public BlockPos getCorePos() {
        return this.corePos != null ? this.corePos : BlockPos.ZERO;
    }

    public void setCorePos(@Nullable BlockPos corePos) {
        if (!Objects.equals(corePos, this.corePos)) {
            this.corePos = corePos;
            // Each cache watches a fixed position. Recreate it on the next lookup.
            coreEnergyCache = null;
            coreItemCache = null;
            coreFluidCache = null;
            invalidateCapabilities();
        }
    }

    public void setExtractor(boolean extractor) {
        if (extractor != this.extractor) {
            this.extractor = extractor;
            invalidateCapabilities();
        }
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
