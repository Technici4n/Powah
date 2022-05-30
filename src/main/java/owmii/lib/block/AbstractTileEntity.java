package owmii.lib.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import owmii.lib.logistics.IRedstoneInteract;
import owmii.lib.logistics.Redstone;
import owmii.lib.logistics.fluid.Tank;
import owmii.lib.logistics.inventory.Inventory;
import owmii.lib.registry.IVariant;
import owmii.lib.util.NBT;
import owmii.lib.util.Stack;

import javax.annotation.Nullable;

@SuppressWarnings("unchecked")
public class AbstractTileEntity<V extends IVariant, B extends AbstractBlock<V, B>> extends TileEntity implements IBlockEntity, IRedstoneInteract {
    @CapabilityInject(IItemHandler.class)
    public static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    @CapabilityInject(IFluidHandler.class)
    public static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY = CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

    /**
     * Used when this is instance of {@link IInventoryHolder}
     **/
    protected final Inventory inv = Inventory.createBlank();
    private final LazyOptional<Inventory> invHolder = LazyOptional.of(() -> this.inv);
    /**
     * Used when this is instance of {@link ITankHolder}
     **/
    protected final Tank tank = new Tank(0);
    private final LazyOptional<FluidTank> tankHolder = LazyOptional.of(() -> this.tank);

    protected V variant;
    protected boolean isContainerOpen;
    /**
     * Used when this is instance of {@link IRedstoneInteract}
     **/
    private Redstone redstone = Redstone.IGNORE;

    public AbstractTileEntity(TileEntityType<?> type) {
        this(type, IVariant.getEmpty());
        this.tank.validate(stack -> true);
    }

    public AbstractTileEntity(TileEntityType<?> type, V variant) {
        super(type);
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
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        readSync(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);
        return writeSync(nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 3, writeSync(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        readSync(pkt.getNbtCompound());
    }

    protected void readSync(CompoundNBT nbt) {
        if (!this.variant.isEmpty() && nbt.contains("variant", 3)) {
            this.variant = (V) this.variant.read(nbt, "variant");
        }
        if (this instanceof IInventoryHolder && !keepInventory()) {
            this.inv.deserializeNBT(nbt);
        }
        if (this instanceof ITankHolder) {
            if (!((ITankHolder) this).keepFluid()) {
                this.tank.readFromNBT(nbt);
            }
        }
        this.redstone = Redstone.values()[nbt.getInt("redstone_mode")];
        readStorable(nbt);
    }

    protected CompoundNBT writeSync(CompoundNBT nbt) {
        if (!this.variant.isEmpty()) {
            this.variant.write(nbt, (Enum<?>) this.variant, "variant");
        }
        if (this instanceof IInventoryHolder && !keepInventory()) {
            nbt.merge(this.inv.serializeNBT());
        }
        if (this instanceof ITankHolder) {
            if (!((ITankHolder) this).keepFluid()) {
                this.tank.writeToNBT(nbt);
            }
        }
        nbt.putInt("redstone_mode", this.redstone.ordinal());
        return writeStorable(nbt);
    }

    public void readStorable(CompoundNBT nbt) {
        if (this instanceof IInventoryHolder && keepInventory()) {
            this.inv.deserializeNBT(nbt);
        }
        if (this instanceof ITankHolder) {
            if (((ITankHolder) this).keepFluid()) {
                this.tank.readFromNBT(nbt);
            }
        }
    }

    public CompoundNBT writeStorable(CompoundNBT nbt) {
        if (this instanceof IInventoryHolder && keepInventory()) {
            nbt.merge(this.inv.serializeNBT());
        }
        if (this instanceof ITankHolder) {
            if (((ITankHolder) this).keepFluid()) {
                this.tank.writeToNBT(nbt);
            }
        }
        return nbt;
    }

    @Override
    public void onPlaced(World world, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        CompoundNBT tag = Stack.getTagOrEmpty(stack);
        if (!tag.isEmpty()) {
            readStorable(tag.getCompound(NBT.TAG_STORABLE_STACK));
        }
    }

    @Override
    public void onRemoved(World world, BlockState state, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (this instanceof IInventoryHolder) {
                if (!keepInventory() || !keepStorable()) {
                    getInventory().drop(world, this.pos);
                }
            }
        }
    }

    public ItemStack storeToStack(ItemStack stack) {
        CompoundNBT nbt = writeStorable(new CompoundNBT());
        CompoundNBT nbt1 = Stack.getTagOrEmpty(stack);
        if (!nbt.isEmpty() && keepStorable()) {
            nbt1.put(NBT.TAG_STORABLE_STACK, nbt);
            stack.setTag(nbt1);
        }
        return stack;
    }

    public static <T extends AbstractTileEntity> T fromStack(ItemStack stack, T tile) {
        CompoundNBT nbt = stack.getChildTag(NBT.TAG_STORABLE_STACK);
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
        boolean power = this.world != null && this.world.getRedstonePowerFromNeighbors(this.pos) > 0;
        return Redstone.IGNORE.equals(getRedstoneMode()) || power && Redstone.ON.equals(getRedstoneMode()) || !power && Redstone.OFF.equals(getRedstoneMode());
    }

    public void sync() {
        if (this.world instanceof ServerWorld) {
            final BlockState state = getBlockState();
            this.world.notifyBlockUpdate(this.pos, state, state, 3);
            this.world.markChunkDirty(this.pos, this);
        }
    }

    public boolean isRemote() {
        return this.world != null && this.world.isRemote;
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        invHolder.invalidate();
        tankHolder.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == ITEM_HANDLER_CAPABILITY && this instanceof IInventoryHolder && !this.inv.isBlank()) {
            return this.invHolder.cast();
        }
        if (cap == FLUID_HANDLER_CAPABILITY && this instanceof ITankHolder) {
            return this.tankHolder.cast();
        }
        return super.getCapability(cap, side);
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
