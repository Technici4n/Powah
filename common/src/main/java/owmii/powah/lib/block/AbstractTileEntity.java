package owmii.powah.lib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import owmii.powah.lib.logistics.IRedstoneInteract;
import owmii.powah.lib.logistics.Redstone;
import owmii.powah.lib.logistics.fluid.Tank;
import owmii.powah.lib.logistics.inventory.Inventory;
import owmii.powah.lib.registry.IVariant;
import owmii.powah.lib.util.NBT;
import owmii.powah.lib.util.Stack;

import javax.annotation.Nullable;

@SuppressWarnings("unchecked")
public class AbstractTileEntity<V extends IVariant, B extends AbstractBlock<V, B>> extends BlockEntity implements IBlockEntity, IRedstoneInteract {
    /**
     * Used when this is instance of {@link IInventoryHolder}
     **/
    protected final Inventory inv = Inventory.createBlank();
    /**
     * Used when this is instance of {@link ITankHolder}
     **/
    protected final Tank tank = new Tank(0);

    protected V variant;
    protected boolean isContainerOpen;
    /**
     * Used when this is instance of {@link IRedstoneInteract}
     **/
    private Redstone redstone = Redstone.IGNORE;

    public AbstractTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        this(type, pos, state, IVariant.getEmpty());
        this.tank.validate(stack -> true);
    }

    public AbstractTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, V variant) {
        super(type, pos, state);
        this.variant = variant;
        if (this instanceof IInventoryHolder) {
            this.inv.setTile((IInventoryHolder) this);
        }
    }

    public B getBlock() {
        return (B) getBlockState().getBlock();
    }

    public V getVariant() {
        return this.variant;
    }

    @Override
    public final void load(CompoundTag tag) {
        super.load(tag);
        readSync(tag);

        if (!tag.contains("#c")) { // Server only...
            loadServerOnly(tag);
        }
    }

    @Override
    protected final void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        writeSync(tag);
        saveServerOnly(tag);
    }

    @Override
    public final CompoundTag getUpdateTag() {
        var tag = saveWithoutMetadata();
        tag.putBoolean("#c", true); // mark client tag
        return tag;
    }

    @Nullable
    @Override
    public final ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    protected void loadServerOnly(CompoundTag compound) {
    }

    protected CompoundTag saveServerOnly(CompoundTag compound) {
        return compound;
    }

    protected void readSync(CompoundTag nbt) {
        if (!this.variant.isEmpty() && nbt.contains("variant", 3)) {
            this.variant = (V) this.variant.read(nbt, "variant");
        }
        if (this instanceof IInventoryHolder && !keepInventory()) {
            this.inv.deserializeNBT(nbt);
        }
        if (this instanceof ITankHolder tankHolder) {
            if (!tankHolder.keepFluid()) {
                this.tank.readFromNBT(nbt);
            }
        }
        this.redstone = Redstone.values()[nbt.getInt("redstone_mode")];
        readStorable(nbt);
    }

    protected CompoundTag writeSync(CompoundTag nbt) {
        if (!this.variant.isEmpty()) {
            this.variant.write(nbt, (Enum<?>) this.variant, "variant");
        }
        if (this instanceof IInventoryHolder && !keepInventory()) {
            nbt.merge(this.inv.serializeNBT());
        }
        if (this instanceof ITankHolder tankHolder) {
            if (!tankHolder.keepFluid()) {
                this.tank.writeToNBT(nbt);
            }
        }
        nbt.putInt("redstone_mode", this.redstone.ordinal());
        return writeStorable(nbt);
    }

    public void readStorable(CompoundTag nbt) {
        if (this instanceof IInventoryHolder && keepInventory()) {
            this.inv.deserializeNBT(nbt);
        }
        if (this instanceof ITankHolder tankHolder) {
            if (tankHolder.keepFluid()) {
                this.tank.readFromNBT(nbt);
            }
        }
    }

    public CompoundTag writeStorable(CompoundTag nbt) {
        if (this instanceof IInventoryHolder && keepInventory()) {
            nbt.merge(this.inv.serializeNBT());
        }
        if (this instanceof ITankHolder tankHolder) {
            if (tankHolder.keepFluid()) {
                this.tank.writeToNBT(nbt);
            }
        }
        return nbt;
    }

    @Override
    public void onPlaced(Level world, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        CompoundTag tag = Stack.getTagOrEmpty(stack);
        if (!tag.isEmpty()) {
            readStorable(tag.getCompound(NBT.TAG_STORABLE_STACK));
        }
    }

    @Override
    public void onRemoved(Level world, BlockState state, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (this instanceof IInventoryHolder) {
                if (!keepInventory() || !keepStorable()) {
                    getInventory().drop(world, this.worldPosition);
                }
            }
        }
    }

    public ItemStack storeToStack(ItemStack stack) {
        CompoundTag nbt = writeStorable(new CompoundTag());
        CompoundTag nbt1 = Stack.getTagOrEmpty(stack);
        if (!nbt.isEmpty() && keepStorable()) {
            nbt1.put(NBT.TAG_STORABLE_STACK, nbt);
            stack.setTag(nbt1);
        }
        return stack;
    }

    public static <T extends AbstractTileEntity> T fromStack(ItemStack stack, T tile) {
        CompoundTag nbt = stack.getTagElement(NBT.TAG_STORABLE_STACK);
        if (nbt != null) {
            tile.readStorable(nbt);
        }
        return tile;
    }

    public boolean keepStorable() {
        return true;
    }

    protected boolean keepInventory() {
        return false;
    }

    public Tank getTank() {
        return this.tank;
    }

    public Redstone getRedstoneMode() {
        return this.redstone;
    }

    public void setRedstoneMode(Redstone mode) {
        this.redstone = mode;
    }

    public boolean checkRedstone() {
        boolean power = this.level != null && this.level.getBestNeighborSignal(this.worldPosition) > 0;
        return Redstone.IGNORE.equals(getRedstoneMode()) || power && Redstone.ON.equals(getRedstoneMode()) || !power && Redstone.OFF.equals(getRedstoneMode());
    }

    public void sync() {
        if (this.level instanceof ServerLevel) {
            final BlockState state = getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
            setChanged();
        }
    }

    public boolean isRemote() {
        return this.level != null && this.level.isClientSide;
    }

    public void setContainerOpen(boolean value) {
        final boolean b = this.isContainerOpen;
        this.isContainerOpen = value;
        if (b != value) {
            sync();
        }
    }

    public Inventory getInventory() {
        return this.inv;
    }
}
