package owmii.powah.block.energycell;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.config.v2.types.EnergyConfig;
import owmii.powah.inventory.EnergyCellContainer;
import owmii.powah.item.EnergyCellItem;
import owmii.powah.lib.block.AbstractEnergyBlock;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.logistics.inventory.AbstractContainer;

public class EnergyCellBlock extends AbstractEnergyBlock<EnergyConfig, EnergyCellBlock> implements SimpleWaterloggedBlock {
    public EnergyCellBlock(Properties properties, Tier tier) {
        super(properties, tier);
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ResourceKey<CreativeModeTab> group) {
        return new EnergyCellItem(this, properties.stacksTo(1), group);
    }

    @Override
    public EnergyConfig getConfig() {
        return Powah.config().devices.energy_cells;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergyCellTile(pos, state, this.variant);
    }

    @Nullable
    @Override
    public AbstractContainer getContainer(int id, Inventory inventory, AbstractTileEntity te, BlockHitResult result) {
        if (te instanceof EnergyCellTile) {
            return new EnergyCellContainer(id, inventory, (EnergyCellTile) te);
        }
        return null;
    }
}
