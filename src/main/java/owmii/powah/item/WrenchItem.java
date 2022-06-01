package owmii.powah.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.handler.IHudItem;
import owmii.lib.item.ItemBase;
import owmii.lib.logistics.energy.SideConfig;
import owmii.powah.api.wrench.IWrench;
import owmii.powah.api.wrench.IWrenchable;
import owmii.powah.api.wrench.WrenchMode;
import owmii.powah.block.cable.CableBlock;
import owmii.powah.block.cable.CableTile;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class WrenchItem extends ItemBase implements IHudItem, IWrench {
    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, Level world, BlockPos pos, Player player, InteractionHand hand, Direction side, Vec3 hit) {
        if (player.isShiftKeyDown()) return InteractionResult.PASS;
        BlockEntity te = world.getBlockEntity(pos);
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof IWrenchable) {
            if (((IWrenchable) state.getBlock()).onWrench(state, world, pos, player, hand, side, getWrenchMode(stack), hit)) {
                return InteractionResult.SUCCESS;
            }
        } else {
            if (!world.isClientSide && getWrenchMode(stack).config()) {
                if (te instanceof CableTile) {
                    CableTile cable = (CableTile) te;
                    if (stack.getItem() instanceof WrenchItem) {
                        Optional<Direction> sides = CableBlock.getHitSide(hit, pos);
                        boolean[] flag = {false};
                        sides.ifPresent(direction -> {
                            SideConfig config = cable.getSideConfig();
                            config.nextType(direction);
                            cable.sync();
                        });
                        return InteractionResult.SUCCESS;
                    }
                }
//                else if (te instanceof TileBase.EnergyStorage) {
//                    TileBase.EnergyStorage storage = (TileBase.EnergyStorage) te;
//                    if (storage.isEnergyPresent(side)) {
//                        SideConfig config = storage.getSideConfig();
//                        config.nextType(side);
//                        storage.markDirtyAndSync();
//                        return ActionResultType.SUCCESS;
//                    }
//                }
            }
        }
        return super.onItemUseFirst(stack, world, pos, player, hand, side, hit);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (playerIn.isShiftKeyDown()) {
            nextWrenchMode(stack);
            playerIn.displayClientMessage(new TranslatableComponent("info.powah.wrench.mode." + getWrenchMode(stack).name().toLowerCase(), ChatFormatting.YELLOW).withStyle(ChatFormatting.GRAY), true);
            return InteractionResultHolder.success(stack);
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("info.powah.wrench.mode." + getWrenchMode(stack).name().toLowerCase(), ChatFormatting.YELLOW).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof Player) {
            Player player = (Player) entityIn;
            oneTimeInfo(player, stack, new TranslatableComponent("info.powah.wrench.mode." + getWrenchMode(stack).name().toLowerCase(), ChatFormatting.YELLOW).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean renderHud(Level world, BlockPos pos, Player player, InteractionHand hand, Direction side, Vec3 hit) {
        return false;
    }

    private boolean changeWrenchMode(ItemStack stack, boolean next) {
        if (stack.getItem() instanceof IWrench)
            if (next) {
                nextWrenchMode(stack);
                return true;
            } else {
                prevWrenchMode(stack);
                return true;
            }
        return false;
    }

    private void nextWrenchMode(ItemStack stack) {
        CompoundTag nbt = getWrenchNBT(stack);
        int i = nbt.getInt("WrenchMode") + 1;
        int j = WrenchMode.values().length - 1;
        nbt.putInt("WrenchMode", i > j ? 0 : i);
    }

    private void prevWrenchMode(ItemStack stack) {
        CompoundTag nbt = getWrenchNBT(stack);
        int i = nbt.getInt("WrenchMode") - 1;
        int j = WrenchMode.values().length - 1;
        nbt.putInt("WrenchMode", i < j ? j : i);
    }

    @Override
    public WrenchMode getWrenchMode(ItemStack stack) {
        return WrenchMode.values()[getWrenchNBT(stack).getInt("WrenchMode")];
    }

    public CompoundTag getWrenchNBT(ItemStack stack) {
        return stack.getOrCreateTagElement("PowahWrenchNBT");
    }
}
