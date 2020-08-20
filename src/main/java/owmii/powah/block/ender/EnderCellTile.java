package owmii.powah.block.ender;

import owmii.lib.block.IInventoryHolder;
import owmii.lib.block.IOwnable;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.config.EnderCellConfig;

public class EnderCellTile extends AbstractEnderTile<Tier, EnderCellConfig, EnderCellBlock> implements IOwnable, IInventoryHolder {
    public EnderCellTile(Tier variant) {
        super(Tiles.ENDER_CELL, variant);
        this.inv.add(3);
    }

    public EnderCellTile() {
        this(Tier.STARTER);
    }

    @Override
    public boolean isExtender() {
        return true;
    }

    @Override
    public int getMaxChannels() {
        return getConfig().getChannels(getVariant());
    }
}
