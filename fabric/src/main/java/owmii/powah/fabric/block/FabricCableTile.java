package owmii.powah.fabric.block;

import java.util.Objects;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import owmii.powah.block.Tier;
import owmii.powah.block.cable.CableTile;
import team.reborn.energy.api.EnergyStorage;

public class FabricCableTile extends CableTile {
    private int lastBump = 0;

    public FabricCableTile(BlockPos pos, BlockState state, Tier variant) {
        super(pos, state, variant);
    }

    public long insert(long maxReceive, TransactionContext transaction, @Nullable Direction direction) {
        if (this.level == null || isRemote() || direction == null || !checkRedstone() || !canReceiveEnergy(direction))
            return 0;
        long received = 0;
        var cables = getCables();

        var insertionGuard = this.netInsertionGuard;
        if (insertionGuard.isTrue())
            return 0;
        insertionGuard.setTrue();

        try {
            int tickCount = ((ServerLevel) getLevel()).getServer().getTickCount();
            if (lastBump != tickCount) {
                lastBump = tickCount;
                startIndex++; // round robin!
            }

            for (var cable : cables) {
                long amount = maxReceive - received;
                if (amount <= 0)
                    break;
                if (!cable.energySides.isEmpty() && cable.isActive()) {
                    received += ((FabricCableTile) cable).pushEnergy(amount, transaction, direction, this);
                }
            }

            return received;
        } finally {
            insertionGuard.setFalse();
        }
    }

    private long pushEnergy(long maxReceive, TransactionContext transaction, @Nullable Direction direction, CableTile cable) {
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
            received += receive(level, pos, side.getOpposite(), amount, transaction);
        }
        return received;
    }

    private long receive(Level level, BlockPos pos, Direction side, long amount, TransactionContext transaction) {
        return Objects.requireNonNullElse(
                EnergyStorage.SIDED.find(level, pos, side),
                EnergyStorage.EMPTY).insert(amount, transaction);
    }
}
