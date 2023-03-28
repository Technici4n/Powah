package owmii.powah.fabric;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
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
import team.reborn.energy.api.EnergyStorageUtil;
import team.reborn.energy.api.base.DelegatingEnergyStorage;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
	public TagKey<Biome> getOverworldBiomeTag() {
		return ConventionalBiomeTags.IN_OVERWORLD;
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
		return EnergyStorageUtil.isEnergyStorage(stack);
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

	@Override
	public long chargeItemsInPlayerInv(Player player, long maxPerSlot, long maxTotal, Predicate<ItemStack> allowStack) {
		final List<SingleSlotStorage<ItemVariant>> list = new ArrayList<>(InventoryStorage.of(player.getInventory(), null).getSlots());
		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			final TrinketComponent trinketComponent = TrinketsApi.getTrinketComponent(player).orElse(null);
			if (trinketComponent != null) {
				trinketComponent.getInventory().forEach(($, map) -> map.forEach(($$, container) -> {
					list.addAll(InventoryStorage.of(container, null).getSlots());
				}));
			}
		}
		return transferSlotList(EnergyStorage::insert, list, maxPerSlot, maxTotal, allowStack);
	}

	@Override
	public long chargeItemsInContainer(Container container, long maxPerSlot, long maxTotal) {
		return chargeItemsInContainer(container, maxPerSlot, maxTotal, s -> true);
	}

	public long chargeItemsInContainer(Container container, long maxPerSlot, long maxTotal, Predicate<ItemStack> allowStack) {
		return transferSlotList(EnergyStorage::insert, InventoryStorage.of(container, null).getSlots(), maxPerSlot, maxTotal, allowStack);
	}

	@Override
	public long chargeItemsInInventory(Inventory inv, int slotFrom, int slotTo, long maxPerSlot, long maxTotal) {
		return transferSlotList(EnergyStorage::insert, createInvWrapper(inv).parts.subList(slotFrom, slotTo), maxPerSlot, maxTotal, s -> true);
	}

	@Override
	public long dischargeItemsInInventory(Inventory inv, long maxPerSlot, long maxTotal) {
		return transferSlotList(EnergyStorage::extract, createInvWrapper(inv).parts, maxPerSlot, maxTotal, s -> true);
	}

    private long transferSlotList(EnergyTransferOperation op, Iterable<? extends SingleSlotStorage<ItemVariant>> slots, long maxPerSlot, long maxTotal, Predicate<ItemStack> allowStack) {
		long charged = 0;
		try (var transaction = Transaction.openOuter()) {
			for (var slot : slots) {
				if (!allowStack.test(slot.getResource().toStack())) continue;

				var storage = ContainerItemContext.ofSingleSlot(slot).find(EnergyStorage.ITEM);
				if (storage != null) {
					charged += op.perform(storage, Math.min(maxPerSlot, maxTotal - charged), transaction);
				}
			}
			transaction.commit();
		}
		return charged;
	}

	private interface EnergyTransferOperation {
		long perform(EnergyStorage storage, long maxAmount, TransactionContext transaction);
	}
}
