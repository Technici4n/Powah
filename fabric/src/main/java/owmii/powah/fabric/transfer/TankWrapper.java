package owmii.powah.fabric.transfer;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.fabric.FluidStackHooksFabric;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import owmii.powah.lib.logistics.fluid.Tank;

public class TankWrapper extends SnapshotParticipant<FluidStack> implements SingleSlotStorage<FluidVariant> {
	private final Tank tank;

	public TankWrapper(Tank tank) {
		this.tank = tank;
	}

	@Override
	public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notBlankNotNegative(resource, maxAmount);

		var stack = FluidStackHooksFabric.fromFabric(resource, maxAmount);
		if (tank.fill(stack, true) > 0) {
			updateSnapshots(transaction);
			tank.setSendUpdates(false);
			var filled = tank.fill(stack, false);
			tank.setSendUpdates(true);

			return filled;
		}

		return 0;
	}

	@Override
	public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
		StoragePreconditions.notBlankNotNegative(resource, maxAmount);

		var stack = FluidStackHooksFabric.fromFabric(resource, maxAmount);
		if (!tank.drain(stack, true).isEmpty()) {
			updateSnapshots(transaction);
			tank.setSendUpdates(false);
			var drained = tank.drain(stack, false).getAmount();
			tank.setSendUpdates(true);

			return drained;
		}

		return 0;
	}

	@Override
	public boolean isResourceBlank() {
		return tank.getFluid().isEmpty();
	}

	@Override
	public FluidVariant getResource() {
		return FluidStackHooksFabric.toFabric(tank.getFluid());
	}

	@Override
	public long getAmount() {
		return tank.getFluidAmount();
	}

	@Override
	public long getCapacity() {
		return tank.getCapacity();
	}

	@Override
	protected FluidStack createSnapshot() {
		return tank.getFluid().copy();
	}

	@Override
	protected void readSnapshot(FluidStack snapshot) {
		tank.setSendUpdates(false);
		tank.setFluid(snapshot);
		tank.setSendUpdates(true);
	}

	@Override
	protected void onFinalCommit() {
		tank.onContentsChanged();
	}
}
