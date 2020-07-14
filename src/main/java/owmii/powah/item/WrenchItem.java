package owmii.powah.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.handler.IHudItem;
import owmii.lib.item.ItemBase;
import owmii.lib.logistics.SideConfig;
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
    public ActionResultType onItemUseFirst(ItemStack stack, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction side, Vector3d hit) {
        if (player.isSneaking()) return ActionResultType.PASS;
        TileEntity te = world.getTileEntity(pos);
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof IWrenchable) {
            if (((IWrenchable) state.getBlock()).onWrench(state, world, pos, player, hand, side, getWrenchMode(stack), hit)) {
                return ActionResultType.SUCCESS;
            }
        } else {
            if (!world.isRemote && getWrenchMode(stack).config()) {
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
                        return ActionResultType.SUCCESS;
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (playerIn.isSneaking()) {
            nextWrenchMode(stack);
            playerIn.sendStatusMessage(new TranslationTextComponent("info.powah.wrench.mode." + getWrenchMode(stack).name().toLowerCase(), TextFormatting.YELLOW).func_240701_a_(TextFormatting.GRAY), true);
            return ActionResult.resultSuccess(stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("info.powah.wrench.mode." + getWrenchMode(stack).name().toLowerCase(), TextFormatting.YELLOW).func_240699_a_(TextFormatting.GRAY));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            oneTimeInfo(player, stack, new TranslationTextComponent("info.powah.wrench.mode." + getWrenchMode(stack).name().toLowerCase(), TextFormatting.YELLOW).func_240699_a_(TextFormatting.GRAY));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean renderHud(World world, BlockPos pos, PlayerEntity player, Hand hand, Direction side, Vector3d hit) {
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
        CompoundNBT nbt = getWrenchNBT(stack);
        int i = nbt.getInt("WrenchMode") + 1;
        int j = WrenchMode.values().length - 1;
        nbt.putInt("WrenchMode", i > j ? 0 : i);
    }

    private void prevWrenchMode(ItemStack stack) {
        CompoundNBT nbt = getWrenchNBT(stack);
        int i = nbt.getInt("WrenchMode") - 1;
        int j = WrenchMode.values().length - 1;
        nbt.putInt("WrenchMode", i < j ? j : i);
    }

    @Override
    public WrenchMode getWrenchMode(ItemStack stack) {
        return WrenchMode.values()[getWrenchNBT(stack).getInt("WrenchMode")];
    }

    public CompoundNBT getWrenchNBT(ItemStack stack) {
        return stack.getOrCreateChildTag("PowahWrenchNBT");
    }
}
