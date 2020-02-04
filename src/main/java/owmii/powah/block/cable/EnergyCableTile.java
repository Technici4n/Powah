package owmii.powah.block.cable;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import owmii.lib.block.TileBase;
import owmii.lib.energy.Energy;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EnergyCableTile extends TileBase.EnergyStorage<Tier, EnergyCableBlock> {
    public final Map<Direction, EnergyProxy> proxyMap = new HashMap<>();
    public final Set<Direction> energySides = new HashSet<>();
    public boolean[] flags = new boolean[6];

    public EnergyCableTile(Tier variant) {
        super(ITiles.ENERGY_CABLE, variant);
        for (Direction side : Direction.values()) {
            this.proxyMap.put(side, new EnergyProxy());
        }
    }

    public EnergyCableTile() {
        this(Tier.STARTER);
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        ListNBT list = compound.getList("LinkedCables", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT nbt = list.getCompound(i);
            Direction direction = Direction.values()[nbt.getInt("Direction")];
            this.proxyMap.put(direction, new EnergyProxy().read(nbt));

        }
        ListNBT list1 = compound.getList("EnergyDirections", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list1.size(); i++) {
            CompoundNBT nbt = list1.getCompound(i);
            Direction direction = Direction.values()[nbt.getInt("EnergyDirection")];
            this.energySides.add(direction);
        }
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        this.proxyMap.forEach((direction, linkedCables) -> {
            CompoundNBT nbt = new CompoundNBT();
            linkedCables.write(nbt);
            nbt.putInt("Direction", direction.ordinal());
            list.add(nbt);
        });
        compound.put("LinkedCables", list);
        ListNBT list1 = new ListNBT();
        this.energySides.forEach((direction) -> {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("EnergyDirection", direction.ordinal());
            list1.add(nbt);
        });
        compound.put("EnergyDirections", list1);
        return super.writeSync(compound);
    }

    @Override
    public long receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction direction) {
        if (this.world == null || direction == null || !canReceiveEnergy(direction)) return 0;
        int received = 0;
        for (Direction side : this.energySides) {
            int amount = maxReceive - received;
            if (amount > 0) {
                long net = Math.min(amount, this.energy.getMaxExtract());
                amount -= net;
                if (canExtractEnergy(side)) {
                    BlockPos pos1 = this.pos.offset(side);
                    TileEntity tile = this.world.getTileEntity(pos1);
                    if (Energy.canReceive(tile, side)) {
                        if (tile instanceof EnergyCableTile)
                            continue;
                        received += Energy.receive(tile, side, net, simulate);
                        if (maxReceive - received <= 0) {
                            return received;
                        }
                    }
                }
            }
        }

        EnergyProxy cables = this.proxyMap.get(direction);
        for (BlockPos cablePos : cables.cables()) {
            if (cablePos.equals(getPos())) continue;
            TileEntity cableTile = this.world.getTileEntity(cablePos);
            if (cableTile instanceof EnergyCableTile && cableTile != this) {
                EnergyCableTile cable = (EnergyCableTile) cableTile;
                for (Direction side : cable.energySides) {
                    int amount = maxReceive - received;
                    if (amount > 0) {
                        long net = Math.min(amount, this.energy.getMaxExtract());
                        amount -= net;
                        if (cable.canExtractEnergy(side)) {
                            BlockPos pos1 = cablePos.offset(side);
                            TileEntity tile = this.world.getTileEntity(pos1);
                            if (Energy.canReceive(tile, side)) {
                                if (tile instanceof EnergyCableTile || pos1.equals(this.pos.offset(direction.getOpposite())))
                                    continue;
                                received += Energy.receive(tile, side, net, simulate);
                                if (maxReceive - received <= 0) {
                                    return received;
                                }
                            }
                        }
                    }
                }
            }
        }
        return received;
    }

    public void search(Block block, Direction side) {
        this.proxyMap.get(side).search(block, this, side).clear();
    }

    @Override
    public long defaultEnergyCapacity() {
        return 0L;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.pos, this.pos.add(1, 1, 1));
    }
}
