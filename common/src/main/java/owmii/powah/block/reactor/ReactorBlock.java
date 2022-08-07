package owmii.powah.block.reactor;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import owmii.powah.EnvHandler;
import owmii.powah.Powah;
import owmii.powah.config.v2.types.GeneratorConfig;
import owmii.powah.lib.block.AbstractGeneratorBlock;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.client.wiki.page.panel.InfoBox;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.logistics.inventory.AbstractContainer;
import owmii.powah.lib.util.Util;
import owmii.powah.block.Tier;
import owmii.powah.inventory.ReactorContainer;
import owmii.powah.item.ReactorItem;

import javax.annotation.Nullable;
import java.util.List;

public class ReactorBlock extends AbstractGeneratorBlock<ReactorBlock> {
    public static final BooleanProperty CORE = BooleanProperty.create("core");

    public ReactorBlock(Properties properties, Tier variant) {
        super(properties.isValidSpawn((state, blockGetter, blockPos, entityType) -> false), variant);
        setStateProps(state -> state.setValue(CORE, false));
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable CreativeModeTab group) {
        return new ReactorItem(this, properties, group);
    }

    @Override
    public GeneratorConfig getConfig() {
        return Powah.config().generators.reactors;
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
        if (tileentity instanceof ReactorTile reactor) {
            if (reactor.isBuilt()) {
                if (EnvHandler.INSTANCE.interactWithTank(player, hand, reactor.getTank())) {
                    reactor.sync();
                    return InteractionResult.SUCCESS;
                }
                return super.use(state, world, pos, player, hand, result);
            }
        } else if (tileentity instanceof ReactorPartTile reactor) {
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CORE);
    }

    @Override
    public void additionalEnergyInfo(ItemStack stack, Energy.Item energy, List<Component> tooltip) {
        tooltip.add(Component.translatable("info.powah.generation.factor").withStyle(ChatFormatting.GRAY).append(Text.COLON).append(Component.literal(Util.numFormat(getConfig().getGeneration(this.variant))).append(Component.translatable("info.lollipop.fe.pet.tick")).withStyle(ChatFormatting.DARK_GRAY)));
    }

    @Override
    public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
        Energy.ifPresent(stack, energy -> {
            box.set(Component.translatable("info.lollipop.capacity"), Component.translatable("info.lollipop.fe", Util.addCommas(energy.getCapacity())));
            box.set(Component.translatable("info.powah.generation.factor"), Component.translatable("info.lollipop.fe.pet.tick", Util.addCommas(getConfig().getGeneration(this.variant))));
            box.set(Component.translatable("info.lollipop.max.extract"), Component.translatable("info.lollipop.fe.pet.tick", Util.addCommas(energy.getMaxExtract())));
        });
        return box;
    }
}
