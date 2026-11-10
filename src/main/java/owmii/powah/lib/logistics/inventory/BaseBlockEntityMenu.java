package owmii.powah.lib.logistics.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.jspecify.annotations.Nullable;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.PowahBaseBlockEntity;
import owmii.powah.network.packet.InteractWithTankPacket;

public abstract class BaseBlockEntityMenu<T extends PowahBaseBlockEntity<?> & IInventoryHolder> extends BaseMenu {
    public final T blockEntity;

    public BaseBlockEntityMenu(@Nullable MenuType<?> containerType, int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(containerType, id, inventory, getInventory(inventory.player, buffer.readBlockPos()));
    }

    public BaseBlockEntityMenu(@Nullable MenuType<?> type, int id, Inventory inventory, T blockEntity) {
        super(type, id, inventory);
        this.blockEntity = blockEntity;
        init(inventory, blockEntity);
        this.blockEntity.setContainerOpen(true);
    }

    @Override
    protected final void init(Inventory inventory) {
        super.init(inventory);
    }

    protected void init(Inventory inventory, T te) {

    }

    @SuppressWarnings("unchecked")
    protected static <T extends PowahBaseBlockEntity<?>> T getInventory(Player player, BlockPos pos) {
        BlockEntity tile = player.level().getBlockEntity(pos);
        if (tile instanceof PowahBaseBlockEntity<?>)
            return (T) tile;
        // What the hell is this?
        return (T) new PowahBaseBlockEntity<>(BlockEntityTypes.SIGN, pos, Blocks.AIR.defaultBlockState());
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.blockEntity.setContainerOpen(false);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();
            int size = this.blockEntity.getInventory().size();
            if (index < size) {
                if (!moveItemStackTo(stack1, size, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack1, 0, size, false)) {
                return ItemStack.EMPTY;
            }
            if (stack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
                slot.onTake(this.player, stack);
            } else {
                slot.setChanged();
            }
        }
        return stack;
    }

    public void interactWithTank(boolean drain) {
        var level = player.level();
        if (level.isClientSide()) {
            ClientPacketDistributor.sendToServer(new InteractWithTankPacket(containerId, drain));
            return;
        }

        var tank = blockEntity.getTank();
        if (tank.getCapacity() == 0) {
            return;
        }

        var carriedTank = ItemAccess.forPlayerCursor(player, this).getCapability(Capabilities.Fluid.ITEM);
        if (carriedTank == null) {
            return;
        }

        FluidStack moved;
        if (drain) {
            moved = moveWithSound(tank, carriedTank, level, player, true);
        } else {
            moved = moveWithSound(carriedTank, tank, level, player, false);

            // If that didn't succeed, but the held item is *empty*, try filling it
            if (moved.isEmpty() && carriedTank.size() > 0 && carriedTank.getAmountAsInt(0) == 0) {
                moved = moveWithSound(tank, carriedTank, level, player, true);
            }
        }

        // Sync immediately when a user interacts with the block entity
        if (!moved.isEmpty()) {
            this.blockEntity.sync();
        }
    }

    private static FluidStack moveWithSound(ResourceHandler<FluidResource> from, ResourceHandler<FluidResource> to, Level level, Player player,
            boolean pickup) {
        var moved = ResourceHandlerUtil.moveFirst(from, to, fr -> true, Integer.MAX_VALUE, null);
        if (moved == null) {
            return FluidStack.EMPTY;
        }

        var stack = moved.resource().toStack(moved.amount());
        playSoundAndGameEvent(stack, level, player, pickup);
        return stack;
    }

    private static void playSoundAndGameEvent(FluidStack stack, Level level, Player player, boolean pickup) {
        Vec3 position = new Vec3(player.getX(), player.getY() + 0.5, player.getZ());

        SoundEvent soundEvent = stack.getFluidType().getSound(stack, pickup ? SoundActions.BUCKET_FILL : SoundActions.BUCKET_EMPTY);
        if (soundEvent != null) {
            level.playSound(null, position.x, position.y, position.z, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        level.gameEvent(player, pickup ? GameEvent.FLUID_PICKUP : GameEvent.FLUID_PLACE, position);
    }
}
