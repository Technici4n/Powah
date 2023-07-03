package owmii.powah.block.ender;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.config.v2.types.EnderConfig;
import owmii.powah.inventory.EnderCellContainer;
import owmii.powah.lib.block.AbstractEnergyBlock;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.logistics.inventory.AbstractContainer;

public class EnderCellBlock extends AbstractEnergyBlock<EnderConfig, EnderCellBlock> {
    public EnderCellBlock(Properties properties, Tier variant) {
        super(properties, variant);
    }

    @Override
    public EnderConfig getConfig() {
        return Powah.config().devices.ender_cells;
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ResourceKey<CreativeModeTab> group) {
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
