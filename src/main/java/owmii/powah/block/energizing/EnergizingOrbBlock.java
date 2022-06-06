package owmii.powah.block.energizing;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

import static net.minecraft.world.phys.shapes.Shapes.join;

public class EnergizingOrbBlock extends AbstractBlock<IVariant.Single, EnergizingOrbBlock> implements SimpleWaterloggedBlock, IWrenchable, IHud {

    public EnergizingOrbBlock(Properties properties) {
        super(properties);
        setStateProps(state -> state.setValue(BlockStateProperties.FACING, Direction.DOWN));
        this.shapes.put(Direction.UP, join(box(3.5D, 11.0D, 3.5D, 12.5D, 1.77D, 12.5D), box(2.5D, 15.0D, 2.5D, 13.5D, 16.0D, 13.5D), BooleanOp.OR));
        this.shapes.put(Direction.DOWN, join(box(3.5D, 14.23D, 3.5D, 12.5D, 5.0D, 12.5D), box(2.5D, 0.0D, 2.5D, 13.5D, 1.0D, 13.5D), BooleanOp.OR));
        this.shapes.put(Direction.NORTH, join(box(3.5D, 3.5D, 14.23D, 12.5D, 12.5D, 5.0D), box(2.5D, 2.5D, 0.0D, 13.5D, 13.5D, 1.0D), BooleanOp.OR));
        this.shapes.put(Direction.SOUTH, join(box(3.5D, 3.5D, 11.0D, 12.5D, 12.5D, 1.77D), box(2.5D, 2.5D, 15.0D, 13.5D, 13.5D, 16.0D), BooleanOp.OR));
        this.shapes.put(Direction.WEST, join(box(14.23D, 3.5D, 3.5D, 5.0D, 12.5D, 12.5D), box(0.0D, 2.5D, 2.5D, 1.0D, 13.5D, 13.5D), BooleanOp.OR));
        this.shapes.put(Direction.EAST, join(box(11.0D, 3.5D, 3.5D, 1.77D, 12.5D, 12.5D), box(15.0D, 2.5D, 2.5D, 16.0D, 13.5D, 13.5D), BooleanOp.OR));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergizingOrbTile(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack held = player.getItemInHand(hand);
        BlockEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof EnergizingOrbTile) {
            EnergizingOrbTile orb = (EnergizingOrbTile) tileentity;
            Inventory inv = orb.getInventory();
            ItemStack output = inv.getStackInSlot(0);
            ItemStack off = player.getOffhandItem();
            //  if (!(off.getItem() instanceof IWrench && ((IWrench) off.getItem()).getWrenchMode(off).link())) {
            if (held.isEmpty() || !output.isEmpty()) {
                if (!world.isClientSide) {
                    ItemHandlerHelper.giveItemToPlayer(player, inv.removeNext());
                }
                return InteractionResult.SUCCESS;
            } else {
                // if (!world.isRemote) {
                ItemStack copy = held.copy();
                copy.setCount(1);
                if (!inv.addNext(copy).isEmpty() && !player.isCreative()) {
                    held.shrink(1);
                }
                // }
                return InteractionResult.SUCCESS;
            }
            // }
        }
        return super.use(state, world, pos, player, hand, result);
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
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        BlockEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof EnergizingOrbTile) {
            EnergizingOrbTile orb = (EnergizingOrbTile) tileentity;
            return orb.getInventory().getNonEmptyStacks().size();
        }
        return super.getAnalogOutputSignal(state, world, pos);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        int range = Configs.ENERGIZING.range.get();
        List<BlockPos> list = BlockPos.betweenClosedStream(pos.offset(-range, -range, -range), pos.offset(range, range, range))
                .map(BlockPos::immutable)
                .filter(pos1 -> !pos.equals(pos1))
                .collect(Collectors.toList());

        list.forEach(pos1 -> {
            BlockEntity tileEntity1 = worldIn.getBlockEntity(pos1);
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
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    public void search(Level worldIn, BlockPos pos) {
        int range = Configs.ENERGIZING.range.get();
        List<BlockPos> list = BlockPos.betweenClosedStream(pos.offset(-range, -range, -range), pos.offset(range, range, range)).map(BlockPos::immutable).filter(pos1 -> !pos.equals(pos1)).collect(Collectors.toList());
        list.stream().filter(p -> worldIn.isLoaded(pos)).forEach(pos1 -> {
            BlockEntity tileEntity1 = worldIn.getBlockEntity(pos1);
            if (tileEntity1 instanceof EnergizingRodTile) {
                if (!((EnergizingRodTile) tileEntity1).hasOrb()) {
                    ((EnergizingRodTile) tileEntity1).setOrbPos(pos);
                }
            }
        });
    }

    @Override
    public boolean onWrench(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, Direction side, WrenchMode mode, Vec3 hit) {
        if (mode.link()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof WrenchItem) {
                WrenchItem wrench = (WrenchItem) stack.getItem();
                BlockEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity instanceof EnergizingOrbTile) {
                    EnergizingOrbTile orb = (EnergizingOrbTile) tileEntity;
                    CompoundTag nbt = wrench.getWrenchNBT(stack);
                    if (nbt.contains("RodPos", Tag.TAG_COMPOUND)) {
                        BlockPos rodPos = NbtUtils.readBlockPos(nbt.getCompound("RodPos"));
                        BlockEntity tileEntity1 = world.getBlockEntity(rodPos);
                        if (tileEntity1 instanceof EnergizingRodTile) {
                            EnergizingRodTile rod = (EnergizingRodTile) tileEntity1;
                            V3d v3d = V3d.from(rodPos);
                            if ((int) v3d.distance(pos) <= Configs.ENERGIZING.range.get()) {
                                rod.setOrbPos(pos);
                                player.displayClientMessage(new TranslatableComponent("chat.powah.wrench.link.done").withStyle(ChatFormatting.GOLD), true);
                            } else {
                                player.displayClientMessage(new TranslatableComponent("chat.powah.wrench.link.fail").withStyle(ChatFormatting.RED), true);
                            }
                        }
                        nbt.remove("RodPos");
                    } else {
                        nbt.put("OrbPos", NbtUtils.writeBlockPos(pos));
                        player.displayClientMessage(new TranslatableComponent("chat.powah.wrench.link.start").withStyle(ChatFormatting.YELLOW), true);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean renderHud(PoseStack matrix, BlockState state, Level world, BlockPos pos, Player player, BlockHitResult result, @Nullable BlockEntity te) {
        if (te instanceof EnergizingOrbTile) {
            EnergizingOrbTile orb = (EnergizingOrbTile) te;
            if (orb.getBuffer().getCapacity() > 0) {
                RenderSystem.getModelViewStack().pushPose();
                RenderSystem.enableBlend();
                Minecraft mc = Minecraft.getInstance();
                Font font = mc.font;
                int x = mc.getWindow().getGuiScaledWidth() / 2;
                int y = mc.getWindow().getGuiScaledHeight();
                String s = "" + ChatFormatting.GREEN + orb.getBuffer().getPercent() + "%";
                String s1 = ChatFormatting.GRAY + I18n.get("info.lollipop.fe.stored", Util.addCommas(orb.getBuffer().getEnergyStored()), Util.numFormat(orb.getBuffer().getCapacity()));
                font.drawShadow(matrix, s, x - (font.width(s) / 2.0f), y - 90, 0xffffff);
                font.drawShadow(matrix, s1, x - (font.width(s1) / 2.0f), y - 75, 0xffffff);
                RenderSystem.disableBlend();
                RenderSystem.getModelViewStack().popPose();
            }
        }
        return true;
    }
}
