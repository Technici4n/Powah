package owmii.powah.block.energizing;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemHandlerHelper;
import owmii.lib.block.BlockBase;
import owmii.lib.inventory.Inventory;
import owmii.lib.util.math.V3d;
import owmii.powah.api.wrench.IWrench;
import owmii.powah.api.wrench.IWrenchable;
import owmii.powah.api.wrench.WrenchMode;
import owmii.powah.config.Config;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.util.math.shapes.VoxelShapes.combineAndSimplify;

public class EnergizingOrbBlock extends BlockBase implements IWaterLoggable, IWrenchable {
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
            ItemStack output = inv.getStackInSlot(0);
            ItemStack off = player.getHeldItemOffhand();
            if (!(off.getItem() instanceof IWrench && ((IWrench) off.getItem()).getWrenchMode(off).link())) {
                if (held.isEmpty() || !output.isEmpty()) {
                    if (!world.isRemote) {
                        ItemHandlerHelper.giveItemToPlayer(player, inv.removeNext());
                    }
                    return true;
                } else {
                    if (!world.isRemote) {
                        ItemStack copy = held.copy();
                        copy.setCount(1);
                        inv.addNext(copy);
                        if (!player.isCreative()) {
                            held.shrink(1);
                        }
                    }
                    return true;
                }
            }
        }

        return super.onBlockActivated(state, world, pos, player, hand, blockRayTraceResult);
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        search(worldIn, pos);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        int range = Config.ENERGIZING_CONFIG.range.get();

        List<BlockPos> list = BlockPos.getAllInBox(pos.add(-range, -range, -range), pos.add(range, range, range))
                .map(BlockPos::toImmutable)
                .filter(pos1 -> !pos.equals(pos1))
                .collect(Collectors.toList());

        list.forEach(pos1 -> {
            TileEntity tileEntity1 = worldIn.getTileEntity(pos1);
            if (tileEntity1 instanceof EnergizingRodTile) {
                if (pos.equals(((EnergizingRodTile) tileEntity1).getOrbPos())) {
                    ((EnergizingRodTile) tileEntity1).setOrbPos(BlockPos.ZERO);
                }
            }
        });

        list.forEach(pos1 -> {
            BlockState state1 = worldIn.getBlockState(pos1);
            if (state1.getBlock() instanceof EnergizingOrbBlock) {
                ((EnergizingOrbBlock) state1.getBlock()).search(worldIn, pos1);
            }
        });
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    public void search(World worldIn, BlockPos pos) {
        int range = Config.ENERGIZING_CONFIG.range.get();

        List<BlockPos> list = BlockPos.getAllInBox(pos.add(-range, -range, -range), pos.add(range, range, range))
                .map(BlockPos::toImmutable)
                .filter(pos1 -> !pos.equals(pos1))
                .collect(Collectors.toList());

        list.forEach(pos1 -> {
            TileEntity tileEntity1 = worldIn.getTileEntity(pos1);
            if (tileEntity1 instanceof EnergizingRodTile) {
                if (!((EnergizingRodTile) tileEntity1).hasOrb()) {
                    ((EnergizingRodTile) tileEntity1).setOrbPos(pos);
                }
            }
        });
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

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof EnergizingOrbTile) {
            EnergizingOrbTile orb = (EnergizingOrbTile) tileEntity;
            return orb.getInventory().getNonEmptyStacks().size();
        }
        return super.getComparatorInputOverride(state, world, pos);
    }

    @Override
    public boolean onWrench(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction side, WrenchMode mode, Vec3d hit) {
        if (mode.link()) {
            ItemStack stack = player.getHeldItem(hand);
            if (stack.getItem() instanceof IWrench) {
                IWrench wrench = (IWrench) stack.getItem();
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof EnergizingOrbTile) {
                    EnergizingOrbTile orb = (EnergizingOrbTile) tileEntity;
                    CompoundNBT nbt = wrench.getWrenchNBT(stack);
                    if (nbt.contains("RodPos", Constants.NBT.TAG_COMPOUND)) {
                        BlockPos rodPos = NBTUtil.readBlockPos(nbt.getCompound("RodPos"));
                        TileEntity tileEntity1 = world.getTileEntity(rodPos);
                        if (tileEntity1 instanceof EnergizingRodTile) {
                            EnergizingRodTile rod = (EnergizingRodTile) tileEntity1;
                            V3d v3d = V3d.from(rodPos);
                            if ((int) v3d.distance(pos) <= Config.ENERGIZING_CONFIG.range.get()) {
                                rod.setOrbPos(pos);
                                player.sendStatusMessage(new TranslationTextComponent("chat.powah.wrench.link.done").applyTextStyle(TextFormatting.GOLD), true);
                            } else {
                                player.sendStatusMessage(new TranslationTextComponent("chat.powah.wrench.link.fail").applyTextStyle(TextFormatting.RED), true);
                            }
                        }
                        nbt.remove("RodPos");
                    } else {
                        nbt.put("OrbPos", NBTUtil.writeBlockPos(pos));
                        player.sendStatusMessage(new TranslationTextComponent("chat.powah.wrench.link.start").applyTextStyle(TextFormatting.YELLOW), true);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
