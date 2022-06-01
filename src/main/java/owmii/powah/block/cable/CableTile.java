package owmii.powah.block.cable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

    public CableTile(BlockPos pos, BlockState state, Tier variant) {
        super(Tiles.CABLE, pos, state, variant);
        for (Direction side : Direction.values()) {
            this.proxyMap.put(side, new EnergyProxy());
        }
    }

    public CableTile(BlockPos pos, BlockState state) {
        this(pos, state, Tier.STARTER);
    }

    @Override
    public CompoundTag saveServerOnly(CompoundTag compound) {
        ListTag list = new ListTag();
        this.proxyMap.forEach((direction, linkedCables) -> {
            CompoundTag nbt = new CompoundTag();
            linkedCables.write(nbt);
            nbt.putInt("direction", direction.ordinal());
            list.add(nbt);
        });
        compound.put("linked_cables", list);
        return super.saveServerOnly(compound);
    }

    @Override
    public void loadServerOnly(CompoundTag compound) {
        super.loadServerOnly(compound);
        ListTag list = compound.getList("linked_cables", Tag.TAG_COMPOUND);
        IntStream.range(0, list.size()).mapToObj(list::getCompound).forEach(nbt -> {
            Direction direction = Direction.values()[nbt.getInt("direction")];
            this.proxyMap.put(direction, new EnergyProxy().read(nbt));
        });
    }

    @Override
    public void readSync(CompoundTag compound) {
        super.readSync(compound);
        ListTag list1 = compound.getList("energy_directions", Tag.TAG_COMPOUND);
        IntStream.range(0, list1.size()).mapToObj(list1::getCompound)
                .map(nbt -> Direction.values()[nbt.getInt("energy_direction")])
                .forEach(this.energySides::add);
    }

    @Override
    public CompoundTag writeSync(CompoundTag compound) {
        ListTag list1 = new ListTag();
        this.energySides.forEach((direction) -> {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("energy_direction", direction.ordinal());
            list1.add(nbt);
        });
        compound.put("energy_directions", list1);
        return super.writeSync(compound);
    }

    @Override
    public long receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction direction) {
        if (this.level == null || isRemote() || direction == null || !checkRedstone() || !canReceiveEnergy(direction))
            return 0;
        long received = 0;
        received += pushEnergy(this.level, maxReceive, simulate, direction, this);
        for (BlockPos cablePos : this.proxyMap.get(direction).cables()) {
            long amount = maxReceive - received;
            if (amount <= 0) break;
            BlockEntity cableTile = this.level.getBlockEntity(cablePos);
            if (cableTile instanceof CableTile) {
                CableTile cable = (CableTile) cableTile;
                received += cable.pushEnergy(this.level, amount, simulate, direction, this);
            }
        }
        return received;
    }

    private long pushEnergy(Level world, long maxReceive, boolean simulate, @Nullable Direction direction, CableTile cable) {
        long received = 0;
        for (Direction side : this.energySides) {
            long amount = Math.min(maxReceive - received, this.energy.getMaxExtract());
            if (amount <= 0) break;
            if (cable.equals(this) && side.equals(direction) || !canExtractEnergy(side)) continue;
            BlockPos pos = this.worldPosition.relative(side);
            if (direction != null && cable.getBlockPos().relative(direction).equals(pos)) continue;
            BlockEntity tile = world.getBlockEntity(pos);
            if (Energy.canReceive(tile, side)) {
                received += Energy.receive(tile, side, amount, simulate);
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
    public AABB getRenderBoundingBox() {
        return new AABB(this.worldPosition, this.worldPosition.offset(1, 1, 1));
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
