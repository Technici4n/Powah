package owmii.lib.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import owmii.lib.Lollipop;

import java.util.Collection;
import java.util.UUID;

public class NBT {
    public static final String TAG_STORABLE_STACK = Lollipop.MOD_ID + "tile_stack_nbt";

    public static <T extends Collection<UUID>> T readUUIDList(CompoundNBT nbt, String key, T list) {
        ListNBT listNBT = nbt.getList(key, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < listNBT.size(); i++) {
            CompoundNBT compound = listNBT.getCompound(i);
            list.add(compound.getUniqueId("uuid"));
        }
        return list;
    }

    public static void writeUUIDList(CompoundNBT nbt, Collection<UUID> list, String key) {
        ListNBT listNBT = new ListNBT();
        list.forEach(pos -> {
            CompoundNBT compound = new CompoundNBT();
            compound.putUniqueId("uuid", pos);
            listNBT.add(compound);
        });
        nbt.put(key, listNBT);
    }

    public static <T extends Collection<BlockPos>> T readPosList(CompoundNBT nbt, String key, T list) {
        ListNBT listNBT = nbt.getList(key, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < listNBT.size(); i++) {
            CompoundNBT compound = listNBT.getCompound(i);
            list.add(readPos(compound, "Pos"));
        }
        return list;
    }

    public static void writePosList(CompoundNBT nbt, Collection<BlockPos> list, String key) {
        ListNBT listNBT = new ListNBT();
        list.forEach(pos -> {
            CompoundNBT compound = new CompoundNBT();
            writePos(compound, pos, "Pos");
            listNBT.add(compound);
        });
        nbt.put(key, listNBT);
    }

    public static BlockPos readPos(CompoundNBT nbt, String key) {
        return NBTUtil.readBlockPos(nbt.getCompound(key));
    }

    public static void writePos(CompoundNBT nbt, BlockPos pos, String key) {
        nbt.put(key, NBTUtil.writeBlockPos(pos));
    }
}
