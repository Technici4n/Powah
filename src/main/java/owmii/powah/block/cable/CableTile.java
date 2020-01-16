package owmii.powah.block.cable;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import owmii.lib.util.Energy;
import owmii.powah.block.ITiles;
import owmii.powah.block.PowahTile;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CableTile extends PowahTile {
    public final Map<Direction, LinkedCables> linkedCables = new HashMap<>();
    public final Set<Direction> energySides = new HashSet<>();
    public boolean[] flags = new boolean[6];

    public CableTile(int maxReceive, int maxExtract, boolean isCreative) {
        super(ITiles.CABLE, 0, maxReceive, maxExtract, isCreative);
        for (Direction side : Direction.values()) {
            this.linkedCables.put(side, LinkedCables.create());
        }
    }

    public CableTile() {
        this(0, 0, false);
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        ListNBT list = compound.getList("LinkedCables", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT nbt = list.getCompound(i);
            Direction direction = Direction.values()[nbt.getInt("Direction")];
            this.linkedCables.put(direction, LinkedCables.create().read(nbt));

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
        this.linkedCables.forEach((direction, linkedCables) -> {
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
    public boolean isNBTStorable() {
        return false;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction direction) {
        if (this.world == null || direction == null || !canReceive(direction)) return 0;
        int received = 0;

        for (Direction side : this.energySides) {
            int amount = maxReceive - received;
            if (amount > 0) {
                int net = Math.min(amount, this.internal.getMaxExtract());
                amount -= net;
                if (canExtract(side)) {
                    BlockPos pos1 = this.pos.offset(side);
                    TileEntity tile = this.world.getTileEntity(pos1);
                    if (Energy.canReceive(tile, side)) {
                        if (tile instanceof CableTile)
                            continue;
                        received += Energy.receive(tile, side, net, simulate);
                        if (maxReceive - received <= 0) {
                            return received;
                        }
                    }
                }
            }
        }

        LinkedCables cables = this.linkedCables.get(direction);
        for (BlockPos cablePos : cables.cables()) {
            if (cablePos.equals(getPos())) continue;
            TileEntity cableTile = this.world.getTileEntity(cablePos);
            if (cableTile instanceof CableTile && cableTile != this) {
                CableTile cable = (CableTile) cableTile;
                for (Direction side : cable.energySides) {
                    int amount = maxReceive - received;
                    if (amount > 0) {
                        int net = Math.min(amount, this.internal.getMaxExtract());
                        amount -= net;
                        if (cable.canExtract(side)) {
                            BlockPos pos1 = cablePos.offset(side);
                            TileEntity tile = this.world.getTileEntity(pos1);
                            if (Energy.canReceive(tile, side)) {
                                if (tile instanceof CableTile || pos1.equals(this.pos.offset(direction.getOpposite())))
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
        this.linkedCables.get(side).search(block, this, side).clear();
    }
}
