package owmii.powah.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.SharedConstants;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import owmii.powah.EnvHandler;
import owmii.powah.Powah;
import owmii.powah.lib.block.AbstractBlock;
import owmii.powah.lib.item.ItemBase;

public class PowahFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		// This prevents the annoying "No data fixer registered for charged_snowball"
		var checkDataFixer = SharedConstants.CHECK_DATA_FIXER_SCHEMA;
		SharedConstants.CHECK_DATA_FIXER_SCHEMA = false;

		Powah.init();

		SharedConstants.CHECK_DATA_FIXER_SCHEMA = checkDataFixer;

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			InteractionResult removeWithWrench = removeWithWrench(player, world, hand, hitResult);
			if (removeWithWrench != null) {
				return removeWithWrench;
			}

			var stack = player.getItemInHand(hand);
			if (stack.getItem() instanceof ItemBase itemBase) {
				return itemBase.onItemUseFirst(stack, player.level, hitResult.getBlockPos(), player, hand, hitResult.getDirection(), hitResult.getLocation());
			}
			return InteractionResult.PASS;
		});
	}

	private static @Nullable InteractionResult removeWithWrench(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
		if (player.isSpectator() || !player.isShiftKeyDown() || !world.mayInteract(player, hitResult.getBlockPos())
				|| !EnvHandler.INSTANCE.isWrench(player.getItemInHand(hand))) {
			return null;
		}
		var pos = hitResult.getBlockPos();
		var state = world.getBlockState(pos);
		if (state.getBlock() instanceof AbstractBlock<?, ?>) {
			var entity = world.getBlockEntity(pos);
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			if (!player.isCreative()) {
				Block.dropResources(state, world, pos, entity);
			}
			// Play a cool sound
			var group = state.getSoundType();
			world.playSound(player, pos, group.getBreakSound(), SoundSource.BLOCKS, (group.getVolume() + 1.0F) / 2.0F,
					group.getPitch() * 0.8F);
			return InteractionResult.sidedSuccess(world.isClientSide);
		}
		return null;
	}
}
