package owmii.powah.block.endercell;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import owmii.lib.util.Server;
import owmii.powah.block.ITiles;
import owmii.powah.block.PowahTile;
import owmii.powah.energy.PowahStorage;

import javax.annotation.Nullable;

public class EnderCellTile extends PowahTile {
    protected int channels;
    protected int activeChannel;
    protected boolean flag;

    @Nullable
    private GameProfile owner;

    public EnderCellTile(TileEntityType<?> enderCell, int maxReceive, int maxExtract, int channels) {
        super(enderCell, 0, maxReceive, maxExtract, false);
        this.channels = channels;
    }

    public EnderCellTile(int maxReceive, int maxExtract, int channels) {
        this(ITiles.ENDER_CELL, maxReceive, maxExtract, channels);
    }

    public EnderCellTile() {
        this(0, 0, 0);
    }

    @Override
    public void readStorable(CompoundNBT compound) {
        this.channels = compound.getInt("TotalChannels");
        if (compound.hasUniqueId("OwnerId")) {
            this.owner = new GameProfile(compound.getUniqueId("OwnerId"), compound.getString("OwnerName"));
            this.activeChannel = compound.getInt("ActiveChannel");
        }
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT compound) {
        compound.putInt("TotalChannels", this.channels);
        if (this.owner != null) {
            compound.putUniqueId("OwnerId", this.owner.getId());
            compound.putString("OwnerName", this.owner.getName());
            compound.putInt("ActiveChannel", this.activeChannel);
        }
        return compound;
    }

    @Override
    public void readSync(CompoundNBT compound) {
        this.internal.read(compound);
        super.readSync(compound);
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        this.internal.write(compound);
        return super.writeSync(compound);
    }

    @Override
    public boolean keepInventory() {
        return false;
    }

    @Override
    protected void firstTick() {
        super.firstTick();
        if (this.world == null) return;
        if (!this.world.isRemote) {
            if (getBlock() instanceof EnderCellBlock) {
                EnderCellBlock cell = (EnderCellBlock) getBlock();
                if (this.channels != cell.getChannels()) {
                    this.channels = cell.getChannels();
                    markDirtyAndSync();
                }
            }
        }
    }

    @Override
    protected boolean postTicks() {
        if (isServerWorld()) {
            int prevCap = this.internal.getMaxEnergyStored();
            int prevStored = this.internal.getEnergyStored();
            this.internal.setCapacity(getCapacity());
            this.internal.setEnergy(getEnergyStored());
            if (this.flag && (prevCap != this.internal.getMaxEnergyStored() || prevStored != this.internal.getEnergyStored())) {
                markDirtyAndSync();
                this.flag = false;
            }
        }
        return super.postTicks() || this.isContainerOpen || this.ticks % 100 == 0;
    }

    @Override
    public void setEnergy(int amount, @Nullable Direction side) {
        PowahStorage storage = getInternal();
        storage.setEnergy(amount);
        setInternal(storage);
    }

    public void setCapacity(int cap) {
        PowahStorage storage = getInternal();
        storage.setCapacity(cap);
        setInternal(storage);
    }

    @Override
    public int getEnergyStored() {
        return getInternal().getEnergyStored();
    }

    @Override
    public int getCapacity() {
        return getInternal().getMaxEnergyStored();
    }

    @Override
    public PowahStorage getInternal() {
        if (isServerWorld()) {
            EnderNetwork network = Server.getData(EnderNetwork::new);
            if (getOwner() != null) {
                return network.getStorage(getOwner().getId(), this.activeChannel);
            }
        }
        return new PowahStorage(0);
    }

    public void setInternal(PowahStorage storage) {
        if (getOwner() != null) {
            if (isServerWorld()) {
                EnderNetwork network = Server.getData(EnderNetwork::new);
                network.setStorage(getOwner().getId(), this.activeChannel, storage);
            }
        }
    }

    @Nullable
    public GameProfile getOwner() {
        return this.owner;
    }

    public void setOwner(@Nullable GameProfile owner) {
        this.owner = owner;
    }

    public int getChannels() {
        return this.channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public int getActiveChannel() {
        return this.activeChannel;
    }

    public void setActiveChannel(int activeChannel) {
        this.activeChannel = Math.min(15, Math.max(0, activeChannel));
        this.flag = true;
    }

    public boolean isExtender() {
        return true;
    }
}
