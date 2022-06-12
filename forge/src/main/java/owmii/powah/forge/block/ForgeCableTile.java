package owmii.powah.forge.block;

import com.google.common.primitives.Ints;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.CapabilityEnergy;
import owmii.powah.block.Tier;
import owmii.powah.block.cable.CableTile;

import javax.annotation.Nullable;

public class ForgeCableTile extends CableTile {
	public ForgeCableTile(BlockPos pos, BlockState state, Tier variant) {
		super(pos, state, variant);
	}

	@Override
	public long receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction direction) {
		if (this.level == null || isRemote() || direction == null || !checkRedstone() || !canReceiveEnergy(direction))
			return 0;
		long received = 0;
		received += pushEnergy(this.level, maxReceive, simulate, direction, this);
		for (BlockPos cablePos : this.proxyMap.get(direction).cables()) {
			long amount = maxReceive - received;
			if (amount <= 0) break;
			BlockEntity cableTile = this.level.getBlockEntity(cablePos);
			if (cableTile instanceof ForgeCableTile cable) {
				received += cable.pushEnergy(this.level, amount, simulate, direction, this);
			}
		}
		return received;
	}

	private long pushEnergy(Level world, long maxReceive, boolean simulate, @Nullable Direction direction, CableTile cable) {
		long received = 0;
		for (Direction side : this.energySides) {
			long amount = Math.min(maxReceive - received, this.energy.getMaxExtract());
			if (amount <= 0) break;
			if (cable.equals(this) && side.equals(direction) || !canExtractEnergy(side)) continue;
			BlockPos pos = this.worldPosition.relative(side);
			if (direction != null && cable.getBlockPos().relative(direction).equals(pos)) continue;
			BlockEntity tile = world.getBlockEntity(pos);
			received += receive(tile, side, amount, simulate);
		}
		return received;
	}

	private long receive(BlockEntity tile, Direction side, long amount, boolean simulate) {
		var energy = tile != null ? tile.getCapability(CapabilityEnergy.ENERGY, side).orElse(null) : null;
		return energy != null ? energy.receiveEnergy(Ints.saturatedCast(amount), simulate) : 0;
	}
}
