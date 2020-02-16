package owmii.powah.block.endergate;

import net.minecraft.util.Direction;
import owmii.lib.block.AbstractBlock;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;
import owmii.powah.block.endercell.EnderCellTile;

import javax.annotation.Nullable;

public class EnderGateTile extends EnderCellTile {
    public EnderGateTile(Tier variant) {
        super(ITiles.ENDER_GATE, variant);
    }

    public EnderGateTile() {
        this(Tier.STARTER);
    }

    @Override
    public boolean isExtender() {
        return false;
    }

    @Override
    public boolean isEnergyPresent(@Nullable Direction side) {
        return side != null && side.equals(getBlockState().get(AbstractBlock.FACING));
    }
}
