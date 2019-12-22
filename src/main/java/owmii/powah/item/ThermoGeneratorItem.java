package owmii.powah.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.PowahBlock;

import javax.annotation.Nullable;

public class ThermoGeneratorItem extends PowahBlockItem {
    public ThermoGeneratorItem(PowahBlock block, Properties properties, @Nullable ItemGroup group) {
        super(block, properties, group);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ActionResultType actionresulttype = onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType();
        return actionresulttype != ActionResultType.SUCCESS ? tryPlace(new BlockItemUseContext(context)) : actionresulttype;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.ANY);
        if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
            return new ActionResult<>(ActionResultType.PASS, itemStack);
        } else {
            if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
                BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) raytraceresult;
                BlockPos blockPos = blockraytraceresult.getPos();
                Direction direction = blockraytraceresult.getFace();
                if (!worldIn.isBlockModifiable(playerIn, blockPos) || !playerIn.canPlayerEdit(blockPos.offset(direction), direction, itemStack)) {
                    return new ActionResult<>(ActionResultType.FAIL, itemStack);
                }
                BlockState blockstate = this.getStateForPlacement(new BlockItemUseContext(new ItemUseContext(playerIn, handIn, blockraytraceresult)));

                if (blockstate != null) {
                    BlockPos blockPos1 = blockPos.up();
                    BlockState blockState = worldIn.getBlockState(blockPos);
                    Material material = blockState.getMaterial();
                    IFluidState ifluidstate = worldIn.getFluidState(blockPos);
                    if (PowahAPI.HEAT_BLOCKS.containsKey(blockState.getBlock()) && worldIn.isAirBlock(blockPos1)) {
                        worldIn.setBlockState(blockPos1, getBlock().getDefaultState(), 11);

                        BlockState blockState1 = worldIn.getBlockState(blockPos1);
                        if (blockState1.getBlock() == blockstate.getBlock()) {
                            blockState1 = func_219985_a(blockPos1, worldIn, itemStack, blockstate);
                            onBlockPlaced(blockPos1, worldIn, playerIn, itemStack, blockState1);
                            blockState1.getBlock().onBlockPlacedBy(worldIn, blockPos1, blockState1, playerIn, itemStack);
                            if (playerIn instanceof ServerPlayerEntity) {
                                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerIn, blockPos, itemStack);
                            }
                        }

                        if (!playerIn.abilities.isCreativeMode) {
                            itemStack.shrink(1);
                        }

                        playerIn.addStat(Stats.ITEM_USED.get(this));
                        worldIn.playSound(playerIn, blockPos, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
                    }
                }
            }

            return new ActionResult<>(ActionResultType.FAIL, itemStack);
        }
    }

    private BlockState func_219985_a(BlockPos pos, World world, ItemStack stack, BlockState state) {
        BlockState blockState = state;
        CompoundNBT compoundNBT = stack.getTag();
        if (compoundNBT != null) {
            CompoundNBT compoundNBT1 = compoundNBT.getCompound("BlockStateTag");
            StateContainer<Block, BlockState> stateContainer = state.getBlock().getStateContainer();
            for (String s : compoundNBT1.keySet()) {
                IProperty<?> property = stateContainer.getProperty(s);
                if (property != null) {
                    INBT j = compoundNBT1.get(s);
                    if (j != null) {
                        String s1 = j.getString();
                        blockState = func_219988_a(blockState, property, s1);
                    }
                }
            }
        }

        if (blockState != state) {
            world.setBlockState(pos, blockState, 2);
        }

        return blockState;
    }

    private static <T extends Comparable<T>> BlockState func_219988_a(BlockState state, IProperty<T> property, String s) {
        return property.parseValue(s).map((p_219986_2_) -> {
            return state.with(property, p_219986_2_);
        }).orElse(state);
    }
}
