package owmii.powah.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import owmii.lib.item.ItemBase;
import owmii.lib.util.Player;
import owmii.lib.util.Stack;
import owmii.powah.config.Configs;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BindingCardItem extends ItemBase {
    private final boolean isMultiDim;

    public BindingCardItem(Properties properties, boolean isMultiDim) {
        super(properties);
        this.isMultiDim = isMultiDim;
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (Configs.GENERAL.binding_card_dim.get()) {
            if (this == IItems.BINDING_CARD) {
                if (target.getClass() == EndermanEntity.class || target.getClass() == EndermiteEntity.class) {
                    if (!playerIn.world.isRemote) {
                        ItemStack stack1 = playerIn.getHeldItem(hand);
                        ItemStack stack2 = new ItemStack(IItems.BINDING_CARD_DIM);
                        CompoundNBT nbt = Stack.getTagOrEmpty(stack1);
                        if (nbt.hasUniqueId("bound_player_id")) {
                            CompoundNBT nbt1 = stack2.getOrCreateTag();
                            nbt1.putUniqueId("bound_player_id", nbt.getUniqueId("bound_player_id"));
                            nbt1.putString("bound_player_name", nbt.getString("bound_player_name"));
                        }
                        playerIn.setHeldItem(hand, stack2);
                        target.playSound(SoundEvents.ENTITY_ENDERMAN_DEATH, 0.5F, 1.0F);
                        target.remove();
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.hasUniqueId("bound_player_id")) {
            nbt.putUniqueId("bound_player_id", playerIn.getUniqueID());
            nbt.putString("bound_player_name", playerIn.getDisplayName().getString());
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        } else if (!playerIn.getUniqueID().equals(nbt.getUniqueId("bound_player_id"))) {
            playerIn.sendStatusMessage(new TranslationTextComponent("chat.powah.no.binding", nbt.getString("bound_player_name")).func_240701_a_(TextFormatting.DARK_RED), true);
            return new ActionResult<>(ActionResultType.FAIL, stack);
        }
        return new ActionResult<>(ActionResultType.PASS, stack);
    }

    public Optional<ServerPlayerEntity> getPlayer(ItemStack stack) {
        return Player.get(Stack.getTagOrEmpty(stack).getUniqueId("bound_player_id"));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = stack.getTag();
        if (nbt == null) {
            tooltip.add(new TranslationTextComponent("info.powah.click.to.bind").func_240701_a_(TextFormatting.DARK_GRAY));
            tooltip.add(new StringTextComponent(""));
        } else if (nbt.hasUniqueId("bound_player_id")) {
            tooltip.add(new TranslationTextComponent("info.lollipop.owner", TextFormatting.YELLOW + nbt.getString("bound_player_name")).func_240699_a_(TextFormatting.GRAY));
            tooltip.add(new StringTextComponent(""));
        }
    }

    public boolean isMultiDim(ItemStack stack) {
        return this.isMultiDim;
    }
}
