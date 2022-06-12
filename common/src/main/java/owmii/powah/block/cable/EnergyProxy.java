package owmii.powah.block.cable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class EnergyProxy {
    public final Map<Direction, EnergyProxy> proxies = new HashMap<>();
    public final List<BlockPos> searchCache = new ArrayList<>();
    public final List<BlockPos> cables = new ArrayList<>();

    public void init() {
        for (Direction side : Direction.values()) {
            this.proxies.put(side, new EnergyProxy());
        }
    }

    public EnergyProxy read(CompoundTag compound) {
        ListTag listNBT = compound.getList("cables_pos", Tag.TAG_COMPOUND);
        for (int j = 0; j < listNBT.size(); j++) {
            CompoundTag nbt = listNBT.getCompound(j);
            add(NbtUtils.readBlockPos(nbt.getCompound("cable_pos")));
        }
        return this;
    }

    public CompoundTag write(CompoundTag compound) {
        ListTag listNBT = new ListTag();
        this.cables.forEach(pos -> {
            CompoundTag nbt = new CompoundTag();
            nbt.put("cable_pos", NbtUtils.writeBlockPos(pos));
            listNBT.add(nbt);
        });
        compound.put("cables_pos", listNBT);
        return compound;
    }

    public Set<CableTile> all(Level world) {
        final Set<CableTile> cableTiles = new HashSet<>();
        Iterator<BlockPos> iterator = this.cables.iterator();
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof CableTile) {
                cableTiles.add((CableTile) tileEntity);
            } else {
                iterator.remove();
            }
        }
        return cableTiles;
    }

    public List<BlockPos> search(Block block, CableTile tile, Direction side) {
        Level world = tile.getLevel();
        if (world != null) {
            BlockPos pos = tile.getBlockPos();
            this.searchCache.add(pos);
            for (Direction direction : Direction.values()) {
                BlockPos blockPos = pos.relative(direction);
                BlockState state = world.getBlockState(blockPos);
                if (state.getBlock() == block) {
                    BlockEntity tile1 = world.getBlockEntity(blockPos);
                    if (tile1 instanceof CableTile) {
                        add(blockPos);
                    }
                    CableBlock cableBlock = (CableBlock) state.getBlock();
                    cableBlock.searchCables(world, blockPos, tile, side);
                }
            }
        }
        return this.searchCache;
    }

    public boolean remove(BlockPos pos) {
        return this.cables.remove(pos);
    }

    public boolean add(BlockPos pos) {
        if (!this.cables.contains(pos)) {
            return this.cables.add(pos);
        }
        return false;
    }

    public List<BlockPos> cables() {
        return this.cables;
    }
}
