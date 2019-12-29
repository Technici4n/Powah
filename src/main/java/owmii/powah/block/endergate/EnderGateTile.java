package owmii.powah.block.endergate;

import net.minecraft.util.Direction;
import owmii.lib.block.BlockBase;
import owmii.powah.block.ITiles;
import owmii.powah.block.endercell.EnderCellTile;

import javax.annotation.Nullable;

public class EnderGateTile extends EnderCellTile {
    public EnderGateTile(int maxReceive, int maxExtract, int channels) {
        super(ITiles.ENDER_GATE, maxReceive, maxExtract, channels);
    }

    public EnderGateTile() {
        this(0, 0, 0);
    }

    @Override
    public boolean isEnergyPresent(@Nullable Direction side) {
        return side != null && side.equals(getBlockState().get(BlockBase.FACING));
    }

    @Override
    public boolean isExtender() {
        return false;
    }
}
