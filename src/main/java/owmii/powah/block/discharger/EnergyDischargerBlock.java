package owmii.powah.block.discharger;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.logistics.TransferType;
import owmii.lib.logistics.inventory.AbstractContainer;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.EnergyDischargerConfig;
import owmii.powah.inventory.DischargerContainer;

import javax.annotation.Nullable;

public class EnergyDischargerBlock extends AbstractEnergyBlock<Tier, EnergyDischargerConfig, EnergyDischargerBlock> {
    public EnergyDischargerBlock(Properties properties, Tier variant) {
        super(properties, variant);
    }

    @Override
    public EnergyDischargerConfig getConfig() {
        return Configs.ENERGY_DISCHARGER;
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return super.getBlockItem(properties.maxStackSize(1), group);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergyDischargerTile(this.variant);
    }

    @Nullable
    @Override
    public <T extends AbstractTileEntity> AbstractContainer getContainer(int id, PlayerInventory inventory, AbstractTileEntity te, BlockRayTraceResult result) {
        if (te instanceof EnergyDischargerTile) {
            return new DischargerContainer(id, inventory, (EnergyDischargerTile) te);
        }
        return null;
    }

    @Override
    public TransferType getTransferType() {
        return TransferType.EXTRACT;
    }

    @Override
    protected Facing getFacing() {
        return Facing.HORIZONTAL;
    }
}
