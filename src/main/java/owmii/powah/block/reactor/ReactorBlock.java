package owmii.powah.block.reactor;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;
import owmii.lib.block.AbstractGeneratorBlock;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.client.util.Text;
import owmii.lib.client.wiki.page.panel.InfoBox;
import owmii.lib.item.EnergyBlockItem;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.logistics.inventory.AbstractContainer;
import owmii.lib.util.Util;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.client.render.tile.ReactorRenderer;
import owmii.powah.config.Configs;
import owmii.powah.config.generator.ReactorConfig;
import owmii.powah.inventory.ReactorContainer;
import owmii.powah.item.ReactorItem;

import javax.annotation.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;

public class ReactorBlock extends AbstractGeneratorBlock<Tier, ReactorConfig, ReactorBlock> {
    public static final BooleanProperty CORE = BooleanProperty.create("core");

    public ReactorBlock(Properties properties, Tier variant) {
        super(properties, variant);
        setStateProps(state -> state.setValue(CORE, false));
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable CreativeModeTab group) {
        return new ReactorItem(this, properties, group);
    }

    @Override
    public ReactorConfig getConfig() {
        return Configs.REACTOR;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(CORE)) {
            return new ReactorTile(pos, state, this.variant);
        }
        return new ReactorPartTile(pos, state, this.variant);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return context.getPlayer() != null ? defaultBlockState().setValue(CORE, true) : super.getStateForPlacement(context);

    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof ReactorTile) {
            ReactorTile reactor = (ReactorTile) tileentity;
            if (reactor.isBuilt()) {
                if (FluidUtil.interactWithFluidHandler(player, hand, reactor.getTank())) {
                    reactor.sync();
                    return InteractionResult.SUCCESS;
                }
                return super.use(state, world, pos, player, hand, result);
            }
        } else if (tileentity instanceof ReactorPartTile) {
            ReactorPartTile reactor = (ReactorPartTile) tileentity;
            if (reactor.isBuilt() && reactor.core().isPresent()) {
                return reactor.getBlock().use(state, world, reactor.getCorePos(), player, hand, result);
            }
        }
        return super.use(state, world, pos, player, hand, result);
    }

    @Nullable
    @Override
    public <T extends AbstractTileEntity> AbstractContainer getContainer(int id, Inventory inventory, AbstractTileEntity te, BlockHitResult result) {
        if (te instanceof ReactorTile) {
            return new ReactorContainer(id, inventory, (ReactorTile) te);
        }
        return null;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof ReactorTile) {
            ReactorTile tile = (ReactorTile) tileentity;
            tile.demolish(world);
        } else if (tileentity instanceof ReactorPartTile) {
            ReactorPartTile tile = (ReactorPartTile) tileentity;
            tile.demolish(world);
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CORE);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderByItem(ItemStack stack, PoseStack matrix, MultiBufferSource rtb, int light, int ov) {
        matrix.pushPose();
        matrix.translate(0.5D, 0.5D, 0.5D);
        matrix.scale(1.0F, -1.0F, -1.0F);
        VertexConsumer buffer = rtb.getBuffer(ReactorRenderer.CUBE_MODEL.renderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor_block_" + getVariant().getName() + ".png")));
        ReactorRenderer.CUBE_MODEL.renderToBuffer(matrix, buffer, light, ov, 1.0F, 1.0F, 1.0F, 1.0F);
        matrix.popPose();
    }

    @Override
    public void additionalEnergyInfo(ItemStack stack, Energy.Item energy, List<Component> tooltip) {
        tooltip.add(new TranslatableComponent("info.powah.generation.factor").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(new TextComponent(Util.numFormat(getConfig().getGeneration(this.variant))).append(new TranslatableComponent("info.lollipop.fe.pet.tick")).withStyle(ChatFormatting.DARK_GRAY)));
    }

    @Override
    public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
        Energy.ifPresent(stack, storage -> {
            if (storage instanceof Energy.Item) {
                Energy.Item energy = (Energy.Item) storage;
                box.set(new TranslatableComponent("info.lollipop.capacity"), new TranslatableComponent("info.lollipop.fe", Util.addCommas(energy.getCapacity())));
                box.set(new TranslatableComponent("info.powah.generation.factor"), new TranslatableComponent("info.lollipop.fe.pet.tick", Util.addCommas(getConfig().getGeneration(this.variant))));
                box.set(new TranslatableComponent("info.lollipop.max.extract"), new TranslatableComponent("info.lollipop.fe.pet.tick", Util.addCommas(energy.getMaxExtract())));
            }
        });
        return box;
    }
}
