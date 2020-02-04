package owmii.powah.item;

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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.block.TileBase;
import owmii.lib.client.handler.IHudItem;
import owmii.lib.energy.SideConfig;
import owmii.lib.item.ItemBase;
import owmii.powah.api.wrench.IWrench;
import owmii.powah.api.wrench.WrenchMode;
import owmii.powah.block.cable.EnergyCableTile;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class WrenchItem extends ItemBase implements IHudItem, IWrench {
    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction side, Vec3d hit) {
        if (player.isShiftKeyDown()) return ActionResultType.PASS;

        TileEntity te = world.getTileEntity(pos);
        if (getWrenchMode(stack).config()) {
            if (te instanceof EnergyCableTile) {
                EnergyCableTile cable = (EnergyCableTile) te;
                if (stack.getItem() instanceof WrenchItem) {
                    Optional<Direction> sides = getHitSide(hit, pos);
                    boolean[] flag = {false};
                    sides.ifPresent(direction -> {
                        SideConfig config = cable.getSideConfig();
                        config.nextType(direction);
                        cable.sync(1);
                    });
                    return ActionResultType.SUCCESS;
                }
            } else if (te instanceof TileBase.EnergyStorage) {
                TileBase.EnergyStorage storage = (TileBase.EnergyStorage) te;
                if (storage.isEnergyPresent(side)) {
                    SideConfig config = storage.getSideConfig();
                    config.nextType(side);
                    storage.sync(1);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return super.onItemUseFirst(stack, world, pos, player, hand, side, hit);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (playerIn.isShiftKeyDown()) {
            nextWrenchMode(stack);
            playerIn.sendStatusMessage(new TranslationTextComponent("info.powah.wrench.mode." + getWrenchMode(stack).name().toLowerCase(), TextFormatting.YELLOW).applyTextStyle(TextFormatting.GRAY), true);
            return ActionResult.func_226249_b_(stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("info.powah.wrench.mode." + getWrenchMode(stack).name().toLowerCase(), TextFormatting.YELLOW).applyTextStyle(TextFormatting.GRAY));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            oneTimeInfo(player, stack, new TranslationTextComponent("info.powah.wrench.mode." + getWrenchMode(stack).name().toLowerCase(), TextFormatting.YELLOW).applyTextStyle(TextFormatting.GRAY));
        }
    }

    public Optional<Direction> getHitSide(Vec3d hit, BlockPos pos) {
        double x = hit.x - pos.getX();
        double y = hit.y - pos.getY();
        double z = hit.z - pos.getZ();
        if (x > 0.0D && x < 0.4D) {
            return Optional.of(Direction.WEST);
        } else if (x > 0.6D && x < 1.0D) {
            return Optional.of(Direction.EAST);
        } else if (z > 0.0D && z < 0.4D) {
            return Optional.of(Direction.NORTH);
        } else if (z > 0.6D && z < 1.0D) {
            return Optional.of(Direction.SOUTH);
        } else if (y > 0.6D && y < 1.0D) {
            return Optional.of(Direction.UP);
        } else if (y > 0.0D && y < 0.4D) {
            return Optional.of(Direction.DOWN);
        }
        return Optional.empty();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean renderHud(World world, BlockPos pos, PlayerEntity player, Hand hand, Direction direction, Vec3d hit) {
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

    private CompoundNBT getWrenchNBT(ItemStack stack) {
        return stack.getOrCreateChildTag("PowahWrenchNBT");
    }
}
