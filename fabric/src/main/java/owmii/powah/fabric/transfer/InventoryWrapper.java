package owmii.powah.fabric.transfer;

import com.google.common.primitives.Ints;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.world.item.ItemStack;
import owmii.powah.lib.logistics.inventory.Inventory;

import java.util.ArrayList;

public class InventoryWrapper extends CombinedStorage<ItemVariant, InventoryWrapper.Slot> {
	private final Inventory inventory;

	public InventoryWrapper(Inventory inventory) {
		super(new ArrayList<>());
		this.inventory = inventory;

		for (int i = 0; i < inventory.getSlots(); ++i) {
			parts.add(new Slot(i));
		}
	}

	protected class Slot extends SnapshotParticipant<ItemStack> implements SingleSlotStorage<ItemVariant> {
		private final int slot;

		private Slot(int slot) {
			this.slot = slot;
		}

		@Override
		public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
			StoragePreconditions.notBlankNotNegative(resource, maxAmount);
			int amount = Ints.saturatedCast(maxAmount);
			var toInsert = resource.toStack(amount);

			if (inventory.insertItem(slot, toInsert, true).getCount() < amount) {
				updateSnapshots(transaction);
				inventory.setSendUpdates(false);
				var inserted = amount - inventory.insertItem(slot, toInsert, false).getCount();
				inventory.setSendUpdates(true);

				return inserted;
			}

			return 0;
		}

		@Override
		public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
			StoragePreconditions.notBlankNotNegative(resource, maxAmount);
			int amount = Ints.saturatedCast(maxAmount);

			if (!inventory.extractItem(slot, amount, true).isEmpty()) {
				updateSnapshots(transaction);
				inventory.setSendUpdates(false);
				var extracted = inventory.extractItem(slot, amount, false);
				inventory.setSendUpdates(true);

				return extracted.getCount();
			}

			return 0;
		}

		@Override
		public boolean isResourceBlank() {
			return inventory.getStackInSlot(slot).isEmpty();
		}

		@Override
		public ItemVariant getResource() {
			return ItemVariant.of(inventory.getStackInSlot(slot));
		}

		@Override
		public long getAmount() {
			return inventory.getStackInSlot(slot).getCount();
		}

		@Override
		public long getCapacity() {
			return inventory.getStackInSlot(slot).getMaxStackSize();
		}

		@Override
		protected ItemStack createSnapshot() {
			return inventory.getStackInSlot(slot).copy();
		}

		@Override
		protected void readSnapshot(ItemStack snapshot) {
			inventory.setSendUpdates(false);
			inventory.setStackInSlot(slot, snapshot);
			inventory.setSendUpdates(false);
		}

		@Override
		protected void onFinalCommit() {
			inventory.onContentsChanged(slot);
		}
	}
}
