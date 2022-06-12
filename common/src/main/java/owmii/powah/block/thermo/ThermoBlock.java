package owmii.powah.block.thermo;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import owmii.powah.EnvHandler;
import owmii.powah.Powah;
import owmii.powah.config.v2.types.GeneratorConfig;
import owmii.powah.lib.block.AbstractGeneratorBlock;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.logistics.inventory.AbstractContainer;
import owmii.powah.block.Tier;
import owmii.powah.inventory.ThermoContainer;

import javax.annotation.Nullable;

public class ThermoBlock extends AbstractGeneratorBlock<ThermoBlock> {
    public ThermoBlock(Properties properties, Tier variant) {
        super(properties, variant);
    }

    @Override
    public GeneratorConfig getConfig() {
        return Powah.config().generators.thermo_generators;
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable CreativeModeTab group) {
        return super.getBlockItem(properties.stacksTo(1), group);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ThermoTile(pos, state, this.variant);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof ThermoTile genTile) {
            if (EnvHandler.INSTANCE.interactWithTank(player, hand, genTile.getTank())) {
                genTile.sync();
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, world, pos, player, hand, blockRayTraceResult);
    }

    @Nullable
    @Override
    public AbstractContainer getContainer(int id, Inventory inventory, AbstractTileEntity te, BlockHitResult result) {
        if (te instanceof ThermoTile) {
            return new ThermoContainer(id, inventory, (ThermoTile) te);
        }
        return null;
    }
}
