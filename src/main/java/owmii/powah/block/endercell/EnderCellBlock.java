package owmii.powah.block.endercell;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import owmii.lib.block.TileBase;
import owmii.lib.inventory.ContainerBase;
import owmii.lib.item.BlockItemBase;
import owmii.lib.util.Data;
import owmii.powah.block.PowahBlock;
import owmii.powah.inventory.EnderCellContainer;
import owmii.powah.inventory.IContainers;
import owmii.powah.item.EnderCellItem;

import javax.annotation.Nullable;
import java.util.List;

public class EnderCellBlock extends PowahBlock implements IWaterLoggable {
    protected int channels;

    public EnderCellBlock(Properties properties, int maxExtract, int maxReceive, int channels) {
        super(properties, 0, maxExtract, maxReceive);
        this.channels = channels;
        setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, false));
    }

    @Override
    public BlockItemBase getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return new EnderCellItem(this, properties, group);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnderCellTile(this.maxReceive, this.maxExtract, this.channels);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof EnderCellTile) {
            EnderCellTile tile = (EnderCellTile) tileentity;
            if (tile.getOwner() == null && placer instanceof ServerPlayerEntity) {
                tile.setOwner(((ServerPlayerEntity) placer).getGameProfile());
            }
        }
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase inv) {
        if (inv instanceof EnderCellTile) {
            return new EnderCellContainer(IContainers.ENDER_CELL, id, playerInventory, (EnderCellTile) inv);
        }
        return null;
    }

    @Override
    protected void postTooltip(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.remove(new StringTextComponent(""));
        tooltip.add(new TranslationTextComponent("info.powah.max.channels", "" + TextFormatting.DARK_GRAY + this.channels).applyTextStyle(TextFormatting.GRAY));
        tooltip.add(new StringTextComponent(""));
        CompoundNBT tag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
        CompoundNBT stackTag = tag.getCompound(Data.TAG_STORABLE);
        int activeChannel = stackTag.getInt("ActiveChannel");
        String owner = stackTag.getString("OwnerName");
        if (!owner.isEmpty()) {
            tooltip.add(new TranslationTextComponent("info.powah.owner", TextFormatting.DARK_GRAY + owner).applyTextStyle(TextFormatting.GRAY));
            tooltip.add(new TranslationTextComponent("info.powah.channel", "" + TextFormatting.DARK_AQUA + (activeChannel + 1)).applyTextStyle(TextFormatting.GRAY));
            tooltip.add(new StringTextComponent(""));
        }
    }

    @Override
    public int stackSize() {
        return 64;
    }

    public int getChannels() {
        return this.channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }
}
