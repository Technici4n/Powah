package owmii.powah.block.energydischarger;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.block.TileBase;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.energy.SideConfig;
import owmii.lib.inventory.ContainerBase;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.inventory.EnergyDischargerContainer;
import owmii.powah.inventory.IContainers;

import javax.annotation.Nullable;

public class EnergyDischargerBlock extends AbstractEnergyBlock<Tier> {
    public EnergyDischargerBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setDefaultState();
    }

    @Override
    public IEnergyConfig<Tier> getEnergyConfig() {
        return Configs.ENERGY_DISCHARGER;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergyDischargerTile(this.variant);
    }

    @Override
    protected boolean semiFullShape() {
        return true;
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase inv) {
        if (inv instanceof EnergyDischargerTile) {
            return new EnergyDischargerContainer(IContainers.ENERGY_DISCHARGER, id, playerInventory, (EnergyDischargerTile) inv);
        }
        return null;
    }

    @Override
    public SideConfig.Type getTransferType() {
        return SideConfig.Type.OUT;
    }

    @Override
    protected Facing getFacing() {
        return Facing.HORIZONTAL;
    }
}
