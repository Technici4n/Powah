package owmii.powah.block.energycell;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.block.TileBase;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.inventory.ContainerBase;
import owmii.lib.item.BlockItemBase;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.inventory.EnergyCellContainer;
import owmii.powah.inventory.IContainers;
import owmii.powah.item.EnergyCellItem;

import javax.annotation.Nullable;

public class EnergyCellBlock extends AbstractEnergyBlock<Tier> implements IWaterLoggable {
    public EnergyCellBlock(Properties properties, Tier tier) {
        super(properties, tier);
    }

    @Override
    public BlockItemBase getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return new EnergyCellItem(this, properties, group);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public IEnergyConfig<Tier> getEnergyConfig() {
        return Configs.ENERGY_CELL;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergyCellTile(this.variant);
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory inventory, TileBase te, BlockRayTraceResult result) {
        if (te instanceof EnergyCellTile) {
            return new EnergyCellContainer(IContainers.ENERGY_CELL, id, inventory, (EnergyCellTile) te);
        }
        return null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return this.variant.equals(Tier.CREATIVE);
    }
}
