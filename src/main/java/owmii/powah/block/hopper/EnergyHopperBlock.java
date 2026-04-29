package owmii.powah.block.hopper;

import java.util.function.LongSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.inventory.EnergyHopperMenu;
import owmii.powah.lib.block.PowahBaseBlockEntity;
import owmii.powah.lib.block.PowahBaseEnergyBlock;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.logistics.inventory.BaseMenu;

public class EnergyHopperBlock extends PowahBaseEnergyBlock<EnergyHopperBlock> {
    private final LongSupplier chargingRateSupplier;

    public EnergyHopperBlock(Properties properties, Tier tier) {
        var config = Powah.config().devices.hoppers;
        super(properties, tier, () -> config.getCapacity(tier), () -> config.getTransfer(tier));
        chargingRateSupplier = () -> config.getChargingRate(tier);
        this.shapes.put(Direction.UP, box(0, 0, 0, 16, 12, 16));
        this.shapes.put(Direction.DOWN, box(0, 4, 0, 16, 16, 16));
        this.shapes.put(Direction.NORTH, box(0, 0, 4, 16, 16, 16));
        this.shapes.put(Direction.SOUTH, box(0, 0, 0, 16, 16, 12));
        this.shapes.put(Direction.EAST, box(0, 0, 0, 12, 16, 16));
        this.shapes.put(Direction.WEST, box(4, 0, 0, 16, 16, 16));
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ResourceKey<CreativeModeTab> group) {
        return super.getBlockItem(properties.stacksTo(1), group);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergyHopperBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends PowahBaseBlockEntity> BaseMenu getContainer(int id, Inventory inventory, PowahBaseBlockEntity te,
            BlockHitResult result) {
        if (te instanceof EnergyHopperBlockEntity) {
            return new EnergyHopperMenu(id, inventory, (EnergyHopperBlockEntity) te);
        }
        return null;
    }

    @Override
    protected Facing getFacing() {
        return Facing.ALL;
    }

    public final long getChargingRate() {
        return chargingRateSupplier.getAsLong();
    }
}
