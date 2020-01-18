package owmii.powah.block.generator.reactor;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import owmii.lib.block.TileBase;
import owmii.lib.inventory.ContainerBase;
import owmii.lib.item.BlockItemBase;
import owmii.lib.util.Energy;
import owmii.lib.util.Text;
import owmii.powah.block.generator.GeneratorBlock;
import owmii.powah.inventory.IContainers;
import owmii.powah.inventory.ReactorContainer;
import owmii.powah.item.ReactorItem;

import javax.annotation.Nullable;
import java.util.List;

public class ReactorBlock extends GeneratorBlock {
    public ReactorBlock(Properties properties, int capacity, int transfer, int perTick) {
        super(properties, capacity, transfer, perTick);
    }

    @Override
    public BlockItemBase getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return new ReactorItem(this, properties, group);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ReactorTile(this.capacity, this.maxExtract, this.perTick);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof ReactorTile) {
            ReactorTile tile = (ReactorTile) tileentity;
            tile.setCore(true);
            tile.getSideConfig().init();
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof ReactorTile) {
            ReactorTile reactor = (ReactorTile) tileentity;
            if (reactor.isBuilt()) {
                if (reactor.isCore()) {
                    boolean result = FluidUtil.interactWithFluidHandler(player, hand, reactor.tank);
                    if (result) {
                        reactor.markDirtyAndSync();
                        return true;
                    }
                    return super.onBlockActivated(state, world, pos, player, hand, blockRayTraceResult);
                } else if (reactor.core().isPresent()) {
                    return reactor.getBlock().onBlockActivated(state, world, reactor.getCorePos(), player, hand, blockRayTraceResult);
                }
            }

        }
        return false;
    }

    @Nullable
    @Override
    public ContainerBase getContainer(int id, PlayerInventory playerInventory, TileBase inv) {
        if (inv instanceof ReactorTile) {
            return new ReactorContainer(IContainers.REACTOR, id, playerInventory, (ReactorTile) inv);
        }
        return null;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof ReactorTile) {
            ReactorTile tile = (ReactorTile) tileentity;
            tile.demolish();
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public int stackSize() {
        return 64;
    }

    @Override
    public void tooltip(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Energy.getForgeEnergy(stack).ifPresent(storage -> {
            if (storage.getMaxEnergyStored() > 0) {
                tooltip.add(new TranslationTextComponent("info.powah.capacity", TextFormatting.DARK_GRAY + Text.numFormat(storage.getMaxEnergyStored())).applyTextStyle(TextFormatting.GRAY));
            }
            if (getBlock() instanceof GeneratorBlock) {
                tooltip.add(new TranslationTextComponent("info.powah.generation.factor", TextFormatting.DARK_GRAY + Text.numFormat(((GeneratorBlock) getBlock()).perTick())).applyTextStyle(TextFormatting.GRAY));
            }
            int maxIn = getMaxReceive();
            int maxOut = getMaxExtract();
            tooltip.add(new TranslationTextComponent("info.powah.max.io", TextFormatting.DARK_GRAY + (maxIn == maxOut ? Text.numFormat(maxOut) : maxIn == 0 || maxOut == 0 ? Text.numFormat(Math.max(maxIn, maxOut)) : (Text.numFormat(maxIn) + "/" + Text.numFormat(maxOut)))).applyTextStyle(TextFormatting.GRAY));
            tooltip.add(new StringTextComponent(""));
        });
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    protected boolean hasLitProp() {
        return false;
    }
}
