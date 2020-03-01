package owmii.powah.block.cable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

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

    public EnergyProxy read(CompoundNBT compound) {
        ListNBT listNBT = compound.getList("CablesPos", Constants.NBT.TAG_COMPOUND);
        for (int j = 0; j < listNBT.size(); j++) {
            CompoundNBT nbt = listNBT.getCompound(j);
            add(NBTUtil.readBlockPos(nbt.getCompound("CablePos")));
        }
        return this;
    }

    public CompoundNBT write(CompoundNBT compound) {
        ListNBT listNBT = new ListNBT();
        this.cables.forEach(pos -> {
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("CablePos", NBTUtil.writeBlockPos(pos));
            listNBT.add(nbt);
        });
        compound.put("CablesPos", listNBT);
        return compound;
    }

    public Set<EnergyCableTile> all(World world) {
        final Set<EnergyCableTile> cableTiles = new HashSet<>();
        Iterator<BlockPos> iterator = this.cables.iterator();
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof EnergyCableTile) {
                cableTiles.add((EnergyCableTile) tileEntity);
            } else {
                iterator.remove();
            }
        }
        return cableTiles;
    }

    public List<BlockPos> search(Block block, EnergyCableTile tile, Direction side) {
        World world = tile.getWorld();
        if (world != null) {
            BlockPos pos = tile.getPos();
            this.searchCache.add(pos);
            for (Direction direction : Direction.values()) {
                BlockPos blockPos = pos.offset(direction);
                BlockState state = world.getBlockState(blockPos);
                if (state.getBlock() == block) {
                    TileEntity tile1 = world.getTileEntity(blockPos);
                    if (tile1 instanceof EnergyCableTile) {
                        add(blockPos);
                    }
                    EnergyCableBlock cableBlock = (EnergyCableBlock) state.getBlock();
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
