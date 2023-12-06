package owmii.powah.block;

import com.google.common.primitives.Ints;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;
import owmii.powah.block.cable.CableTile;

public class ForgeCableTile extends CableTile {
    public ForgeCableTile(BlockPos pos, BlockState state, Tier variant) {
        super(pos, state, variant);
    }

    @Override
    public long receiveEnergy(long maxReceive, boolean simulate, @Nullable Direction direction) {
        if (this.level == null || isRemote() || direction == null || !checkRedstone() || !canReceiveEnergy(direction))
            return 0;
        long received = 0;
        var cables = getCables();

        var insertionGuard = this.netInsertionGuard;
        if (insertionGuard.isTrue())
            return 0;
        insertionGuard.setTrue();

        try {
            if (!simulate) {
                startIndex++; // round robin!
            }

            for (var cable : cables) {
                long amount = maxReceive - received;
                if (amount <= 0)
                    break;
                if (!cable.energySides.isEmpty() && cable.isActive()) {
                    received += ((ForgeCableTile) cable).pushEnergy(amount, simulate, direction, this);
                }
            }

            return received;
        } finally {
            insertionGuard.setFalse();
        }
    }

    private long pushEnergy(long maxReceive, boolean simulate, @Nullable Direction direction, CableTile cable) {
        if (!(getLevel() instanceof ServerLevel serverLevel))
            throw new RuntimeException("Expected server level");

        long received = 0;
        for (int i = 0; i < 6; ++i) {
            // Shift by tick count to ensure that it distributes evenly on average
            Direction side = Direction.from3DDataValue((i + serverLevel.getServer().getTickCount()) % 6);
            if (!this.energySides.contains(side))
                continue;

            long amount = Math.min(maxReceive - received, this.energy.getMaxExtract());
            if (amount <= 0)
                break;
            if (cable.equals(this) && side.equals(direction) || !canExtractEnergy(side))
                continue;
            BlockPos pos = this.worldPosition.relative(side);
            if (direction != null && cable.getBlockPos().relative(direction).equals(pos))
                continue;
            received += receive(level, pos, side.getOpposite(), amount, simulate);
        }
        return received;
    }

    private long receive(Level level, BlockPos pos, Direction side, long amount, boolean simulate) {
        var tile = level.getBlockEntity(pos);
        var energy = tile != null ? tile.getCapability(Capabilities.ENERGY, side).orElse(null) : null;
        return energy != null ? energy.receiveEnergy(Ints.saturatedCast(amount), simulate) : 0;
    }
}
