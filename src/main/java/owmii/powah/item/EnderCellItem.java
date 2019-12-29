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
import owmii.lib.util.Const;
import owmii.lib.util.Player;
import owmii.powah.block.endercell.EnderCellBlock;
import owmii.powah.client.screen.IScreens;

import javax.annotation.Nullable;

public class EnderCellItem extends PowahBlockItem {
    private final EnderCellBlock block;

    public EnderCellItem(EnderCellBlock block, Properties properties, @Nullable ItemGroup group) {
        super(block, properties, group);
        this.block = block;
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        if (Player.isFake(playerIn)) return;
        CompoundNBT tag = stack.getOrCreateTag();
        if (!tag.contains(Const.TAG_STORABLE, Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("TotalChannels", getBlock().getChannels());
            nbt.putUniqueId("OwnerId", playerIn.getGameProfile().getId());
            nbt.putString("OwnerName", playerIn.getGameProfile().getName());
            nbt.putInt("ActiveChannel", 0);
            tag.put(Const.TAG_STORABLE, nbt);
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ActionResultType actionresulttype = tryPlace(new BlockItemUseContext(context));
        return actionresulttype != ActionResultType.SUCCESS ? onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType() : actionresulttype;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (Player.isFake(playerIn)) return ActionResult.newResult(ActionResultType.FAIL, stack);
        CompoundNBT tag = stack.getOrCreateTag();
        if (!tag.contains(Const.TAG_STORABLE, Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("TotalChannels", getBlock().getChannels());
            nbt.putUniqueId("OwnerId", playerIn.getGameProfile().getId());
            nbt.putString("OwnerName", playerIn.getGameProfile().getName());
            nbt.putInt("ActiveChannel", 0);
            tag.put(Const.TAG_STORABLE, nbt);
        }
        if (worldIn.isRemote) {
            CompoundNBT nbt = tag.getCompound(Const.TAG_STORABLE);
            IScreens.openEnderNetScreen(stack, nbt);
        }
        return ActionResult.newResult(ActionResultType.SUCCESS, stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            CompoundNBT tag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
            if (tag.contains(Const.TAG_STORABLE, Constants.NBT.TAG_COMPOUND)) {
                CompoundNBT stackTag = tag.getCompound(Const.TAG_STORABLE);
                if (stackTag.contains("ActiveChannel", Constants.NBT.TAG_INT)) {
                    oneTimeInfo(player, stack, new TranslationTextComponent("info.powah.channel", "" + TextFormatting.DARK_AQUA + (stackTag.getInt("ActiveChannel") + 1)).applyTextStyle(TextFormatting.GRAY));
                }
            }
        }
    }

    @Override
    public EnderCellBlock getBlock() {
        return this.block;
    }
}
