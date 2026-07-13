package owmii.powah.block.reactor;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
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
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jspecify.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.inventory.ReactorMenu;
import owmii.powah.item.ReactorItem;
import owmii.powah.lib.block.PowahBaseBlockEntity;
import owmii.powah.lib.block.PowahBaseGeneratorBlock;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.item.EnergyBlockItem;
import owmii.powah.lib.logistics.fluid.Tank;
import owmii.powah.lib.logistics.inventory.BaseMenu;
import owmii.powah.util.Util;

public class ReactorBlock extends PowahBaseGeneratorBlock<ReactorBlock> {
    public static final BooleanProperty CORE = BooleanProperty.create("core");

    public ReactorBlock(Properties properties, Tier tier) {
        super(properties.isValidSpawn((_, _, _, _) -> false), Powah.config().generators.reactors, tier);
        setStateProps(state -> state.setValue(CORE, false));
    }

    @Override
    public EnergyBlockItem getBlockItem(Item.Properties properties, @Nullable ResourceKey<CreativeModeTab> group) {
        return new ReactorItem(this, properties, group);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(CORE)) {
            return new ReactorBlockEntity(pos, state);
        }
        return new ReactorPartBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return context.getPlayer() != null ? defaultBlockState().setValue(CORE, true) : super.getStateForPlacement(context);

    }

    @Override
    protected InteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
            BlockHitResult pHitResult) {
        BlockEntity tileentity = pLevel.getBlockEntity(pPos);
        if (tileentity instanceof ReactorPartBlockEntity reactor) {
            if (reactor.isBuilt() && reactor.core().isPresent()) {
                return reactor.getBlock().useItemOn(pStack, pState, pLevel, reactor.getCorePos(), pPlayer, pHand, pHitResult);
            }
        } else if (tileentity instanceof ReactorBlockEntity reactor) {
            if (reactor.isBuilt()) {
                Tank tank = reactor.getTank();
                if (FluidUtil.interactWithFluidHandler(pPlayer, pHand, pPos, tank, null)) {
                    reactor.sync();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult result) {
        BlockEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof ReactorPartBlockEntity reactor) {
            if (reactor.isBuilt() && reactor.core().isPresent()) {
                return reactor.getBlock().useWithoutItem(state, world, reactor.getCorePos(), player, result);
            }
        }
        return super.useWithoutItem(state, world, pos, player, result);
    }

    @Nullable
    @Override
    public <T extends PowahBaseBlockEntity> BaseMenu getContainer(int id, Inventory inventory, PowahBaseBlockEntity te,
            BlockHitResult result) {
        if (te instanceof ReactorBlockEntity) {
            return new ReactorMenu(id, inventory, (ReactorBlockEntity) te);
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CORE);
    }

    @Override
    public void additionalEnergyInfo(ItemStack stack, EnergyHandler energy, Consumer<Component> tooltip) {
        tooltip.accept(Component.translatable("info.powah.generation.factor").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                .append(Component.translatable("info.lollipop.fe.pet.tick", Util.numFormat(getEnergyGeneration()))
                        .withStyle(ChatFormatting.DARK_GRAY)));
    }
}
