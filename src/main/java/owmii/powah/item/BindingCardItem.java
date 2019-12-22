package owmii.powah.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import owmii.lib.item.ItemBase;
import owmii.lib.util.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BindingCardItem extends ItemBase {
    public BindingCardItem(Properties properties) {
        super(properties);
        addPropertyOverride(new ResourceLocation("bound"), (stack, world, livingEntity) -> {
            float f = 0.0F;
            CompoundNBT nbt = stack.getTag();
            if (nbt != null) {

                if (nbt.hasUniqueId("BindedPlayerId")) {
                    f = 1.0F;
                }
            }
            return f;
        });
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.hasUniqueId("BindedPlayerId")) {
            nbt.putUniqueId("BindedPlayerId", playerIn.getUniqueID());
            nbt.putString("BindedPlayerName", playerIn.getDisplayName().getString());
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        } else if (!playerIn.getUniqueID().equals(nbt.getUniqueId("BindedPlayerId"))) {
            playerIn.sendStatusMessage(new TranslationTextComponent("chat.powah.no.binding", nbt.getString("BindedPlayerName")).applyTextStyle(TextFormatting.DARK_RED), true);
            return new ActionResult<>(ActionResultType.FAIL, stack);
        }
        return new ActionResult<>(ActionResultType.PASS, stack);
    }

    public Optional<ServerPlayerEntity> getPlayer(ItemStack stack) {
        if (stack.getTag() == null) return Optional.empty();
        return Player.get(stack.getTag().getUniqueId("BindedPlayerId"));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = stack.getTag();
        if (nbt == null) {
            tooltip.add(new TranslationTextComponent("info.powah.click.to.bind").applyTextStyle(TextFormatting.GRAY));
        } else if (nbt.hasUniqueId("BindedPlayerId")) {
            tooltip.add(new TranslationTextComponent("info.powah.owner", nbt.getString("BindedPlayerName")).applyTextStyle(TextFormatting.GRAY));
            tooltip.add(new StringTextComponent(""));
        }
    }
}
