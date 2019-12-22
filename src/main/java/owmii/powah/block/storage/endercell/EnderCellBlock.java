package owmii.powah.block.storage.endercell;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import owmii.lib.util.NBT;
import owmii.powah.block.PowahBlock;
import owmii.powah.inventory.EnderCellContainer;
import owmii.powah.inventory.IContainers;

import javax.annotation.Nullable;
import java.util.List;

public class EnderCellBlock extends PowahBlock {
    private int channels;

    public EnderCellBlock(Properties properties, int maxExtract, int maxReceive, int channels) {
        super(properties, 0, maxExtract, maxReceive);
        this.channels = channels;
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
        if (this == EnderCells.BASIC.get()) {
            return new EnderCellContainer(IContainers.ENDER_CELL_BASIC, id, playerInventory, (EnderCellTile) inv);
        } else if (this == EnderCells.HARDENED.get()) {
            return new EnderCellContainer(IContainers.ENDER_CELL_HARDENED, id, playerInventory, (EnderCellTile) inv);
        } else if (this == EnderCells.BLAZING.get()) {
            return new EnderCellContainer(IContainers.ENDER_CELL_BLAZING, id, playerInventory, (EnderCellTile) inv);
        } else if (this == EnderCells.NIOTIC.get()) {
            return new EnderCellContainer(IContainers.ENDER_CELL_NIOTIC, id, playerInventory, (EnderCellTile) inv);
        } else if (this == EnderCells.SPIRITED.get()) {
            return new EnderCellContainer(IContainers.ENDER_CELL_SPIRITED, id, playerInventory, (EnderCellTile) inv);
        }
        return super.getContainer(id, playerInventory, inv);
    }

    @Override
    protected void postTooltip(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
        CompoundNBT stackTag = tag.getCompound(NBT.TAG_STACK);
        int activeChannel = stackTag.getInt("ActiveChannel");
        String owner = stackTag.getString("OwnerName");
        if (!owner.isEmpty()) {
            tooltip.add(new TranslationTextComponent("info.powah.owner", TextFormatting.DARK_GRAY + owner).applyTextStyle(TextFormatting.GRAY));
        }
        tooltip.add(new TranslationTextComponent("info.powah.chanel", "" + TextFormatting.DARK_AQUA + (activeChannel + 1)).applyTextStyle(TextFormatting.GRAY));
        tooltip.add(new StringTextComponent(""));
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
