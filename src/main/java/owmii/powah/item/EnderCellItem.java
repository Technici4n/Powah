package owmii.powah.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.util.Data;
import owmii.lib.util.Player;
import owmii.lib.util.Stack;
import owmii.powah.block.Tier;
import owmii.powah.block.endercell.EnderCellBlock;
import owmii.powah.client.screen.Screens;
import owmii.powah.config.EnderCellConfig;

import javax.annotation.Nullable;

public class EnderCellItem extends EnergyBlockItem<Tier, EnderCellBlock> {
    public EnderCellItem(EnderCellBlock block, Properties properties, @Nullable ItemGroup group) {
        super(block, properties, group);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ActionResultType actionresulttype = tryPlace(new BlockItemUseContext(context));
        if (context.getPlayer() == null) return ActionResultType.FAIL;
        return actionresulttype != ActionResultType.SUCCESS ? onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType() : actionresulttype;
    }

    @Override
    public void onCreated(ItemStack stack, World world, PlayerEntity player) {
        if (Player.isFake(player)) return;
        setEnderNBT(stack, player);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
        if (!Player.isFake(player)) {
            ItemStack stack = player.getHeldItem(hand);
            setEnderNBT(stack, player);
            if (worldIn.isRemote) {
                Screens.openEnderNetScreen(stack, ((EnderCellConfig) getEnergyConfig()).getChannels(getVariant()));
            }
        }
        return super.onItemRightClick(worldIn, player, hand);
    }

    private void setEnderNBT(ItemStack stack, PlayerEntity player) {
        CompoundNBT tag = stack.getOrCreateTag();
        if (!tag.contains(Data.TAG_TE_STORABLE, Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putUniqueId("OwnerId", player.getGameProfile().getId());
            nbt.putString("OwnerName", player.getGameProfile().getName());
            nbt.putInt("ActiveChannel", 0);
            tag.put(Data.TAG_TE_STORABLE, nbt);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            CompoundNBT tag = Stack.getTagOrEmpty(stack);
            if (tag.contains(Data.TAG_TE_STORABLE, Constants.NBT.TAG_COMPOUND)) {
                CompoundNBT stackTag = tag.getCompound(Data.TAG_TE_STORABLE);
                if (stackTag.contains("ActiveChannel", Constants.NBT.TAG_INT)) {
                    oneTimeInfo(player, stack, new TranslationTextComponent("info.powah.channel", "" + TextFormatting.DARK_AQUA + (stackTag.getInt("ActiveChannel") + 1)).applyTextStyle(TextFormatting.GRAY));
                }
            }
        }
    }
}
