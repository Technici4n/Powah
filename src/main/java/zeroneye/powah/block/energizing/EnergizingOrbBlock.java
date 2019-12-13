package zeroneye.powah.block.energizing;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import zeroneye.lib.block.BlockBase;
import zeroneye.lib.inventory.Inventory;
import zeroneye.powah.config.Config;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.util.math.shapes.VoxelShapes.combineAndSimplify;

public class EnergizingOrbBlock extends BlockBase implements IWaterLoggable {
    public EnergizingOrbBlock(Properties properties) {
        super(properties);
        setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return combineAndSimplify(
                makeCuboidShape(3.5D, 5.0D, 3.5D, 12.5D, 14.23D, 12.5D),
                makeCuboidShape(2.5D, 0.0D, 2.5D, 13.5D, 1.0D, 13.5D),
                IBooleanFunction.OR);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        ItemStack held = player.getHeldItem(hand);
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof EnergizingOrbTile) {
            EnergizingOrbTile orb = (EnergizingOrbTile) tileentity;
            Inventory inv = orb.getInventory();
            if (!world.isRemote) {
                if (player.isSneaking() || held.isEmpty() || !inv.getStackInSlot(0).isEmpty()) {
                    ItemHandlerHelper.giveItemToPlayer(player, inv.removeNext());
                } else {
                    ItemStack copy = held.copy();
                    copy.setCount(1);
                    inv.addNext(copy);
                    if (!player.isCreative()) {
                        held.shrink(1);
                    }
                }
            }
            return true;
        }

        return super.onBlockActivated(state, world, pos, player, hand, blockRayTraceResult);
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof EnergizingOrbTile) {
            int range = Config.ENERGIZING_CONFIG.range.get();
            List<BlockPos> list = BlockPos.getAllInBox(pos.add(-range, -range, -range), pos.add(range, range, range)).map(BlockPos::toImmutable).collect(Collectors.toList());
            list.forEach(pos1 -> {
                TileEntity tileEntity1 = worldIn.getTileEntity(pos1);
                if (tileEntity1 instanceof EnergizingRodTile) {
                    if (!((EnergizingRodTile) tileEntity1).hasOrb()) {
                        ((EnergizingRodTile) tileEntity1).setOrbPos(pos);
                    }
                }
            });
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof EnergizingOrbTile) {
            int range = Config.ENERGIZING_CONFIG.range.get();
            List<BlockPos> list = BlockPos.getAllInBox(pos.add(-range, -range, -range), pos.add(range, range, range)).map(BlockPos::toImmutable).collect(Collectors.toList());
            list.forEach(pos1 -> {
                TileEntity tileEntity1 = worldIn.getTileEntity(pos1);
                if (tileEntity1 instanceof EnergizingRodTile) {
                    if (pos.equals(((EnergizingRodTile) tileEntity1).getOrbPos())) {
                        ((EnergizingRodTile) tileEntity1).setOrbPos(BlockPos.ZERO);
                    }
                }
            });
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergizingOrbTile();
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }
}
