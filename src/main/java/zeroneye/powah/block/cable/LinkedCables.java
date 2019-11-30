package zeroneye.powah.block.cable;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LinkedCables {
    private final Set<BlockPos> cables = new HashSet<>();
    public final Set<BlockPos> searchCach = new HashSet<>();

    public static LinkedCables create() {
        return new LinkedCables();
    }

    public LinkedCables read(CompoundNBT compound) {
        ListNBT listNBT = compound.getList("CablesPos", Constants.NBT.TAG_COMPOUND);
        for (int j = 0; j < listNBT.size(); j++) {
            CompoundNBT nbt = listNBT.getCompound(j);
            add(NBTUtil.readBlockPos(nbt.getCompound("CablePos")));
        }
        ListNBT listNBT1 = compound.getList("searchCach", Constants.NBT.TAG_COMPOUND);
        for (int j = 0; j < listNBT1.size(); j++) {
            CompoundNBT nbt = listNBT1.getCompound(j);
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

        ListNBT listNBT1 = new ListNBT();
        this.cables.forEach(pos -> {
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("CablePos", NBTUtil.writeBlockPos(pos));
            listNBT1.add(nbt);
        });
        compound.put("searchCach", listNBT1);

        return compound;
    }

    public Set<CableTile> all(World world) {
        final Set<CableTile> cableTiles = new HashSet<>();
        Iterator<BlockPos> iterator = this.cables.iterator();
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof CableTile) {
                cableTiles.add((CableTile) tileEntity);
            } else {
                iterator.remove();
            }
        }
        return cableTiles;
    }

    public Set<BlockPos> search(CableTile tile, Direction side) {
        World world = tile.getWorld();
        if (world != null) {
            BlockPos pos = tile.getPos();
            this.searchCach.add(pos);
            for (Direction direction : Direction.values()) {
                BlockPos blockPos = pos.offset(direction);
                BlockState state = world.getBlockState(blockPos);
                if (state.getBlock() instanceof CableBlock) {
                    TileEntity tileEntity = world.getTileEntity(blockPos);
                    if (tileEntity instanceof CableTile) {
                        add(blockPos);
                    }
                    CableBlock cableBlock = (CableBlock) state.getBlock();
                    cableBlock.searchCables(world, blockPos, tile, side);
                }
            }
        }
        return this.searchCach;
    }

    public boolean remove(BlockPos pos) {
        return this.cables.remove(pos);
    }

    public boolean add(BlockPos pos) {
        return this.cables.add(pos);
    }

    public Set<BlockPos> cables() {
        return cables;
    }
}
