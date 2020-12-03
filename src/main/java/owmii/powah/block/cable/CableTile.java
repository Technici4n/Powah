package owmii.powah.block.cable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import owmii.lib.block.AbstractEnergyStorage;
import owmii.lib.block.IInventoryHolder;
import owmii.lib.logistics.energy.Energy;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.config.CableConfig;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class CableTile extends AbstractEnergyStorage<Tier, CableConfig, CableBlock> implements IInventoryHolder {


    public final Map<Direction, EnergyProxy> proxyMap = new HashMap<>();
    public final Set<Direction> energySides = new HashSet<>();

    public CableTile(Tier variant) {
        super(Tiles.CABLE, variant);
        for (Direction side : Direction.values()) {
            this.proxyMap.put(side, new EnergyProxy());
        }
    }

    public CableTile() {
        this(Tier.STARTER);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        this.proxyMap.forEach((direction, linkedCables) -> {
            CompoundNBT nbt = new CompoundNBT();
            linkedCables.write(nbt);
            nbt.putInt("direction", direction.ordinal());
            list.add(nbt);
        });
        compound.put("linked_cables", list);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        ListNBT list = compound.getList("linked_cables", Constants.NBT.TAG_COMPOUND);
        IntStream.range(0, list.size()).mapToObj(list::getCompound).forEach(nbt -> {
            Direction direction = Direction.values()[nbt.getInt("direction")];
            this.proxyMap.put(direction, new EnergyProxy().read(nbt));
        });
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        ListNBT list1 = compound.getList("energy_directions", Constants.NBT.TAG_COMPOUND);
        IntStream.range(0, list1.size()).mapToObj(list1::getCompound)
                .map(nbt -> Direction.values()[nbt.getInt("energy_direction")])
                .forEach(this.energySides::add);
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        ListNBT list1 = new ListNBT();
        this.energySides.forEach((direction) -> {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("energy_direction", direction.ordinal());
            list1.add(nbt);
        });
        compound.put("energy_directions", list1);
        return super.writeSync(compound);
    }

    @Override
    public long receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction direction) {
        if (this.world == null || isRemote() || direction == null || !checkRedstone() || !canReceiveEnergy(direction))
            return 0;
        long received = 0;
        received += pushEnergy(this.world, maxReceive, simulate, direction, this);
        for (BlockPos cablePos : this.proxyMap.get(direction).cables()) {
            TileEntity cableTile = this.world.getTileEntity(cablePos);
            if (cableTile instanceof CableTile) {
                CableTile cable = (CableTile) cableTile;
                received += cable.pushEnergy(this.world, maxReceive, simulate, direction, this);
            }
        }
        return received;
    }

    private long pushEnergy(World world, int maxReceive, boolean simulate, @Nullable Direction direction, CableTile cable) {
        long received = 0;
        for (Direction side : this.energySides) {
            if (cable.equals(this) && side.equals(direction) || !canExtractEnergy(side)) continue;
            BlockPos pos = this.pos.offset(side);
            if (direction != null && cable.getPos().offset(direction).equals(pos)) continue;
            TileEntity tile = world.getTileEntity(pos);
            long amount = maxReceive - received;
            if (amount > 0) {
                if (Energy.canReceive(tile, side)) {
                    long net = Math.min(amount, this.energy.getMaxExtract());
                    amount -= net;
                    received += Energy.receive(tile, side, net, simulate);
                    if (maxReceive - received <= 0) {
                        return received;
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
    protected long getEnergyCapacity() {
        return 0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.pos, this.pos.add(1, 1, 1));
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean keepStorable() {
        return false;
    }
}
