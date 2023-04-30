package owmii.powah.block.hopper;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.config.v2.types.ChargingConfig;
import owmii.powah.inventory.EnergyHopperContainer;
import owmii.powah.lib.block.AbstractEnergyBlock;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.logistics.inventory.AbstractContainer;

public class EnergyHopperBlock extends AbstractEnergyBlock<ChargingConfig, EnergyHopperBlock> {
    public EnergyHopperBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setDefaultState();
        this.shapes.put(Direction.UP, box(0, 0, 0, 16, 12, 16));
        this.shapes.put(Direction.DOWN, box(0, 4, 0, 16, 16, 16));
        this.shapes.put(Direction.NORTH, box(0, 0, 4, 16, 16, 16));
        this.shapes.put(Direction.SOUTH, box(0, 0, 0, 16, 16, 12));
        this.shapes.put(Direction.EAST, box(0, 0, 0, 12, 16, 16));
        this.shapes.put(Direction.WEST, box(4, 0, 0, 16, 16, 16));
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable CreativeModeTab group) {
        return super.getBlockItem(properties.stacksTo(1), group);
    }

    @Override
    public ChargingConfig getConfig() {
        return Powah.config().devices.hoppers;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergyHopperTile(pos, state, this.variant);
    }

    @Nullable
    @Override
    public <T extends AbstractTileEntity> AbstractContainer getContainer(int id, Inventory inventory, AbstractTileEntity te, BlockHitResult result) {
        if (te instanceof EnergyHopperTile) {
            return new EnergyHopperContainer(id, inventory, (EnergyHopperTile) te);
        }
        return null;
    }

    @Override
    protected Facing getFacing() {
        return Facing.ALL;
    }
}
