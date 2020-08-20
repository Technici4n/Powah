package owmii.powah.block.ender;

import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.config.EnderGateConfig;

import javax.annotation.Nullable;

public class EnderGateTile extends AbstractEnderTile<Tier, EnderGateConfig, EnderGateBlock> {
    public EnderGateTile(Tier variant) {
        super(Tiles.ENDER_GATE, variant);
        this.inv.add(3);
    }

    public EnderGateTile() {
        this(Tier.STARTER);
    }

    @Override
    public boolean isExtender() {
        return false;
    }

    @Override
    public int getMaxChannels() {
        return getConfig().getChannels(getVariant());
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return slot > 0 && super.canInsert(slot, stack);
    }

    @Override
    public boolean isEnergyPresent(@Nullable Direction side) {
        return side != null && side.equals(getBlockState().get(BlockStateProperties.FACING));
    }
}
