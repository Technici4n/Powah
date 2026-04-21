package owmii.powah.lib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import owmii.powah.components.PowahComponents;
import owmii.powah.lib.logistics.IRedstoneInteract;
import owmii.powah.lib.logistics.Redstone;
import owmii.powah.lib.logistics.fluid.Tank;
import owmii.powah.lib.logistics.inventory.Inventory;

@SuppressWarnings("unchecked")
public class PowahBaseBlockEntity<B extends PowahBaseBlock<B>> extends BlockEntity implements IBlockEntity, IRedstoneInteract {
    private static final Logger LOG = LoggerFactory.getLogger(PowahBaseBlockEntity.class);

    /**
     * Used when this is instance of {@link IInventoryHolder}
     **/
    protected final Inventory inv;
    /**
     * Used when this is instance of {@link ITankHolder}
     **/
    protected final Tank tank;

    protected boolean isContainerOpen;
    /**
     * Used when this is instance of {@link IRedstoneInteract}
     **/
    private Redstone redstone = Redstone.IGNORE;

    public PowahBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inv = new Inventory(getInternalInventorySize());
        if (this instanceof IInventoryHolder) {
            this.inv.setTile((IInventoryHolder) this);
        }
        this.tank = new Tank(getInternalTankCapacity());
    }

    protected int getInternalInventorySize() {
        return 0;
    }

    protected int getInternalTankCapacity() {
        return 0;
    }

    public B getBlock() {
        return (B) getBlockState().getBlock();
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        readSync(input);

        if (!input.getBooleanOr("#c", false)) { // Server only...
            loadServerOnly(input);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        writeSync(output);
        saveServerOnly(output);
    }

    @Override
    public final CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        var tag = saveWithoutMetadata(registries);
        tag.putBoolean("#c", true); // mark client tag
        return tag;
    }

    @Nullable
    @Override
    public final ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    protected void loadServerOnly(ValueInput input) {
    }

    protected void saveServerOnly(ValueOutput output) {
    }

    protected void readSync(ValueInput input) {
        if (this instanceof IInventoryHolder && !keepInventory()) {
            this.inv.deserialize(input);
        }
        if (this instanceof ITankHolder tankHolder) {
            if (!tankHolder.keepFluid()) {
                this.tank.deserialize(input.childOrEmpty("tank"));
            }
        }
        this.redstone = Redstone.values()[input.getIntOr("redstone_mode", 0)];
        readStorable(input);
    }

    protected void writeSync(ValueOutput output) {
        if (this instanceof IInventoryHolder && !keepInventory()) {
            this.inv.serialize(output);
        }
        if (this instanceof ITankHolder tankHolder) {
            if (!tankHolder.keepFluid()) {
                this.tank.serialize(output.child("tank"));
            }
        }
        output.putInt("redstone_mode", this.redstone.ordinal());
        writeStorable(output);
    }

    public void readStorable(ValueInput input) {
        if (this instanceof IInventoryHolder && keepInventory()) {
            this.inv.deserialize(input);
        }
        if (this instanceof ITankHolder tankHolder) {
            if (tankHolder.keepFluid()) {
                this.tank.deserialize(input.childOrEmpty("tank"));
            }
        }
    }

    public void writeStorable(ValueOutput output) {
        if (this instanceof IInventoryHolder && keepInventory()) {
            this.inv.serialize(output);
        }
        if (this instanceof ITankHolder tankHolder) {
            if (tankHolder.keepFluid()) {
                this.tank.serialize(output.child("tank"));
            }
        }
    }

    @Override
    public void onPlaced(Level level, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        var storedState = stack.get(PowahComponents.STORED_BLOCK_ENTITY_STATE);
        if (storedState != null) {
            var input = TagValueInput.create(new ProblemReporter.ScopedCollector(LOG), level.registryAccess(), storedState.copyTag());
            readStorable(input);
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (this instanceof IInventoryHolder) {
            if (!keepInventory() || !keepStorable()) {
                getInventory().drop(getLevel(), this.worldPosition);
            }
        }
    }

    public ItemStack storeToStack(ItemStack stack) {
        var output = TagValueOutput.createWithContext(new ProblemReporter.ScopedCollector(LOG), getLevel().registryAccess());
        writeStorable(output);
        var nbt = output.buildResult();
        if (!nbt.isEmpty() && keepStorable()) {
            stack.set(PowahComponents.STORED_BLOCK_ENTITY_STATE, CustomData.of(nbt));
        }
        return stack;
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
        Redstone redstoneMode = getRedstoneMode();

        // avoid checking redstone if mode is IGNORE
        // getBestNeighborSignal is relatively expensive and should not be called if not needed
        if (Redstone.IGNORE.equals(redstoneMode))
            return true;

        boolean power = this.level != null && this.level.getBestNeighborSignal(this.worldPosition) > 0;
        return power && Redstone.ON.equals(redstoneMode)
                || !power && Redstone.OFF.equals(redstoneMode);
    }

    public void sync() {
        if (this.level instanceof ServerLevel) {
            final BlockState state = getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
            setChanged();
        }
    }

    public boolean isRemote() {
        return this.level != null && this.level.isClientSide();
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
