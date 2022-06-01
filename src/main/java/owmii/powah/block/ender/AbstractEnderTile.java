package owmii.powah.block.ender;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.block.AbstractEnergyStorage;
import owmii.lib.block.IInventoryHolder;
import owmii.lib.block.IOwnable;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.registry.IVariant;
import owmii.lib.util.Player;
import owmii.lib.util.math.RangedInt;
import owmii.powah.api.energy.endernetwork.IEnderExtender;

import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AbstractEnderTile<V extends Enum<V> & IVariant<V>, C extends IEnergyConfig<V>, B extends AbstractEnergyBlock<V, C, B>> extends AbstractEnergyStorage<V, C, B> implements IOwnable, IInventoryHolder {
    private final RangedInt channel = new RangedInt(12);

    @Nullable
    private GameProfile owner;
    private boolean flag;

    public AbstractEnderTile(BlockEntityType<?> type, BlockPos pos, BlockState state, V variant) {
        super(type, pos, state, variant);
    }

    @Override
    public void readStorable(CompoundTag nbt) {
        super.readStorable(nbt);
        this.channel.read(nbt, "channel");
        if (nbt.hasUUID("owner_id")) {
            this.owner = new GameProfile(nbt.getUUID("owner_id"), nbt.getString("owner_name"));
        }
    }

    @Override
    public CompoundTag writeStorable(CompoundTag nbt) {
        this.channel.writ(nbt, "channel");
        if (this.owner != null) {
            nbt.putUUID("owner_id", this.owner.getId());
            nbt.putString("owner_name", this.owner.getName());
        }
        return super.writeStorable(nbt);
    }

    @Override
    protected void onFirstTick(Level world) {
        super.onFirstTick(world);
        getEnergy().setTransfer(getEnergyTransfer());
    }

    @Override
    protected int postTick(Level world) {
        if (!isRemote()) {
            if (this.energy.clone(getEnergy())) {
                sync(5);
            }
        }
        return chargeItems(1, 3) + extractFromSides(world) > 0 ? 10 : -1;
    }

    @Override
    public void onSlotChanged(int slot) {
        if (this.level != null && slot == 0) {
            ItemStack stack = this.inv.getStackInSlot(0);
            if (isExtender() && stack.getItem() instanceof IEnderExtender) {
                Energy energy = getEnergy();
                IEnderExtender e = (IEnderExtender) stack.getItem();
                long cap = e.getExtendedCapacity(stack);
                long newCap = energy.getCapacity() + cap;
                if (cap <= Energy.MAX && newCap > 0 && newCap <= Energy.MAX) {
                    if (!isRemote()) {
                        energy.setCapacity(newCap);
                        energy.setStored(e.getExtendedEnergy(stack) + getEnergy().getStored());
                        setEnergy(energy);
                    }
                    stack.shrink(1);
                    this.level.playSound(null, this.worldPosition, SoundEvents.ENDER_EYE_DEATH, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    protected long receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction side) {
        final long l = super.receiveEnergy(maxReceive, simulate, side);
        setEnergy(getEnergy());
        return l;
    }

    @Override
    protected long extractEnergy(int maxExtract, boolean simulate, @Nullable Direction side) {
        final long l = super.extractEnergy(maxExtract, simulate, side);
        setEnergy(getEnergy());
        return l;
    }

    @Override
    public boolean canExtractEnergy(@Nullable Direction side) {
        return checkRedstone() && super.canExtractEnergy(side);
    }

    @Override
    public boolean canReceiveEnergy(@Nullable Direction side) {
        return checkRedstone() && super.canReceiveEnergy(side);
    }

    @Override
    public void onPlaced(Level world, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onPlaced(world, state, placer, stack);
        if (getOwner() == null && placer instanceof ServerPlayer && !Player.isFake((net.minecraft.world.entity.player.Player) placer)) {
            setOwner(((ServerPlayer) placer).getGameProfile());
        }
    }

    public void setEnergy(Energy energy) {
        if (!isRemote() && this.owner != null) {
            EnderNetwork network = EnderNetwork.get(level);
            network.setEnergy(this.owner.getId(), this.channel.get(), energy);
        }
    }

    public boolean isExtender() {
        return true;
    }

    @Nullable
    @Override
    public GameProfile getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(@Nullable GameProfile owner) {
        this.owner = owner;
    }

    @Override
    public Energy getEnergy() {
        if (isRemote()) {
            return this.energy;
        } else {
            return EnderNetwork.get(level).getEnergy(this, this.channel.get()).setTransfer(getEnergyTransfer());
        }
    }

    public RangedInt getChannel() {
        return this.channel;
    }

    public int getMaxChannels() {
        return 0;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        if (slot == 0) {
            if (stack.getItem() instanceof IEnderExtender) {
                IEnderExtender extender = (IEnderExtender) stack.getItem();
                long l = extender.getExtendedCapacity(stack);
                return l > 0 && l + getEnergy().getCapacity() <= Energy.MAX;
            } else return false;
        }
        return Energy.chargeable(stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }
}
