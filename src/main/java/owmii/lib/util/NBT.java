package owmii.lib.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import owmii.lib.Lollipop;

import java.util.Collection;
import java.util.UUID;

public class NBT {
    public static final String TAG_STORABLE_STACK = Lollipop.MOD_ID + "tile_stack_nbt";

    public static <T extends Collection<UUID>> T readUUIDList(CompoundTag nbt, String key, T list) {
        ListTag listNBT = nbt.getList(key, Tag.TAG_COMPOUND);
        for (int i = 0; i < listNBT.size(); i++) {
            CompoundTag compound = listNBT.getCompound(i);
            list.add(compound.getUUID("uuid"));
        }
        return list;
    }

    public static void writeUUIDList(CompoundTag nbt, Collection<UUID> list, String key) {
        ListTag listNBT = new ListTag();
        list.forEach(pos -> {
            CompoundTag compound = new CompoundTag();
            compound.putUUID("uuid", pos);
            listNBT.add(compound);
        });
        nbt.put(key, listNBT);
    }

    public static <T extends Collection<BlockPos>> T readPosList(CompoundTag nbt, String key, T list) {
        ListTag listNBT = nbt.getList(key, Tag.TAG_COMPOUND);
        for (int i = 0; i < listNBT.size(); i++) {
            CompoundTag compound = listNBT.getCompound(i);
            list.add(readPos(compound, "Pos"));
        }
        return list;
    }

    public static void writePosList(CompoundTag nbt, Collection<BlockPos> list, String key) {
        ListTag listNBT = new ListTag();
        list.forEach(pos -> {
            CompoundTag compound = new CompoundTag();
            writePos(compound, pos, "Pos");
            listNBT.add(compound);
        });
        nbt.put(key, listNBT);
    }

    public static BlockPos readPos(CompoundTag nbt, String key) {
        return NbtUtils.readBlockPos(nbt.getCompound(key));
    }

    public static void writePos(CompoundTag nbt, BlockPos pos, String key) {
        nbt.put(key, NbtUtils.writeBlockPos(pos));
    }
}
