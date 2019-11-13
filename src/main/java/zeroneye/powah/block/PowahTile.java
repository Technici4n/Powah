package zeroneye.powah.block;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import zeroneye.lib.block.TileBase;
import zeroneye.lib.energy.EnergyStorage;

import javax.annotation.Nullable;

public abstract class PowahTile extends TileBase.Tickable {
    private final EnergyStorage internal;

    public PowahTile(TileEntityType<?> type) {
        super(type);
        this.internal = new EnergyStorage() {
            @Override
            public int getMaxEnergyStored() {
                return PowahTile.this.getMaxEnergyStored();
            }

            @Override
            public int getMaxExtract() {
                return PowahTile.this.getTransfer();
            }

            @Override
            public int getMaxReceive() {
                return PowahTile.this.getTransfer();
            }

            @Override
            public boolean canReceive() {
                return PowahTile.this.canReceive() && super.canReceive();
            }

            @Override
            public boolean canExtract() {
                return PowahTile.this.canExtract() && super.canExtract();
            }
        };
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        this.internal.setEnergy(compound.getInt(EnergyStorage.TAG_ENERGEY_STORED));
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        compound.putInt(EnergyStorage.TAG_ENERGEY_STORED, this.internal.getEnergyStored());
        return super.writeSync(compound);
    }

    public EnergyStorage getInternal() {
        return internal;
    }

    public int getMaxEnergyStored() {
        Block block = getBlockState().getBlock();
        return block instanceof PowahBlock ? ((PowahBlock) block).getCapacity() : 0;
    }

    public int getTransfer() {
        Block block = getBlockState().getBlock();
        return block instanceof PowahBlock ? ((PowahBlock) block).getTransfer() : 0;
    }

    public boolean canReceive() {
        return !(getBlockState().getBlock() instanceof GeneratorBlock);
    }

    public boolean canExtract() {
        return true;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityEnergy.ENERGY ? LazyOptional.of(this::getInternal).cast() : super.getCapability(cap, side);
    }
}
