package owmii.powah.fabric;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import owmii.powah.EnvHandler;
import owmii.powah.block.Tier;
import owmii.powah.block.cable.CableTile;
import owmii.powah.block.reactor.ReactorPartTile;
import owmii.powah.fabric.block.FabricCableTile;
import owmii.powah.fabric.transfer.EnergyItemWrapper;
import owmii.powah.fabric.transfer.EnergyParticipant;
import owmii.powah.fabric.transfer.InventoryWrapper;
import owmii.powah.fabric.transfer.TankWrapper;
import owmii.powah.item.ItemGroups;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.lib.block.IBlock;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.ITankHolder;
import owmii.powah.lib.item.IEnergyContainingItem;
import owmii.powah.lib.logistics.fluid.Tank;
import owmii.powah.lib.logistics.inventory.Inventory;
import owmii.powah.world.gen.Features;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.DelegatingEnergyStorage;

import java.util.Objects;
import java.util.function.Consumer;

public class FabricEnvHandler implements EnvHandler {
	@Override
	public void setupBlockItems() {
		for (var block : Registry.BLOCK) {
			if (block instanceof IBlock<?,?> iBlock) {
				var blockItem = iBlock.getBlockItem(new Item.Properties(), ItemGroups.MAIN);
				Registry.register(Registry.ITEM, Registry.BLOCK.getKey(block), blockItem);
			}
		}
	}

	@Override
	public void registerWorldgen() {
		EnvHandler.super.registerWorldgen();
		Features.init();
	}

	@Override
	public void scheduleCommonSetup(Runnable runnable) {
		runnable.run();
	}

	@Override
	public void handleReactorInitClient(Consumer<?> consumer) {
		// do nothing, thanks forge
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return stack.getItem().hasCraftingRemainingItem();
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		return new ItemStack(stack.getItem().getCraftingRemainingItem());
	}

	@Override
	public void registerTransfer() {
		EnergyStorage.SIDED.registerFallback((level, pos, state, be, side) -> {
			if (be instanceof ReactorPartTile reactorPart) {
				var core = reactorPart.core();
				if (core.isPresent() && reactorPart.isExtractor()) {
					return EnergyStorage.SIDED.find(level, core.get().getBlockPos(), null, core.get(), side);
				}
			}
			return null;
		});
		FluidStorage.SIDED.registerFallback((level, pos, state, be, side) -> {
			if (be instanceof ReactorPartTile reactorPart) {
				var core = reactorPart.core();
				if (core.isPresent()) {
					return FluidStorage.SIDED.find(level, core.get().getBlockPos(), null, core.get(), side);
				}
			}
			return null;
		});
		ItemStorage.SIDED.registerFallback((level, pos, state, be, side) -> {
			if (be instanceof ReactorPartTile reactorPart) {
				var core = reactorPart.core();
				if (core.isPresent()) {
					return ItemStorage.SIDED.find(level, core.get().getBlockPos(), null, core.get(), side);
				}
			}
			return null;
		});
		EnergyStorage.SIDED.registerFallback((level, pos, state, be, side) -> {
			if (be instanceof AbstractEnergyStorage<?,?> energyStorage) {
				if (be instanceof FabricCableTile cableTile) {
					if (cableTile.isEnergyPresent(side)) {
						return new DelegatingEnergyStorage(EnergyStorage.EMPTY, () -> false) {
							@Override
							public long insert(long maxAmount, TransactionContext transaction) {
								return cableTile.insert(maxAmount, transaction, side);
							}
						};
					}
				} else {
					if (energyStorage.isEnergyPresent(side)) {
						return new EnergyStorage() {
							@Override
							public long insert(long maxAmount, TransactionContext transaction) {
								StoragePreconditions.notNegative(maxAmount);

								EnergyParticipant.get(energyStorage.getEnergy()).updateSnapshots(transaction);
								return energyStorage.receiveEnergy(maxAmount, false, side);
							}

							@Override
							public long extract(long maxAmount, TransactionContext transaction) {
								StoragePreconditions.notNegative(maxAmount);

								EnergyParticipant.get(energyStorage.getEnergy()).updateSnapshots(transaction);
								return energyStorage.extractEnergy(maxAmount, false, side);
							}

							@Override
							public long getAmount() {
								return energyStorage.getEnergy().getEnergyStored();
							}

							@Override
							public long getCapacity() {
								return energyStorage.getEnergy().getCapacity();
							}

							@Override
							public boolean supportsInsertion() {
								return energyStorage.canReceiveEnergy(side);
							}

							@Override
							public boolean supportsExtraction() {
								return energyStorage.canExtractEnergy(side);
							}
						};
					}
				}
			}
			return null;
		});
		ItemStorage.SIDED.registerFallback((level, pos, state, be, side) -> {
			if (be instanceof IInventoryHolder holder) {
				var inv = holder.getInventory();
				if (!inv.isBlank()) {
					return (InventoryWrapper) inv.getPlatformWrapper();
				}
			}
			return null;
		});
		FluidStorage.SIDED.registerFallback((level, pos, state, be, side) -> {
			if (be instanceof ITankHolder holder) {
				var tank = holder.getTank();
				return (TankWrapper) tank.getPlatformWrapper();
			}
			return null;
		});
		EnergyStorage.ITEM.registerFallback((stack, context) -> {
			IEnergyContainingItem.Info info;
			if (stack.getItem() instanceof IEnergyContainingItem eci && (info = eci.getEnergyInfo()) != null) {
				return EnergyItemWrapper.create(context, info.capacity(), info.maxInsert(), info.maxExtract());
			}
			return null;
		});
	}

	@Override
	public InventoryWrapper createInvWrapper(Inventory inventory) {
		return new InventoryWrapper(inventory);
	}

	@Override
	public TankWrapper createTankWrapper(Tank tank) {
		return new TankWrapper(tank);
	}

	@Override
	public boolean interactWithTank(Player player, InteractionHand hand, Tank tank) {
		var handStorage = Objects.requireNonNullElse(
				(player.getAbilities().instabuild
						? ContainerItemContext.withInitial(player.getItemInHand(hand))
						: ContainerItemContext.ofPlayerHand(player, hand))
						.find(FluidStorage.ITEM),
				Storage.<FluidVariant>empty()
		);
		var tankWrapper = createTankWrapper(tank);

		return StorageUtil.move(tankWrapper, handStorage, fv -> true, Long.MAX_VALUE, null) > 0
				|| StorageUtil.move(handStorage, tankWrapper, fv -> true, Long.MAX_VALUE, null) > 0;
	}

	@Override
	public boolean canDischarge(ItemStack stack) {
		try (var tx = Transaction.openNested(Transaction.getCurrentUnsafe())) {
			return Objects.requireNonNullElse(
					ContainerItemContext.withInitial(stack).find(EnergyStorage.ITEM),
					EnergyStorage.EMPTY
			).extract(Long.MAX_VALUE, tx) > 0;
		}
	}

	@Override
	public boolean hasEnergy(Level level, BlockPos pos, Direction side) {
		return EnergyStorage.SIDED.find(level, pos, side) != null;
	}

	@Override
	public long pushEnergy(Level level, BlockPos pos, Direction side, long howMuch) {
		try (var tx = Transaction.openOuter()) {
			long inserted = Objects.requireNonNullElse(
					EnergyStorage.SIDED.find(level, pos, side),
					EnergyStorage.EMPTY
			).insert(howMuch, tx);
			tx.commit();
			return inserted;
		}
	}

	@Override
	public CableTile createCable(BlockPos pos, BlockState state, Tier variant) {
		return new FabricCableTile(pos, state, variant);
	}
}
