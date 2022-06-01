package owmii.powah.block.ender;

import net.minecraft.core.BlockPos;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.logistics.inventory.AbstractContainer;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.EnderCellConfig;
import owmii.powah.inventory.EnderCellContainer;

import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class EnderCellBlock extends AbstractEnergyBlock<Tier, EnderCellConfig, EnderCellBlock> {
    public EnderCellBlock(Properties properties, Tier variant) {
        super(properties, variant);
    }

    @Override
    public EnderCellConfig getConfig() {
        return Configs.ENDER_CELL;
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable CreativeModeTab group) {
        return super.getBlockItem(properties.stacksTo(1), group);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnderCellTile(pos, state, this.variant);
    }

    @Nullable
    @Override
    public AbstractContainer getContainer(int id, Inventory inventory, AbstractTileEntity te, BlockHitResult result) {
        if (te instanceof EnderCellTile) {
            return new EnderCellContainer(id, inventory, (EnderCellTile) te);
        }
        return null;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context);
    }
}
