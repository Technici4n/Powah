package owmii.powah.item;

import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import owmii.powah.Powah;
import owmii.powah.components.BoundPlayer;
import owmii.powah.components.PowahComponents;
import owmii.powah.lib.item.PowahBaseItem;
import owmii.powah.util.Player;

public class BindingCardItem extends PowahBaseItem {
    private final boolean isMultiDim;

    public BindingCardItem(Properties properties, boolean isMultiDim) {
        super(properties);
        this.isMultiDim = isMultiDim;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, net.minecraft.world.entity.player.Player playerIn, LivingEntity target,
            InteractionHand hand) {
        if (Powah.config().general.dimensional_binding_card) {
            if (this == Itms.BINDING_CARD.get()) {
                if (target.getClass() == EnderMan.class || target.getClass() == Endermite.class) {
                    if (!playerIn.level().isClientSide()) {
                        ItemStack stack1 = playerIn.getItemInHand(hand);
                        ItemStack stack2 = new ItemStack(Itms.BINDING_CARD_DIM.get());
                        stack2.copyFrom(stack1, PowahComponents.BOUND_PLAYER);
                        playerIn.setItemInHand(hand, stack2);
                        target.playSound(SoundEvents.ENDERMAN_DEATH, 0.5F, 1.0F);
                        target.remove(Entity.RemovalReason.KILLED);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.interactLivingEntity(stack, playerIn, target, hand);
    }

    @Override
    public InteractionResult use(Level worldIn, net.minecraft.world.entity.player.Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        var boundPlayer = stack.get(PowahComponents.BOUND_PLAYER);
        if (boundPlayer == null) {
            stack.set(PowahComponents.BOUND_PLAYER, new BoundPlayer(
                    playerIn.getUUID(),
                    playerIn.getDisplayName().getString()));
            return InteractionResult.SUCCESS.heldItemTransformedTo(stack);
        } else if (!playerIn.getUUID().equals(boundPlayer.gameProfileId())) {
            playerIn.sendOverlayMessage(
                    Component.translatable("chat.powah.no.binding", boundPlayer.name()).withStyle(ChatFormatting.DARK_RED));
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }

    public Optional<ServerPlayer> getPlayer(ServerLevel level, ItemStack stack) {
        var boundPlayer = stack.get(PowahComponents.BOUND_PLAYER);
        if (boundPlayer == null) {
            return Optional.empty();
        }
        return Player.get(level, boundPlayer.gameProfileId());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder,
            TooltipFlag tooltipFlag) {
        var boundPlayer = itemStack.get(PowahComponents.BOUND_PLAYER);
        if (boundPlayer == null) {
            builder.accept(Component.translatable("info.powah.click.to.bind").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            builder.accept(Component.translatable("info.lollipop.owner", ChatFormatting.YELLOW + boundPlayer.name())
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    public boolean isMultiDim(ItemStack stack) {
        return this.isMultiDim;
    }
}
