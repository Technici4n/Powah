package owmii.powah.block.ender;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;

import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class EnderGateTile extends AbstractEnderTile<EnderGateBlock> {
    public EnderGateTile(BlockPos pos, BlockState state, Tier variant) {
        super(Tiles.ENDER_GATE.get(), pos, state, variant);
        this.inv.add(3);
    }

    public EnderGateTile(BlockPos pos, BlockState state) {
        this(pos, state, Tier.STARTER);
    }

    @Override
    public boolean isExtender() {
        return false;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return slot > 0 && super.canInsert(slot, stack);
    }

    @Override
    public boolean isEnergyPresent(@Nullable Direction side) {
        return side != null && side.equals(getBlockState().getValue(BlockStateProperties.FACING));
    }
}
