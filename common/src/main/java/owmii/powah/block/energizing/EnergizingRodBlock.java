package owmii.powah.block.energizing;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import owmii.powah.Powah;
import owmii.powah.config.v2.types.EnergyConfig;
import owmii.powah.lib.block.AbstractEnergyBlock;
import owmii.powah.lib.client.handler.IHud;
import owmii.powah.lib.client.util.Draw;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.util.Util;
import owmii.powah.lib.util.math.V3d;
import owmii.powah.api.wrench.IWrenchable;
import owmii.powah.api.wrench.WrenchMode;
import owmii.powah.block.Tier;
import owmii.powah.item.WrenchItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.world.phys.shapes.Shapes.join;

public class EnergizingRodBlock extends AbstractEnergyBlock<EnergyConfig, EnergizingRodBlock> implements SimpleWaterloggedBlock, IWrenchable, IHud {
    public EnergizingRodBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setStateProps(state -> state.setValue(BlockStateProperties.FACING, Direction.DOWN));
        this.shapes.put(Direction.UP, join(box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), join(box(7.0D, 13.0D, 7.0D, 9.0D, 16.0D, 9.0D), box(7.25D, 9.0D, 7.25D, 8.75D, 13.0D, 8.75D), BooleanOp.OR), BooleanOp.OR));
        this.shapes.put(Direction.DOWN, join(box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), join(box(7.0D, 0.0D, 7.0D, 9.0D, 3.0D, 9.0D), box(7.25D, 3.0D, 7.25D, 8.75D, 7.0D, 8.75D), BooleanOp.OR), BooleanOp.OR));
        this.shapes.put(Direction.NORTH, join(box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), join(box(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 3.0D), box(7.25D, 7.25D, 3.0D, 8.75D, 8.75D, 7.0D), BooleanOp.OR), BooleanOp.OR));
        this.shapes.put(Direction.SOUTH, join(box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), join(box(7.0D, 7.0D, 13.0D, 9.0D, 9.0D, 16.0D), box(7.25D, 7.25D, 13.0D, 8.75D, 8.75D, 9.0D), BooleanOp.OR), BooleanOp.OR));
        this.shapes.put(Direction.WEST, join(box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), join(box(0.0D, 7.0D, 7.0D, 3.0D, 9.0D, 9.0D), box(3.0D, 7.25D, 7.25D, 7.0D, 8.75D, 8.75D), BooleanOp.OR), BooleanOp.OR));
        this.shapes.put(Direction.EAST, join(box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D), join(box(13.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D), box(13.0D, 7.25D, 7.25D, 9.0D, 8.75D, 8.75D), BooleanOp.OR), BooleanOp.OR));
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable CreativeModeTab group) {
        return super.getBlockItem(properties.stacksTo(1), group);
    }

    @Override
    public EnergyConfig getConfig() {
        return Powah.config().devices.energizing_rods;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergizingRodTile(pos, state, this.variant);
    }

    @Override
    protected boolean checkValidEnergySide() {
        return true;
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof EnergizingRodTile) {
            setOrbPos(worldIn, pos, (EnergizingRodTile) tileEntity);
        }
    }

    public void setOrbPos(Level worldIn, BlockPos pos, EnergizingRodTile tile) {
        int range = Powah.config().general.energizing_range;
        List<BlockPos> list = BlockPos.betweenClosedStream(pos.offset(-range, -range, -range), pos.offset(range, range, range)).map(BlockPos::immutable).collect(Collectors.toList());
        for (BlockPos pos1 : list) {
            if (pos1.equals(BlockPos.ZERO)) continue;
            BlockEntity tileEntity1 = worldIn.getBlockEntity(pos1);
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
    public boolean onWrench(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, Direction side, WrenchMode mode, Vec3 hit) {
        if (mode.link()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof WrenchItem) {
                WrenchItem wrench = (WrenchItem) stack.getItem();
                BlockEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity instanceof EnergizingRodTile) {
                    EnergizingRodTile rod = (EnergizingRodTile) tileEntity;
                    CompoundTag nbt = wrench.getWrenchNBT(stack);
                    if (nbt.contains("OrbPos", Tag.TAG_COMPOUND)) {
                        BlockPos orbPos = NbtUtils.readBlockPos(nbt.getCompound("OrbPos"));
                        BlockEntity tileEntity1 = world.getBlockEntity(orbPos);
                        if (tileEntity1 instanceof EnergizingOrbTile) {
                            EnergizingOrbTile orb = (EnergizingOrbTile) tileEntity1;
                            V3d v3d = V3d.from(orbPos);
                            if ((int) v3d.distance(pos) <= Powah.config().general.energizing_range) {
                                rod.setOrbPos(orbPos);
                                player.displayClientMessage(Component.translatable("chat.powah.wrench.link.done").withStyle(ChatFormatting.GOLD), true);
                            } else {
                                player.displayClientMessage(Component.translatable("chat.powah.wrench.link.fail").withStyle(ChatFormatting.RED), true);
                            }
                        }
                        nbt.remove("OrbPos");
                    } else {
                        nbt.put("RodPos", NbtUtils.writeBlockPos(pos));
                        player.displayClientMessage(Component.translatable("chat.powah.wrench.link.start").withStyle(ChatFormatting.YELLOW), true);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean renderHud(PoseStack matrix, BlockState state, Level world, BlockPos pos, Player player, BlockHitResult result, @Nullable BlockEntity te) {
        if (te instanceof EnergizingRodTile rod) {
            RenderSystem.getModelViewStack().pushPose();
            RenderSystem.enableBlend();
            Minecraft mc = Minecraft.getInstance();
            Font font = mc.font;
            int x = mc.getWindow().getGuiScaledWidth() / 2;
            int y = mc.getWindow().getGuiScaledHeight();
            String s = ChatFormatting.GRAY + I18n.get("info.lollipop.stored") + ": " + I18n.get("info.lollipop.fe.stored", Util.addCommas(rod.getEnergy().getEnergyStored()), Util.numFormat(rod.getEnergy().getCapacity()));
            RenderSystem.setShaderTexture(0, new ResourceLocation("lollipop", "textures/gui/ov_energy.png"));
            Draw.drawTexturedModalRect(matrix, x - 37 - 1, y - 80, 0, 0, 74, 9, 0);
            Draw.gaugeH(x - 37, y - 79, 72, 16, 0, 9, ((EnergizingRodTile) te).getEnergy());
            font.drawShadow(matrix, s, x - (font.width(s) / 2.0f), y - 67, 0xffffff);
            RenderSystem.disableBlend();
            RenderSystem.getModelViewStack().popPose();
        }
        return true;
    }
}
