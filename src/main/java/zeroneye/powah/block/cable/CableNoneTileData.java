package zeroneye.powah.block.cable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CableNoneTileData extends WorldSavedData {
    private final Map<BlockPos, CompoundNBT> map = new HashMap<>();

    public CableNoneTileData() {
        super("powah_cables_data");
    }

    @Override
    public void read(CompoundNBT nbt) {
        ListNBT listNBT = nbt.getList("CableDatas", Constants.NBT.TAG_COMPOUND);
        listNBT.forEach(inbt -> {
            if (inbt instanceof CompoundNBT) {
                CompoundNBT nbt1 = (CompoundNBT) inbt;
                this.map.put(NBTUtil.readBlockPos(nbt1.getCompound("Pos")), nbt1.getCompound("Tag"));
            }
        });
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT listNBT = new ListNBT();
        this.map.forEach((pos, nbt) -> {
            CompoundNBT nbt1 = new CompoundNBT();
            nbt1.put("Pos", NBTUtil.writeBlockPos(pos));
            nbt.put("Tag", nbt);
            listNBT.add(nbt1);
        });
        compound.put("CableDatas", listNBT);
        return compound;
    }

    @Nullable
    public CompoundNBT add(BlockPos pos, CompoundNBT nbt) {
        final CompoundNBT put = this.map.put(pos, nbt);
        markDirty();
        return put;
    }

    @Nullable
    public CompoundNBT remove(BlockPos pos) {
        final CompoundNBT remove = this.map.remove(pos);
        markDirty();
        return remove;
    }

    @Nullable
    public CompoundNBT get(BlockPos pos) {
        return this.map.get(pos);
    }

    public void clear() {
        this.map.clear();
        markDirty();
    }
}
