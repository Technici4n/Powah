package owmii.powah.block.magmator;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
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
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.config.v2.types.GeneratorConfig;
import owmii.powah.inventory.MagmatorContainer;
import owmii.powah.lib.block.AbstractGeneratorBlock;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.logistics.fluid.Tank;
import owmii.powah.lib.logistics.inventory.AbstractContainer;

public class MagmatorBlock extends AbstractGeneratorBlock<MagmatorBlock> {
    public MagmatorBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setDefaultState();
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ResourceKey<CreativeModeTab> group) {
        return super.getBlockItem(properties.stacksTo(1), group);
    }

    @Override
    public GeneratorConfig getConfig() {
        return Powah.config().generators.magmators;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MagmatorTile(pos, state, this.variant);
    }

    @Nullable
    @Override
    public <T extends AbstractTileEntity> AbstractContainer getContainer(int id, Inventory inventory, AbstractTileEntity te, BlockHitResult result) {
        if (te instanceof MagmatorTile) {
            return new MagmatorContainer(id, inventory, (MagmatorTile) te);
        }
        return null;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof MagmatorTile magmator) {
            Tank tank = magmator.getTank();
            if (FluidUtil.interactWithFluidHandler(player, hand, tank)) {
                magmator.sync();
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, world, pos, player, hand, result);
    }

    @Override
    protected Facing getFacing() {
        return Facing.HORIZONTAL;
    }

    @Override
    protected boolean isPlacerFacing() {
        return true;
    }

    @Override
    protected boolean hasLitProp() {
        return true;
    }
}
