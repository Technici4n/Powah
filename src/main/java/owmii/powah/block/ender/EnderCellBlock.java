package owmii.powah.block.ender;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.logistics.inventory.AbstractContainer;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.EnderCellConfig;
import owmii.powah.inventory.EnderCellContainer;

import javax.annotation.Nullable;

public class EnderCellBlock extends AbstractEnergyBlock<Tier, EnderCellConfig, EnderCellBlock> {
    public EnderCellBlock(Properties properties, Tier variant) {
        super(properties, variant);
    }

    @Override
    public EnderCellConfig getConfig() {
        return Configs.ENDER_CELL;
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return super.getBlockItem(properties.maxStackSize(1), group);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnderCellTile(this.variant);
    }

    @Nullable
    @Override
    public AbstractContainer getContainer(int id, PlayerInventory inventory, AbstractTileEntity te, BlockRayTraceResult result) {
        if (te instanceof EnderCellTile) {
            return new EnderCellContainer(id, inventory, (EnderCellTile) te);
        }
        return null;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context);
    }
}
