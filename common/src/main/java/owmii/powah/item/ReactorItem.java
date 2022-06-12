package owmii.powah.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.AABB;
import owmii.powah.EnvHandler;
import owmii.powah.config.v2.types.GeneratorConfig;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.util.Player;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.block.reactor.ReactorBlock;
import owmii.powah.client.render.tile.ReactorItemRenderer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ReactorItem extends EnergyBlockItem<GeneratorConfig, ReactorBlock> {
    public ReactorItem(ReactorBlock block, Properties properties, @Nullable CreativeModeTab group) {
        super(block, properties, group);
    }

    @SuppressWarnings("unused") // overridden on Forge
    public void initializeClient(Consumer<?> consumer) {
        EnvHandler.INSTANCE.handleReactorInitClient(consumer);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        if (!context.canPlace()) return InteractionResult.FAIL;
        net.minecraft.world.entity.player.Player player = context.getPlayer();
        if (player == null || Player.isFake(player)) return InteractionResult.FAIL;
        ItemStack stack = context.getItemInHand();
        if (stack.getCount() < 36 && !player.isCreative()) {
            player.displayClientMessage(new TranslatableComponent("chat.powah.not.enough.blocks", "" + ChatFormatting.YELLOW + (36 - stack.getCount()) + ChatFormatting.RED).withStyle(ChatFormatting.RED), true);
            return InteractionResult.FAIL;
        }
        BlockPos pos = context.getClickedPos();
        List<BlockPos> list = BlockPos.betweenClosedStream(pos.offset(-1, 0, -1), pos.offset(1, 3, 1))
                .map(BlockPos::immutable)
                .collect(Collectors.toList());

        for (BlockPos blockPos : list) {
            if (!context.getLevel().getBlockState(blockPos).getMaterial().isReplaceable()) return InteractionResult.FAIL;
        }
        List<LivingEntity> entities = context.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(1.0D, 3.0D, 1.0D));
        if (!entities.isEmpty()) return InteractionResult.FAIL;

        stack.shrink(35);
        return super.place(context);
    }
}
