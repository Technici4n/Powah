package owmii.powah;

import dev.architectury.platform.Platform;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import owmii.powah.block.Tier;
import owmii.powah.block.cable.CableTile;
import owmii.powah.lib.logistics.fluid.Tank;
import owmii.powah.lib.logistics.inventory.Inventory;
import owmii.powah.world.gen.WorldGen;

import java.util.function.Consumer;

public interface EnvHandler {
	EnvHandler INSTANCE = Util.make(() -> {
		try {
			var klass = Class.forName(Platform.isForge() ? "owmii.powah.forge.ForgeEnvHandler" : "owmii.powah.fabric.FabricEnvHandler");
			return (EnvHandler) klass.getConstructor().newInstance();
		} catch (Exception exception) {
			throw new RuntimeException("Failed to setup env handler", exception);
		}
	});

	// INIT
	void setupBlockItems();
	default void registerWorldgen() {
		WorldGen.init();
	}
	void scheduleCommonSetup(Runnable runnable);

	// WEIRD PLATFORM STUFF
	void handleReactorInitClient(Consumer<?> consumer);

	// MISC HOOKS
	boolean hasContainerItem(ItemStack stack);
	ItemStack getContainerItem(ItemStack stack);

	// TRANSFER
	void registerTransfer();
	// items
	Object createInvWrapper(Inventory inventory);
	// fluids
	Object createTankWrapper(Tank tank);
	boolean interactWithTank(Player player, InteractionHand hand, Tank tank);
	// energy
	boolean canDischarge(ItemStack stack);
	boolean hasEnergy(Level level, BlockPos pos, Direction side);
	long pushEnergy(Level level, BlockPos pos, Direction side, long howMuch);
	CableTile createCable(BlockPos pos, BlockState state, Tier variant);
	// a bit ugly, but I couldn't find a better way
	long chargeItemsInPlayerInv(Player player, long maxPerSlot, long maxTotal);
	long chargeItemsInContainer(Container container, long maxPerSlot, long maxTotal);
	long chargeItemsInInventory(Inventory inv, int slotFrom, int slotTo, long maxPerSlot, long maxTotal);
	long dischargeItemsInInventory(Inventory inv, long maxPerSlot, long maxTotal);
}
