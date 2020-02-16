package owmii.powah.block.endercell;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import owmii.lib.block.TileBase;
import owmii.lib.energy.Energy;
import owmii.lib.util.Server;
import owmii.powah.api.energy.endernetwork.IEnderExtender;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;

import javax.annotation.Nullable;
import java.util.List;

public class EnderCellTile extends TileBase.EnergyStorage<Tier, EnderCellBlock> {
    protected int activeChannel;
    protected boolean flag;

    @Nullable
    private GameProfile owner;

    public EnderCellTile(TileEntityType<?> tileEntityType, Tier variant) {
        super(tileEntityType, variant);
        this.inv.add(1);
    }

    public EnderCellTile(Tier variant) {
        this(ITiles.ENDER_CELL, variant);
    }

    public EnderCellTile() {
        this(Tier.STARTER);
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        this.energy.readCapacity(compound);
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        this.energy.writeCapacity(compound);
        return super.writeSync(compound);
    }

    @Override
    public void readStorable(CompoundNBT compound) {
        if (compound.hasUniqueId("OwnerId")) {
            this.owner = new GameProfile(compound.getUniqueId("OwnerId"), compound.getString("OwnerName"));
            this.activeChannel = compound.getInt("ActiveChannel");
        }
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT compound) {
        if (this.owner != null) {
            compound.putUniqueId("OwnerId", this.owner.getId());
            compound.putString("OwnerName", this.owner.getName());
            compound.putInt("ActiveChannel", this.activeChannel);
        }
        return compound;
    }

    @Override
    protected void onFirstTick(World world) {}

    @Override
    protected boolean postTicks(World world) {
        if (!isRemote()) {
            extendEnderNetwork(world);
            long prevCap = this.energy.getCapacity();
            long prevStored = this.energy.getStored();
            this.energy.setCapacity(getEnergyCapacity());
            this.energy.setStored(getEnergyStored());
            if (this.flag && (prevCap != this.energy.getStored() || prevStored != this.energy.getCapacity())) {
                this.flag = false;
                markDirtyAndSync();
            }
        }
        return super.postTicks(world) || this.isContainerOpen || this.ticks % 100 == 0;
    }

    private void extendEnderNetwork(World world) {
        ItemStack stack = this.inv.getStackInSlot(builtInSlots());
        if (isExtender() && stack.getItem() instanceof IEnderExtender) {
            Energy energy = getEnergyStorage();
            IEnderExtender e = (IEnderExtender) stack.getItem();
            long cap = e.getExtendedCapacity(stack);
            long newCap = energy.getCapacity() + cap;
            if (cap <= Energy.MAX && newCap > 0 && newCap <= Energy.MAX) {
                if (!isRemote()) {
                    energy.setCapacity(newCap);
                    energy.setStored(e.getExtendedEnergy(stack) + getEnergyStored());
                    setEnergyStorage(energy);
                }
                stack.shrink(1);
                world.playSound(null, this.pos, SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public boolean canInsert(int index, ItemStack stack) {
        if (index == builtInSlots()) {
            if (isExtender() && stack.getItem() instanceof IEnderExtender) {
                Energy energy = getEnergyStorage();
                IEnderExtender e = (IEnderExtender) stack.getItem();
                long cap = e.getExtendedCapacity(stack);
                long newCap = energy.getCapacity() + cap;
                return cap <= Energy.MAX && newCap > 0 && newCap <= Energy.MAX;
            }
            return false;
        }
        return super.canInsert(index, stack);
    }

    @Override
    public void consumeEnergy(long amount) {
        Energy energy = getEnergyStorage();
        energy.consume(amount);
        setEnergyStorage(energy);
    }

    @Override
    public void produceEnergy(long amount) {
        Energy energy = getEnergyStorage();
        energy.produce(amount);
        setEnergyStorage(energy);
    }

    @Override
    public Energy getEnergyStorage() {
        if (!isRemote() && this.owner != null) {
            EnderNetwork network = Server.getData(EnderNetwork::new);
            return network.getStorage(this.owner.getId(), this.activeChannel).setMaxTransfer();
        }
        return Energy.create(0);
    }

    public void setEnergyStorage(Energy energy) {
        if (!isRemote() && this.owner != null) {
            EnderNetwork network = Server.getData(EnderNetwork::new);
            network.setStorage(this.owner.getId(), this.activeChannel, energy);
        }
    }

    public Energy getSyncEnergy() {
        return this.energy;
    }

    @Override
    public void getListedEnergyInfo(List<String> list) {
        list.add(TextFormatting.DARK_GRAY + I18n.format("info.lollipop.channel", "" + TextFormatting.DARK_AQUA + (this.activeChannel + 1)));
        super.getListedEnergyInfo(list);
    }

    @Override
    public long getMaxEnergyReceive() {
        return defaultTransfer();
    }

    @Override
    public long getMaxEnergyExtract() {
        return defaultTransfer();
    }

    public void setActiveChannel(int channel) {
        this.activeChannel = Math.min(15, Math.max(0, channel));
        this.flag = true;
    }

    public int getActiveChannel() {
        return this.activeChannel;
    }

    public int getTotalChannels() {
        return getBlock().getTotalChannels();
    }

    @Nullable
    public GameProfile getOwner() {
        return this.owner;
    }

    public void setOwner(@Nullable GameProfile owner) {
        this.owner = owner;
    }

    public boolean isExtender() {
        return true;
    }
}
