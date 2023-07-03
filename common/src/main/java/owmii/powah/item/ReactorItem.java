package owmii.powah.item;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import owmii.powah.EnvHandler;
import owmii.powah.block.reactor.ReactorBlock;
import owmii.powah.config.v2.types.GeneratorConfig;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.util.Player;

public class ReactorItem extends EnergyBlockItem<GeneratorConfig, ReactorBlock> {
    /**
     * Prevent dupe with mods that check for instanceof BlockItem and place it without going through our {@link #place} override.
     */
    private final ThreadLocal<Boolean> ALLOW_PLACEMENT = ThreadLocal.withInitial(() -> false);

    public ReactorItem(ReactorBlock block, Properties properties, @Nullable ResourceKey<CreativeModeTab> group) {
        super(block, properties, group);
    }

    @SuppressWarnings("unused") // overridden on Forge
    public void initializeClient(Consumer<?> consumer) {
        EnvHandler.INSTANCE.handleReactorInitClient(consumer);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        if (!context.canPlace())
            return InteractionResult.FAIL;
        net.minecraft.world.entity.player.Player player = context.getPlayer();
        if (player == null || Player.isFake(player))
            return InteractionResult.FAIL;
        ItemStack stack = context.getItemInHand();
        if (player.getInventory().countItem(stack.getItem()) < 36 && !player.isCreative()) {
            player.displayClientMessage(Component
                    .translatable("chat.powah.not.enough.blocks",
                            "" + ChatFormatting.YELLOW + (36 - player.getInventory().countItem(stack.getItem())) + ChatFormatting.RED)
                    .withStyle(ChatFormatting.RED), true);
            return InteractionResult.FAIL;
        }
        BlockPos pos = context.getClickedPos();
        List<BlockPos> list = BlockPos.betweenClosedStream(pos.offset(-1, 0, -1), pos.offset(1, 3, 1))
                .map(BlockPos::immutable)
                .collect(Collectors.toList());

        for (BlockPos blockPos : list) {
            if (!context.getLevel().getBlockState(blockPos).canBeReplaced())
                return InteractionResult.FAIL;
        }
        List<LivingEntity> entities = context.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(1.0D, 3.0D, 1.0D));
        if (!entities.isEmpty())
            return InteractionResult.FAIL;

        if (!player.isCreative()) {
            if (stack.getCount() < 36) {
                int held = stack.getCount();
                stack.setCount(0);
                final int taken = ContainerHelper.clearOrCountMatchingItems(player.getInventory(), s -> s.is(this), 36 - held, false);
                if (taken + held != 36) {
                    throw new IllegalStateException();
                }
                stack.setCount(1);
            } else {
                stack.shrink(35);
            }
        }

        ALLOW_PLACEMENT.set(true);
        try {
            return super.place(context);
        } finally {
            ALLOW_PLACEMENT.set(false);
        }
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        return ALLOW_PLACEMENT.get() && super.placeBlock(context, state);
    }
}
