//package zeroneye.powah.block;
//
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.tileentity.TileEntityType;
//import net.minecraft.util.Direction;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.energy.CapabilityEnergy;
//import zeroneye.lib.block.IInvBase;
//import zeroneye.lib.block.TileBase;
//import zeroneye.lib.util.Energy;
//import zeroneye.powah.energy.PowahStorage;
//import zeroneye.powah.energy.PowerMode;
//import zeroneye.powah.energy.RedstoneMode;
//
//import javax.annotation.Nullable;
//
//public abstract class PowahTile_old extends TileBase.Tickable implements IInvBase {
//    private final PowahStorage internal = new PowahStorage();
//    private RedstoneMode redstoneMode = RedstoneMode.IGNORE;
//    private boolean isContainerOpen;
//
//    public PowahTile_old(TileEntityType<?> type, int capacity, int transfer, boolean canReceive, boolean canExtract, boolean isCreative) {
//        super(type);
//        this.internal.setCapacity(capacity);
//        this.internal.setMaxExtract(transfer);
//        this.internal.setMaxReceive(transfer);
//        this.internal.setCanExtract(canExtract);
//        this.internal.setCanReceive(canReceive);
//        this.internal.setCreative(isCreative);
//    }
//
//    public PowahTile_old(TileEntityType<?> type) {
//        this(type, 0, 0, false, false, false);
//    }
//
//    @Override
//    public void readStorable(CompoundNBT compound) {
//        super.readStorable(compound);
//        this.internal.read(compound);
//    }
//
//    @Override
//    public CompoundNBT writeStorable(CompoundNBT compound) {
//        this.internal.write(compound);
//        return super.writeStorable(compound);
//    }
//
//    @Override
//    public void readSync(CompoundNBT compound) {
//        super.readSync(compound);
//        this.redstoneMode = RedstoneMode.values()[compound.getInt("RedstoneMode")];
//    }
//
//    @Override
//    public CompoundNBT writeSync(CompoundNBT compound) {
//        compound.putInt("RedstoneMode", this.redstoneMode.ordinal());
//        return super.writeSync(compound);
//    }
//
//    @Override
//    public void tick() {
//        if (this.world == null || !canExtract() || !checkRedstone()) return;
//
//        int extracted = 0;
//        for (Direction direction : Direction.values()) {
//            int amount = Math.min(getMaxReceive(), getEnergyStored());
//            int received = Energy.receive(this.world, this.pos.offset(direction), direction.getOpposite(), amount, false);
//            extracted += this.internal.extractEnergy(received, false);
//        }
//
//        for (int i = 0; i < getChargingSlots(); i++) {
//            final ItemStack stack = getStackInSlot(i);
//            if (!stack.isEmpty()) {
//                int amount = Math.min(getMaxExtract(), getEnergyStored());
//                int received = Energy.receive(stack, amount, false);
//                extracted += this.internal.extractEnergy(received, false);
//            }
//        }
//
//        if (extracted > 0) {
//            sync(true);
//        }
//        super.tick();
//    }
//
//    @Override
//    public int getSizeInventory() {
//        return getChargingSlots() + getUpgradSlots();
//    }
//
//    public int getUpgradSlots() {
//        return 0;
//    }
//
//    public int getChargingSlots() {
//        return 0;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
//        for (int i = 0; i < getChargingSlots(); i++) {
//            if (i == index) {
//                return Energy.getForgeEnergy(itemStack, null).isPresent();
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public int getSyncTicks() {
//        return isContainerOpen() ? 10 : 40;
//    }
//
//    public PowahStorage getInternal() {
//        return internal;
//    }
//
//    public boolean isContainerOpen() {
//        return isContainerOpen;
//    }
//
//    public void setContainerOpen(boolean containerOpen) {
//        isContainerOpen = containerOpen;
//    }
//
//    public int getMaxEnergyStored() {
//        return this.internal.getMaxEnergyStored();
//    }
//
//    public int getEnergyStored() {
//        return this.internal.getEnergyStored();
//    }
//
//    public int getMaxExtract() {
//        return this.internal.getMaxExtract();
//    }
//
//    public int getMaxReceive() {
//        return this.internal.getMaxReceive();
//    }
//
//    public boolean canReceive() {
//        return this.internal.canReceive();
//    }
//
//    public boolean canExtract() {
//        return this.internal.canExtract();
//    }
//
//    public boolean isCreative() {
//        return this.internal.isCreative();
//    }
//
//    public PowerMode getPowerMode() {
//        return this.internal.getPowerMode();
//    }
//
//    public void nextPowerMode() {
//        int i = getPowerMode().ordinal() + 1;
//        PowerMode powerMode = PowerMode.values()[i > 3 ? 0 : i];
//        if (!this.internal.canExtract() && powerMode.isOut()) {
//            powerMode = PowerMode.IN;
//        } else if (!this.internal.canReceive() && powerMode.isIn()) {
//            powerMode = PowerMode.NON.equals(powerMode) || PowerMode.ALL.equals(powerMode) ? PowerMode.OUT : PowerMode.NON;
//        }
//        this.internal.setPowerMode(powerMode);
//        markDirtyAndSync();
//    }
//
//    public RedstoneMode getRedstoneMode() {
//        return redstoneMode;
//    }
//
//    public void setRedstoneMode(RedstoneMode redstoneMode) {
//        this.redstoneMode = redstoneMode;
//    }
//
//    public void nextRedstoneMode() {
//        int i = this.redstoneMode.ordinal() + 1;
//        RedstoneMode redstoneMode = RedstoneMode.values()[i > 2 ? 0 : i];
//        this.redstoneMode = redstoneMode;
//        markDirtyAndSync();
//    }
//
//    public boolean checkRedstone() {
//        boolean power = this.world != null && this.world.getRedstonePowerFromNeighbors(this.pos) > 0;
//        return RedstoneMode.IGNORE.equals(getRedstoneMode())
//                || power && RedstoneMode.ON.equals(getRedstoneMode())
//                || !power && RedstoneMode.OFF.equals(getRedstoneMode());
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public boolean showPowerModeButton() {
//        return true;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public boolean showRedstoneModeButton() {
//        return true;
//    }
//
//    public int receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction direction) {
//        if (!canReceive() || !getPowerMode().isIn() || !checkRedstone())
//            return 0;
//
//        int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(getMaxReceive(), maxReceive));
//        if (!simulate)
//            this.internal.setEnergy(getEnergyStored() + energyReceived);
//        return energyReceived;
//    }
//
//    public int extractEnergy(int maxExtract, boolean simulate, @Nullable Direction direction) {
//        if (!canExtract() || !getPowerMode().isOut() || !checkRedstone())
//            return 0;
//
//        int energyExtracted = Math.min(getEnergyStored(), Math.min(getMaxExtract(), maxExtract));
//        if (!simulate && !this.isCreative())
//            this.internal.setEnergy(getEnergyStored() - energyExtracted);
//        return energyExtracted;
//    }
//
//    @Override
//    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
//        return cap == CapabilityEnergy.ENERGY ? LazyOptional.of(() -> new PowahStorage() {
//            @Override
//            public int extractEnergy(int maxExtract, boolean simulate) {
//                return PowahTile_old.this.extractEnergy(maxExtract, simulate, side);
//            }
//
//            @Override
//            public int receiveEnergy(int maxReceive, boolean simulate) {
//                return PowahTile_old.this.receiveEnergy(maxReceive, simulate, side);
//            }
//        }).cast() : super.getCapability(cap, side);
//    }
//
//}
