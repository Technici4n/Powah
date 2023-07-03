package owmii.powah.item;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import owmii.powah.Powah;
import owmii.powah.lib.item.ItemBase;
import owmii.powah.lib.util.Player;
import owmii.powah.lib.util.Stack;

public class BindingCardItem extends ItemBase {
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
                    if (!playerIn.level().isClientSide) {
                        ItemStack stack1 = playerIn.getItemInHand(hand);
                        ItemStack stack2 = new ItemStack(Itms.BINDING_CARD_DIM.get());
                        CompoundTag nbt = Stack.getTagOrEmpty(stack1);
                        if (nbt.hasUUID("bound_player_id")) {
                            CompoundTag nbt1 = stack2.getOrCreateTag();
                            nbt1.putUUID("bound_player_id", nbt.getUUID("bound_player_id"));
                            nbt1.putString("bound_player_name", nbt.getString("bound_player_name"));
                        }
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
    public InteractionResultHolder<ItemStack> use(Level worldIn, net.minecraft.world.entity.player.Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        CompoundTag nbt = stack.getOrCreateTag();
        if (!nbt.hasUUID("bound_player_id")) {
            nbt.putUUID("bound_player_id", playerIn.getUUID());
            nbt.putString("bound_player_name", playerIn.getDisplayName().getString());
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        } else if (!playerIn.getUUID().equals(nbt.getUUID("bound_player_id"))) {
            playerIn.displayClientMessage(
                    Component.translatable("chat.powah.no.binding", nbt.getString("bound_player_name")).withStyle(ChatFormatting.DARK_RED), true);
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, stack);
    }

    public Optional<ServerPlayer> getPlayer(ServerLevel level, ItemStack stack) {
        return Player.get(level, Stack.getTagOrEmpty(stack).getUUID("bound_player_id"));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag nbt = stack.getTag();
        if (nbt == null) {
            tooltip.add(Component.translatable("info.powah.click.to.bind").withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(Component.empty());
        } else if (nbt.hasUUID("bound_player_id")) {
            tooltip.add(Component.translatable("info.lollipop.owner", ChatFormatting.YELLOW + nbt.getString("bound_player_name"))
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.empty());
        }
    }

    public boolean isMultiDim(ItemStack stack) {
        return this.isMultiDim;
    }
}
