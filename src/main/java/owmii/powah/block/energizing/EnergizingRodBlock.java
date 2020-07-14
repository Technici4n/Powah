package owmii.powah.block.energizing;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.client.gui.GuiUtils;
import owmii.lib.Lollipop;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.client.handler.IHud;
import owmii.lib.client.util.Draw;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.util.Util;
import owmii.lib.util.math.V3d;
import owmii.powah.api.wrench.IWrenchable;
import owmii.powah.api.wrench.WrenchMode;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.EnergizingConfig;
import owmii.powah.item.WrenchItem;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.minecraft.util.math.shapes.VoxelShapes.combineAndSimplify;

public class EnergizingRodBlock extends AbstractEnergyBlock<Tier, EnergizingConfig> implements IWaterLoggable, IWrenchable, IHud {
    private static final Map<Direction, VoxelShape> VOXEL_SHAPES = new HashMap<>();

    public EnergizingRodBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setStateProps(state -> state.with(BlockStateProperties.FACING, Direction.DOWN));
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return super.getBlockItem(properties.maxStackSize(1), group);
    }

    static {
        VOXEL_SHAPES.put(Direction.UP, combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), combineAndSimplify(makeCuboidShape(7.0D, 13.0D, 7.0D, 9.0D, 16.0D, 9.0D), makeCuboidShape(7.25D, 9.0D, 7.25D, 8.75D, 13.0D, 8.75D), IBooleanFunction.OR), IBooleanFunction.OR));
        VOXEL_SHAPES.put(Direction.DOWN, combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), combineAndSimplify(makeCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 3.0D, 9.0D), makeCuboidShape(7.25D, 3.0D, 7.25D, 8.75D, 7.0D, 8.75D), IBooleanFunction.OR), IBooleanFunction.OR));
        VOXEL_SHAPES.put(Direction.NORTH, combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 3.0D), makeCuboidShape(7.25D, 7.25D, 3.0D, 8.75D, 8.75D, 7.0D), IBooleanFunction.OR), IBooleanFunction.OR));
        VOXEL_SHAPES.put(Direction.SOUTH, combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 13.0D, 9.0D, 9.0D, 16.0D), makeCuboidShape(7.25D, 7.25D, 13.0D, 8.75D, 8.75D, 9.0D), IBooleanFunction.OR), IBooleanFunction.OR));
        VOXEL_SHAPES.put(Direction.WEST, combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), combineAndSimplify(makeCuboidShape(0.0D, 7.0D, 7.0D, 3.0D, 9.0D, 9.0D), makeCuboidShape(3.0D, 7.25D, 7.25D, 7.0D, 8.75D, 8.75D), IBooleanFunction.OR), IBooleanFunction.OR));
        VOXEL_SHAPES.put(Direction.EAST, combineAndSimplify(makeCuboidShape(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), combineAndSimplify(makeCuboidShape(13.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D), makeCuboidShape(13.0D, 7.25D, 7.25D, 9.0D, 8.75D, 8.75D), IBooleanFunction.OR), IBooleanFunction.OR));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VOXEL_SHAPES.get(state.get(BlockStateProperties.FACING));
    }

    @Override
    public EnergizingConfig getConfig() {
        return Configs.ENERGIZING;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EnergizingRodTile(this.variant);
    }

    @Override
    protected boolean checkValidEnergySide() {
        return true;
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof EnergizingRodTile) {
            setOrbPos(worldIn, pos, (EnergizingRodTile) tileEntity);
        }
    }

    public void setOrbPos(World worldIn, BlockPos pos, EnergizingRodTile tile) {
        int range = Configs.ENERGIZING.range.get();
        List<BlockPos> list = BlockPos.getAllInBox(pos.add(-range, -range, -range), pos.add(range, range, range)).map(BlockPos::toImmutable).collect(Collectors.toList());
        for (BlockPos pos1 : list) {
            if (pos1.equals(BlockPos.ZERO)) continue;
            TileEntity tileEntity1 = worldIn.getTileEntity(pos1);
            if (tileEntity1 instanceof EnergizingOrbTile) {
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
    public boolean onWrench(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction side, WrenchMode mode, Vector3d hit) {
        if (mode.link()) {
            ItemStack stack = player.getHeldItem(hand);
            if (stack.getItem() instanceof WrenchItem) {
                WrenchItem wrench = (WrenchItem) stack.getItem();
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof EnergizingRodTile) {
                    EnergizingRodTile rod = (EnergizingRodTile) tileEntity;
                    CompoundNBT nbt = wrench.getWrenchNBT(stack);
                    if (nbt.contains("OrbPos", Constants.NBT.TAG_COMPOUND)) {
                        BlockPos orbPos = NBTUtil.readBlockPos(nbt.getCompound("OrbPos"));
                        TileEntity tileEntity1 = world.getTileEntity(orbPos);
                        if (tileEntity1 instanceof EnergizingOrbTile) {
                            EnergizingOrbTile orb = (EnergizingOrbTile) tileEntity1;
                            V3d v3d = V3d.from(orbPos);
                            if ((int) v3d.distance(pos) <= Configs.ENERGIZING.range.get()) {
                                rod.setOrbPos(orbPos);
                                player.sendStatusMessage(new TranslationTextComponent("chat.powah.wrench.link.done").func_240701_a_(TextFormatting.GOLD), true);
                            } else {
                                player.sendStatusMessage(new TranslationTextComponent("chat.powah.wrench.link.fail").func_240701_a_(TextFormatting.RED), true);
                            }
                        }
                        nbt.remove("OrbPos");
                    } else {
                        nbt.put("RodPos", NBTUtil.writeBlockPos(pos));
                        player.sendStatusMessage(new TranslationTextComponent("chat.powah.wrench.link.start").func_240701_a_(TextFormatting.YELLOW), true);
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
        if (te instanceof EnergizingRodTile) {
            EnergizingRodTile rod = (EnergizingRodTile) te;
            RenderSystem.pushMatrix();
            RenderSystem.enableBlend();
            Minecraft mc = Minecraft.getInstance();
            FontRenderer font = mc.fontRenderer;
            int x = mc.getMainWindow().getScaledWidth() / 2;
            int y = mc.getMainWindow().getScaledHeight();
            String s = TextFormatting.GRAY + I18n.format("info.lollipop.stored.energy.fe", Util.addCommas(rod.getEnergy().getEnergyStored()), Util.numFormat(rod.getEnergy().getCapacity()));
            mc.getTextureManager().bindTexture(new ResourceLocation(Lollipop.MOD_ID, "textures/gui/ov_energy.png"));
            GuiUtils.drawTexturedModalRect(x - 37 - 1, y - 80, 0, 0, 74, 9, 0);
            Draw.gaugeH(x - 37, y - 79, 72, 16, 0, 9, ((EnergizingRodTile) te).getEnergy());
            font.func_238421_b_(matrix, s, x - (font.getStringWidth(s) / 2.0f), y - 67, 0xffffff);
            RenderSystem.disableBlend();
            RenderSystem.popMatrix();
        }
        return true;
    }
}
