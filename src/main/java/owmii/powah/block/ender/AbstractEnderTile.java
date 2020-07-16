package owmii.powah.block.ender;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import owmii.lib.block.*;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.util.Player;
import owmii.lib.util.Server;
import owmii.lib.util.math.RangedInt;
import owmii.powah.api.energy.endernetwork.IEnderExtender;

import javax.annotation.Nullable;

public class AbstractEnderTile<V extends IVariant<?>, C extends IEnergyConfig<V>, B extends AbstractEnergyBlock<V, C>> extends AbstractEnergyStorage<V, C, B> implements IOwnable, IInventoryHolder {
    private final RangedInt channel = new RangedInt(12);

    @Nullable
    private GameProfile owner;
    private boolean flag;

    public AbstractEnderTile(TileEntityType<?> type, V variant) {
        super(type, variant);
    }

    @Override
    public void readStorable(CompoundNBT nbt) {
        super.readStorable(nbt);
        this.channel.read(nbt, "channel");
        if (nbt.hasUniqueId("owner_id")) {
            this.owner = new GameProfile(nbt.getUniqueId("owner_id"), nbt.getString("owner_name"));
        }
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT nbt) {
        this.channel.writ(nbt, "channel");
        if (this.owner != null) {
            nbt.putUniqueId("owner_id", this.owner.getId());
            nbt.putString("owner_name", this.owner.getName());
        }
        return super.writeStorable(nbt);
    }

    @Override
    protected void onFirstTick(World world) {
        super.onFirstTick(world);
        getEnergy().setTransfer(getEnergyTransfer());
    }

    @Override
    protected int postTick(World world) {
        if (!isRemote()) {
            if (this.energy.clone(getEnergy())) {
                sync(5);
            }
        }
        return chargeItems(1, 3) + extractFromSides(world) > 0 ? 10 : -1;
    }

    @Override
    public void onSlotChanged(int slot) {
        if (this.world != null && slot == 0) {
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
                    this.world.playSound(null, this.pos, SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    protected long receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction side) {
        if (!checkRedstone()) return 0;
        final long l = super.receiveEnergy(maxReceive, simulate, side);
        setEnergy(getEnergy());
        return l;
    }

    @Override
    protected long extractEnergy(int maxExtract, boolean simulate, @Nullable Direction side) {
        if (!checkRedstone()) return 0;
        final long l = super.extractEnergy(maxExtract, simulate, side);
        setEnergy(getEnergy());
        return l;
    }

    @Override
    public void onPlaced(World world, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onPlaced(world, state, placer, stack);
        if (getOwner() == null && placer instanceof ServerPlayerEntity && !Player.isFake((PlayerEntity) placer)) {
            setOwner(((ServerPlayerEntity) placer).getGameProfile());
        }
    }

    public void setEnergy(Energy energy) {
        if (!isRemote() && this.owner != null) {
            EnderNetwork network = Server.getData(EnderNetwork::new);
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
            return Server.getData(EnderNetwork::new)
                    .getChannels(this).get(this.channel.get()).setTransfer(getEnergyTransfer());
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
                return extender.getExtendedCapacity(stack) + getEnergy().getCapacity() <= Energy.MAX;
            } else return false;
        }
        return Energy.isPresent(stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }
}
