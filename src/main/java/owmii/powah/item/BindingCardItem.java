package owmii.powah.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
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
        addPropertyOverride(new ResourceLocation("bound"), (stack, world, livingEntity) -> {
            float f = 0.0F;
            CompoundNBT nbt = stack.getTag();
            if (nbt != null) {

                if (nbt.hasUniqueId("BoundPlayerId")) {
                    f = 1.0F;
                }
            }
            return f;
        });
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (Configs.GENERAL.binding_card_dim.get()) {
            if (this == IItems.BINDING_CARD) {
                if (target.getClass() == EndermanEntity.class || target.getClass() == EndermiteEntity.class) {
                    if (!playerIn.world.isRemote) {
                        ItemStack stack1 = playerIn.getHeldItem(hand);
                        ItemHandlerHelper.giveItemToPlayer(playerIn, new ItemStack(IItems.BINDING_CARD_DIM));
                        target.playSound(SoundEvents.ENTITY_ENDERMAN_DEATH, 0.5F, 1.0F);
                        target.remove();
                        if (!playerIn.isCreative()) {
                            stack1.shrink(1);
                        }
                    }
                    return true;
                }
            }
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.hasUniqueId("BoundPlayerId")) {
            nbt.putUniqueId("BoundPlayerId", playerIn.getUniqueID());
            nbt.putString("BoundPlayerName", playerIn.getDisplayName().getString());
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        } else if (!playerIn.getUniqueID().equals(nbt.getUniqueId("BoundPlayerId"))) {
            playerIn.sendStatusMessage(new TranslationTextComponent("chat.powah.no.binding", nbt.getString("BoundPlayerName")).applyTextStyle(TextFormatting.DARK_RED), true);
            return new ActionResult<>(ActionResultType.FAIL, stack);
        }
        return new ActionResult<>(ActionResultType.PASS, stack);
    }

    public Optional<ServerPlayerEntity> getPlayer(ItemStack stack) {
        return Player.get(Stack.getTagOrEmpty(stack).getUniqueId("BoundPlayerId"));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = stack.getTag();
        if (nbt == null) {
            tooltip.add(new TranslationTextComponent("info.powah.click.to.bind").applyTextStyle(TextFormatting.DARK_GRAY));
            tooltip.add(new StringTextComponent(""));
        } else if (nbt.hasUniqueId("BoundPlayerId")) {
            tooltip.add(new TranslationTextComponent("info.lollipop.owner", TextFormatting.YELLOW + nbt.getString("BoundPlayerName")).applyTextStyle(TextFormatting.GRAY));
            tooltip.add(new StringTextComponent(""));
        }
    }

    public boolean isMultiDim(ItemStack stack) {
        return this.isMultiDim;
    }
}
