package owmii.powah.block.reactor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;
import owmii.lib.block.AbstractGeneratorBlock;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.logistics.inventory.AbstractContainer;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.client.render.tile.ReactorRenderer;
import owmii.powah.config.Configs;
import owmii.powah.config.generator.ReactorConfig;
import owmii.powah.inventory.ReactorContainer;
import owmii.powah.item.ReactorItem;

import javax.annotation.Nullable;

public class ReactorBlock extends AbstractGeneratorBlock<Tier, ReactorConfig> {
    public static final BooleanProperty CORE = BooleanProperty.create("core");

    public ReactorBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setStateProps(state -> state.with(CORE, false));
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ItemGroup group) {
        return new ReactorItem(this, properties, group);
    }

    @Override
    public ReactorConfig getConfig() {
        return Configs.REACTOR;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (state.get(CORE)) {
            return new ReactorTile(this.variant);
        }
        return new ReactorPartTile(this.variant);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return context.getPlayer() != null ? getDefaultState().with(CORE, true) : super.getStateForPlacement(context);

    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof ReactorTile) {
            ReactorTile reactor = (ReactorTile) tileentity;
            if (reactor.isBuilt()) {
                if (FluidUtil.interactWithFluidHandler(player, hand, reactor.getTank())) {
                    reactor.sync();
                    return ActionResultType.SUCCESS;
                }
                return super.onBlockActivated(state, world, pos, player, hand, result);
            }
        } else if (tileentity instanceof ReactorPartTile) {
            ReactorPartTile reactor = (ReactorPartTile) tileentity;
            if (reactor.isBuilt() && reactor.core().isPresent()) {
                return reactor.getBlock().onBlockActivated(state, world, reactor.getCorePos(), player, hand, result);
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, result);
    }

    @Nullable
    @Override
    public <T extends AbstractTileEntity> AbstractContainer getContainer(int id, PlayerInventory inventory, AbstractTileEntity te, BlockRayTraceResult result) {
        if (te instanceof ReactorTile) {
            return new ReactorContainer(id, inventory, (ReactorTile) te);
        }
        return null;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileentity = world.getTileEntity(pos);
        if (tileentity instanceof ReactorTile) {
            ReactorTile tile = (ReactorTile) tileentity;
            tile.demolish(world);
        } else if (tileentity instanceof ReactorPartTile) {
            ReactorPartTile tile = (ReactorPartTile) tileentity;
            tile.demolish(world);
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return false;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(CORE);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderByItem(ItemStack stack, MatrixStack matrix, IRenderTypeBuffer rtb, int light, int ov) {
        matrix.push();
        matrix.translate(0.5D, 0.5D, 0.5D);
        matrix.scale(1.0F, -1.0F, -1.0F);
        IVertexBuilder buffer = rtb.getBuffer(ReactorRenderer.CUBE_MODEL.getRenderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor_block_" + getVariant().getName() + ".png")));
        ReactorRenderer.CUBE_MODEL.render(matrix, buffer, light, ov, 1.0F, 1.0F, 1.0F, 1.0F);
        matrix.pop();
    }
}
