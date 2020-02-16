package owmii.powah.block.endercell;

import net.minecraft.block.BlockState;
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
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.block.TileBase;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.energy.Energy;
import owmii.lib.inventory.ContainerBase;
import owmii.lib.item.BlockItemBase;
import owmii.lib.util.Data;
import owmii.lib.util.Stack;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.EnderCellConfig;
import owmii.powah.inventory.EnderCellContainer;
import owmii.powah.inventory.IContainers;
import owmii.powah.item.EnderCellItem;

import javax.annotation.Nullable;
import java.util.List;

public class EnderCellBlock extends AbstractEnergyBlock<Tier> {
    public EnderCellBlock(Properties properties, Tier variant) {
        super(properties, variant);
    }

    @Override
    public BlockItemBase getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return new EnderCellItem(this, properties, group);
    }

    @Override
    public IEnergyConfig<Tier> getEnergyConfig() {
        return Configs.ENDER_CELL;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnderCellTile(this.variant);
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

    @Override
    public void additionalEnergyInfo(ItemStack stack, Energy.Item energy, List<ITextComponent> tooltip) {
        tooltip.add(new TranslationTextComponent("info.lollipop.max.channels", "" + TextFormatting.DARK_GRAY + getTotalChannels()).applyTextStyle(TextFormatting.GRAY));
        tooltip.add(new StringTextComponent(""));
        CompoundNBT nbt = Stack.getTagOrEmpty(stack).getCompound(Data.TAG_TE_STORABLE);
        tooltip.add(new TranslationTextComponent("info.powah.channel", "" + TextFormatting.DARK_AQUA + (nbt.getInt("ActiveChannel") + 1)).applyTextStyle(TextFormatting.DARK_GRAY));
    }

    public int getTotalChannels() {
        return ((EnderCellConfig) getEnergyConfig()).getChannels(this.variant);
    }
}
