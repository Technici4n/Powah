package owmii.powah.block.energizing;

import static net.minecraft.world.phys.shapes.Shapes.join;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import org.jspecify.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.api.wrench.IWrenchable;
import owmii.powah.api.wrench.WrenchMode;
import owmii.powah.components.PowahComponents;
import owmii.powah.item.WrenchItem;
import owmii.powah.lib.block.PowahBaseBlock;
import owmii.powah.lib.logistics.inventory.Inventory;
import owmii.powah.util.math.V3d;

public class EnergizingOrbBlock extends PowahBaseBlock<EnergizingOrbBlock> implements SimpleWaterloggedBlock, IWrenchable {
    public EnergizingOrbBlock(Properties properties) {
        super(properties);
        setStateProps(state -> state.setValue(BlockStateProperties.FACING, Direction.DOWN));
        this.shapes.put(Direction.UP, join(box(3.5D, 11.0D, 3.5D, 12.5D, 1.77D, 12.5D), box(2.5D, 15.0D, 2.5D, 13.5D, 16.0D, 13.5D), BooleanOp.OR));
        this.shapes.put(Direction.DOWN, join(box(3.5D, 14.23D, 3.5D, 12.5D, 5.0D, 12.5D), box(2.5D, 0.0D, 2.5D, 13.5D, 1.0D, 13.5D), BooleanOp.OR));
        this.shapes.put(Direction.NORTH, join(box(3.5D, 3.5D, 14.23D, 12.5D, 12.5D, 5.0D), box(2.5D, 2.5D, 0.0D, 13.5D, 13.5D, 1.0D), BooleanOp.OR));
        this.shapes.put(Direction.SOUTH,
                join(box(3.5D, 3.5D, 11.0D, 12.5D, 12.5D, 1.77D), box(2.5D, 2.5D, 15.0D, 13.5D, 13.5D, 16.0D), BooleanOp.OR));
        this.shapes.put(Direction.WEST, join(box(14.23D, 3.5D, 3.5D, 5.0D, 12.5D, 12.5D), box(0.0D, 2.5D, 2.5D, 1.0D, 13.5D, 13.5D), BooleanOp.OR));
        this.shapes.put(Direction.EAST, join(box(11.0D, 3.5D, 3.5D, 1.77D, 12.5D, 12.5D), box(15.0D, 2.5D, 2.5D, 16.0D, 13.5D, 13.5D), BooleanOp.OR));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergizingOrbBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
            BlockHitResult hitResult) {
        BlockEntity tileentity = level.getBlockEntity(pos);
        if (tileentity instanceof EnergizingOrbBlockEntity orb) {
            Inventory inv = orb.getInventory();
            ItemStack output = inv.getStackInSlot(0);
            if (held.isEmpty() || !output.isEmpty()) {
                if (!level.isClientSide()) {
                    player.getInventory().placeItemBackInInventory(inv.removeNext());
                }
                return InteractionResult.SUCCESS;
            } else {
                ItemStack copy = held.copy();
                copy.setCount(1);
                if (!inv.addNext(copy).isEmpty() && !player.isCreative()) {
                    held.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(held, state, level, pos, player, hand, hitResult);
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        search(worldIn, pos);
    }

    @Override
    protected Facing getFacing() {
        return Facing.ALL;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction side) {
        BlockEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof EnergizingOrbBlockEntity) {
            EnergizingOrbBlockEntity orb = (EnergizingOrbBlockEntity) tileentity;
            return orb.getInventory().getNonEmptyStacks().size();
        }
        return super.getAnalogOutputSignal(state, world, pos, side);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public void search(Level worldIn, BlockPos pos) {
        int range = Powah.config().general.energizing_range;
        List<BlockPos> list = BlockPos.betweenClosedStream(pos.offset(-range, -range, -range), pos.offset(range, range, range))
                .map(BlockPos::immutable).filter(pos1 -> !pos.equals(pos1)).collect(Collectors.toList());
        list.stream().filter(worldIn::isLoaded).forEach(pos1 -> {
            BlockEntity tileEntity1 = worldIn.getBlockEntity(pos1);
            if (tileEntity1 instanceof EnergizingRodBlockEntity) {
                if (!((EnergizingRodBlockEntity) tileEntity1).hasOrb()) {
                    ((EnergizingRodBlockEntity) tileEntity1).setOrbPos(pos);
                }
            }
        });
    }

    @Override
    public boolean onWrench(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, Direction side, WrenchMode mode,
            Vec3 hit) {
        if (mode.link()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof WrenchItem) {
                BlockEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity instanceof EnergizingOrbBlockEntity) {
                    BlockPos rodPos = stack.get(PowahComponents.LINK_ROD_POS);
                    if (rodPos != null) {
                        if (world.getBlockEntity(rodPos) instanceof EnergizingRodBlockEntity rod) {
                            V3d v3d = V3d.from(rodPos);
                            if ((int) v3d.distance(pos) <= Powah.config().general.energizing_range) {
                                rod.setOrbPos(pos);
                                player.sendOverlayMessage(Component.translatable("chat.powah.wrench.link.done").withStyle(ChatFormatting.GOLD));
                            } else {
                                player.sendOverlayMessage(Component.translatable("chat.powah.wrench.link.fail").withStyle(ChatFormatting.RED));
                            }
                        }
                        stack.remove(PowahComponents.LINK_ROD_POS);
                    } else {
                        stack.set(PowahComponents.LINK_ORB_POS, pos);
                        player.sendOverlayMessage(Component.translatable("chat.powah.wrench.link.start").withStyle(ChatFormatting.YELLOW));
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
