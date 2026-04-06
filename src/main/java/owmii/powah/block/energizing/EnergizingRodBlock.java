package owmii.powah.block.energizing;

import static net.minecraft.world.phys.shapes.Shapes.join;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import org.jspecify.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.api.wrench.IWrenchable;
import owmii.powah.api.wrench.WrenchMode;
import owmii.powah.block.Tier;
import owmii.powah.components.PowahComponents;
import owmii.powah.item.WrenchItem;
import owmii.powah.lib.block.PowahBaseEnergyBlock;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.util.math.V3d;

public class EnergizingRodBlock extends PowahBaseEnergyBlock<EnergizingRodBlock> implements SimpleWaterloggedBlock, IWrenchable {
    public EnergizingRodBlock(Properties properties, Tier tier) {
        var config = Powah.config().devices.energizing_rods;
        super(properties, tier, () -> config.getCapacity(tier), () -> config.getTransfer(tier));
        setStateProps(state -> state.setValue(BlockStateProperties.FACING, Direction.DOWN));
        this.shapes.put(Direction.UP, join(box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D),
                join(box(7.0D, 13.0D, 7.0D, 9.0D, 16.0D, 9.0D), box(7.25D, 9.0D, 7.25D, 8.75D, 13.0D, 8.75D), BooleanOp.OR), BooleanOp.OR));
        this.shapes.put(Direction.DOWN, join(box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D),
                join(box(7.0D, 0.0D, 7.0D, 9.0D, 3.0D, 9.0D), box(7.25D, 3.0D, 7.25D, 8.75D, 7.0D, 8.75D), BooleanOp.OR), BooleanOp.OR));
        this.shapes.put(Direction.NORTH, join(box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D),
                join(box(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 3.0D), box(7.25D, 7.25D, 3.0D, 8.75D, 8.75D, 7.0D), BooleanOp.OR), BooleanOp.OR));
        this.shapes.put(Direction.SOUTH, join(box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D),
                join(box(7.0D, 7.0D, 13.0D, 9.0D, 9.0D, 16.0D), box(7.25D, 7.25D, 13.0D, 8.75D, 8.75D, 9.0D), BooleanOp.OR), BooleanOp.OR));
        this.shapes.put(Direction.WEST, join(box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D),
                join(box(0.0D, 7.0D, 7.0D, 3.0D, 9.0D, 9.0D), box(3.0D, 7.25D, 7.25D, 7.0D, 8.75D, 8.75D), BooleanOp.OR), BooleanOp.OR));
        this.shapes.put(Direction.EAST, join(box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D),
                join(box(13.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D), box(13.0D, 7.25D, 7.25D, 9.0D, 8.75D, 8.75D), BooleanOp.OR), BooleanOp.OR));
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ResourceKey<CreativeModeTab> group) {
        return super.getBlockItem(properties.stacksTo(1), group);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergizingRodBlockEntity(pos, state);
    }

    @Override
    protected boolean checkValidEnergySideProperty() {
        return true;
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof EnergizingRodBlockEntity) {
            setOrbPos(worldIn, pos, (EnergizingRodBlockEntity) tileEntity);
        }
    }

    public void setOrbPos(Level worldIn, BlockPos pos, EnergizingRodBlockEntity tile) {
        int range = Powah.config().general.energizing_range;
        List<BlockPos> list = BlockPos.betweenClosedStream(pos.offset(-range, -range, -range), pos.offset(range, range, range))
                .map(BlockPos::immutable).collect(Collectors.toList());
        for (BlockPos pos1 : list) {
            if (pos1.equals(BlockPos.ZERO))
                continue;
            BlockEntity tileEntity1 = worldIn.getBlockEntity(pos1);
            if (tileEntity1 instanceof EnergizingOrbBlockEntity) {
                tile.setOrbPos(pos1);
                break;
            }
        }
    }

    @Override
    protected Facing getFacing() {
        return Facing.ALL;
    }

    @Override
    public boolean onWrench(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, Direction side, WrenchMode mode,
            Vec3 hit) {
        if (mode.link()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof WrenchItem) {
                BlockEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity instanceof EnergizingRodBlockEntity rod) {
                    var orbPos = stack.get(PowahComponents.LINK_ORB_POS);
                    if (orbPos != null) {
                        if (world.getBlockEntity(orbPos) instanceof EnergizingOrbBlockEntity) {
                            V3d v3d = V3d.from(orbPos);
                            if ((int) v3d.distance(pos) <= Powah.config().general.energizing_range) {
                                rod.setOrbPos(orbPos);
                                player.sendOverlayMessage(Component.translatable("chat.powah.wrench.link.done").withStyle(ChatFormatting.GOLD));
                            } else {
                                player.sendOverlayMessage(Component.translatable("chat.powah.wrench.link.fail").withStyle(ChatFormatting.RED));
                            }
                        }
                        stack.remove(PowahComponents.LINK_ORB_POS);
                    } else {
                        stack.set(PowahComponents.LINK_ROD_POS, pos);
                        player.sendOverlayMessage(Component.translatable("chat.powah.wrench.link.start").withStyle(ChatFormatting.YELLOW));
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
