package owmii.powah.block.energizing;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemHandlerHelper;
import owmii.lib.block.AbstractBlock;
import owmii.lib.client.handler.IHud;
import owmii.lib.logistics.inventory.Inventory;
import owmii.lib.registry.IVariant;
import owmii.lib.util.Util;
import owmii.lib.util.math.V3d;
import owmii.powah.api.wrench.IWrenchable;
import owmii.powah.api.wrench.WrenchMode;
import owmii.powah.config.Configs;
import owmii.powah.item.WrenchItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.util.math.shapes.VoxelShapes.combineAndSimplify;

public class EnergizingOrbBlock extends AbstractBlock<IVariant.Single, EnergizingOrbBlock> implements IWaterLoggable, IWrenchable, IHud {

    public EnergizingOrbBlock(Properties properties) {
        super(properties);
        setStateProps(state -> state.with(BlockStateProperties.FACING, Direction.DOWN));
        this.shapes.put(Direction.UP, combineAndSimplify(makeCuboidShape(3.5D, 11.0D, 3.5D, 12.5D, 1.77D, 12.5D), makeCuboidShape(2.5D, 15.0D, 2.5D, 13.5D, 16.0D, 13.5D), IBooleanFunction.OR));
        this.shapes.put(Direction.DOWN, combineAndSimplify(makeCuboidShape(3.5D, 14.23D, 3.5D, 12.5D, 5.0D, 12.5D), makeCuboidShape(2.5D, 0.0D, 2.5D, 13.5D, 1.0D, 13.5D), IBooleanFunction.OR));
        this.shapes.put(Direction.NORTH, combineAndSimplify(makeCuboidShape(3.5D, 3.5D, 14.23D, 12.5D, 12.5D, 5.0D), makeCuboidShape(2.5D, 2.5D, 0.0D, 13.5D, 13.5D, 1.0D), IBooleanFunction.OR));
        this.shapes.put(Direction.SOUTH, combineAndSimplify(makeCuboidShape(3.5D, 3.5D, 11.0D, 12.5D, 12.5D, 1.77D), makeCuboidShape(2.5D, 2.5D, 15.0D, 13.5D, 13.5D, 16.0D), IBooleanFunction.OR));
        this.shapes.put(Direction.WEST, combineAndSimplify(makeCuboidShape(14.23D, 3.5D, 3.5D, 5.0D, 12.5D, 12.5D), makeCuboidShape(0.0D, 2.5D, 2.5D, 1.0D, 13.5D, 13.5D), IBooleanFunction.OR));
        this.shapes.put(Direction.EAST, combineAndSimplify(makeCuboidShape(11.0D, 3.5D, 3.5D, 1.77D, 12.5D, 12.5D), makeCuboidShape(15.0D, 2.5D, 2.5D, 16.0D, 13.5D, 13.5D), IBooleanFunction.OR));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergizingOrbTile();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        ItemStack held = player.getHeldItem(hand);
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof EnergizingOrbTile) {
            EnergizingOrbTile orb = (EnergizingOrbTile) tileentity;
            Inventory inv = orb.getInventory();
            ItemStack output = inv.getStackInSlot(0);
            ItemStack off = player.getHeldItemOffhand();
            //  if (!(off.getItem() instanceof IWrench && ((IWrench) off.getItem()).getWrenchMode(off).link())) {
            if (held.isEmpty() || !output.isEmpty()) {
                if (!world.isRemote) {
                    ItemHandlerHelper.giveItemToPlayer(player, inv.removeNext());
                }
                return ActionResultType.SUCCESS;
            } else {
                // if (!world.isRemote) {
                ItemStack copy = held.copy();
                copy.setCount(1);
                if (!inv.addNext(copy).isEmpty() && !player.isCreative()) {
                    held.shrink(1);
                }
                // }
                return ActionResultType.SUCCESS;
            }
            // }
        }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        search(worldIn, pos);
    }

    @Override
    protected Facing getFacing() {
        return Facing.ALL;
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof EnergizingOrbTile) {
            EnergizingOrbTile orb = (EnergizingOrbTile) tileentity;
            return orb.getInventory().getNonEmptyStacks().size();
        }
        return super.getComparatorInputOverride(state, world, pos);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        int range = Configs.ENERGIZING.range.get();
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
        int range = Configs.ENERGIZING.range.get();
        List<BlockPos> list = BlockPos.getAllInBox(pos.add(-range, -range, -range), pos.add(range, range, range)).map(BlockPos::toImmutable).filter(pos1 -> !pos.equals(pos1)).collect(Collectors.toList());
        list.stream().filter(p -> worldIn.isBlockPresent(pos)).forEach(pos1 -> {
            TileEntity tileEntity1 = worldIn.getTileEntity(pos1);
            if (tileEntity1 instanceof EnergizingRodTile) {
                if (!((EnergizingRodTile) tileEntity1).hasOrb()) {
                    ((EnergizingRodTile) tileEntity1).setOrbPos(pos);
                }
            }
        });
    }

    @Override
    public boolean onWrench(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction side, WrenchMode mode, Vector3d hit) {
        if (mode.link()) {
            ItemStack stack = player.getHeldItem(hand);
            if (stack.getItem() instanceof WrenchItem) {
                WrenchItem wrench = (WrenchItem) stack.getItem();
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
                            if ((int) v3d.distance(pos) <= Configs.ENERGIZING.range.get()) {
                                rod.setOrbPos(pos);
                                player.sendStatusMessage(new TranslationTextComponent("chat.powah.wrench.link.done").mergeStyle(TextFormatting.GOLD), true);
                            } else {
                                player.sendStatusMessage(new TranslationTextComponent("chat.powah.wrench.link.fail").mergeStyle(TextFormatting.RED), true);
                            }
                        }
                        nbt.remove("RodPos");
                    } else {
                        nbt.put("OrbPos", NBTUtil.writeBlockPos(pos));
                        player.sendStatusMessage(new TranslationTextComponent("chat.powah.wrench.link.start").mergeStyle(TextFormatting.YELLOW), true);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean renderHud(MatrixStack matrix, BlockState state, World world, BlockPos pos, PlayerEntity player, BlockRayTraceResult result, @Nullable TileEntity te) {
        if (te instanceof EnergizingOrbTile) {
            EnergizingOrbTile orb = (EnergizingOrbTile) te;
            if (orb.getBuffer().getCapacity() > 0) {
                RenderSystem.pushMatrix();
                RenderSystem.enableBlend();
                Minecraft mc = Minecraft.getInstance();
                FontRenderer font = mc.fontRenderer;
                int x = mc.getMainWindow().getScaledWidth() / 2;
                int y = mc.getMainWindow().getScaledHeight();
                String s = "" + TextFormatting.GREEN + orb.getBuffer().getPercent() + "%";
                String s1 = TextFormatting.GRAY + I18n.format("info.lollipop.fe.stored", Util.addCommas(orb.getBuffer().getEnergyStored()), Util.numFormat(orb.getBuffer().getCapacity()));
                font.drawStringWithShadow(matrix, s, x - (font.getStringWidth(s) / 2.0f), y - 90, 0xffffff);
                font.drawStringWithShadow(matrix, s1, x - (font.getStringWidth(s1) / 2.0f), y - 75, 0xffffff);
                RenderSystem.disableBlend();
                RenderSystem.popMatrix();
            }
        }
        return true;
    }
}
