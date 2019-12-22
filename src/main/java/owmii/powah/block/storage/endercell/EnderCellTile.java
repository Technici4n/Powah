package owmii.powah.block.storage.endercell;

import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import owmii.lib.util.Energy;
import owmii.lib.util.Server;
import owmii.powah.block.ITiles;
import owmii.powah.block.PowahBlock;
import owmii.powah.block.PowahTile;
import owmii.powah.block.storage.energycell.EnergyCellBlock;
import owmii.powah.energy.PowahStorage;
import owmii.powah.item.PowahBlockItem;

import javax.annotation.Nullable;

public class EnderCellTile extends PowahTile {
    private int channels;
    private int activeChannel;
    private boolean flag;

    @Nullable
    private GameProfile owner;

    public EnderCellTile(int maxReceive, int maxExtract, int channels) {
        super(ITiles.ENDER_CELL, 0, maxReceive, maxExtract, false);
        this.channels = channels;
        this.inv.add(1);
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
    protected void onFirstTick() {
        super.onFirstTick();
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
            if (getOwner() != null) {
                if (!this.inv.isEmpty()) {
                    ItemStack stack = this.inv.getStackInSlot(0);
                    if (stack.getItem() instanceof PowahBlockItem) {
                        PowahBlockItem item = (PowahBlockItem) stack.getItem();
                        PowahBlock block = item.getBlock();
                        if (block instanceof EnergyCellBlock) {
                            PowahStorage storage = getInternal();
                            int cap = block.getCapacity();
                            int newCap = getCapacity() + cap;
                            if (newCap <= 2000000000) {
                                int stored = Energy.getStored(stack);
                                storage.setCapacity(newCap);
                                storage.setEnergy(stored + getEnergyStored());
                                setInternal(storage);
                                stack.shrink(1);
                            }
                        }
                    }
                }
            }

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
    public void setEnergy(int amount) {
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

    protected void setInternal(PowahStorage storage) {
        if (getOwner() != null) {
            if (isServerWorld()) {
                EnderNetwork network = Server.getData(EnderNetwork::new);
                network.setStorage(getOwner().getId(), this.activeChannel, storage);
            }
        }
    }

    protected void updateInternal() {
        setInternal(getInternal());
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
}
