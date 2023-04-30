package owmii.powah.block.cable;

import com.google.common.collect.Iterables;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.config.v2.types.CableConfig;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.lib.block.IInventoryHolder;

public abstract class CableTile extends AbstractEnergyStorage<CableConfig, CableBlock> implements IInventoryHolder {

    /**
     * Tag-Name used for synchronizing connected sides to the client.
     */
    private static final String NBT_ENERGY_SIDES = "cs";

    public final EnumSet<Direction> energySides = EnumSet.noneOf(Direction.class);
    @Nullable
    CableNet net = null;
    protected int startIndex = 0;

    public CableTile(BlockPos pos, BlockState state, Tier variant) {
        super(Tiles.CABLE.get(), pos, state, variant);
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        CableNet.addCable(this);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        CableNet.removeCable(this);
    }

    public boolean isActive() {
        if (getLevel() instanceof ServerLevel serverLevel) {
            return serverLevel.getChunkSource().isPositionTicking(ChunkPos.asLong(getBlockPos()));
        }
        return false;
    }

    protected Iterable<CableTile> getCables() {
        if (net == null) {
            CableNet.calculateNetwork(this);
        }
        startIndex %= net.cableList.size();
        return Iterables.concat(net.cableList.subList(startIndex, net.cableList.size()), net.cableList.subList(0, startIndex));
    }

    @Override
    public void readSync(CompoundTag compound) {
        super.readSync(compound);
        readEnergySides(compound);
    }

    @Override
    public CompoundTag writeSync(CompoundTag compound) {
        writeEnergySides(compound);

        return super.writeSync(compound);
    }

    private void readEnergySides(CompoundTag compound) {
        // Read connected sides
        this.energySides.clear();
        var sideBits = compound.getByte(NBT_ENERGY_SIDES);
        for (var side : Direction.values()) {
            if ((sideBits & getSideMask(side)) != 0) {
                this.energySides.add(side);
            }
        }
    }

    private void writeEnergySides(CompoundTag compound) {
        // Write connected sides
        byte sideBits = 0;
        for (var side : this.energySides) {
            sideBits |= getSideMask(side);
        }
        compound.putByte(NBT_ENERGY_SIDES, sideBits);
    }

    private static byte getSideMask(Direction side) {
        return (byte) (1 << side.ordinal());
    }

    @Override
    protected long getEnergyCapacity() {
        return 0;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean keepStorable() {
        return false;
    }
}
