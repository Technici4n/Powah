package owmii.powah.block.storage.endercell;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import owmii.powah.energy.PowahStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderNetwork extends WorldSavedData {
    private final Map<UUID, NonNullList<PowahStorage>> enderNetworks = new HashMap<>();

    public EnderNetwork() {
        super("powah_ender_network_data");
    }

    @Override
    public void read(CompoundNBT nbt) {
        ListNBT listNBT = nbt.getList("EnderNetwork", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < listNBT.size(); i++) {
            CompoundNBT nbt1 = listNBT.getCompound(i);
            UUID uuid = nbt1.getUniqueId("UUID");
            NonNullList<PowahStorage> list = NonNullList.withSize(16, new PowahStorage(0));
            ListNBT listNBT1 = nbt1.getList("EnderChannels", Constants.NBT.TAG_COMPOUND);
            for (int j = 0; j < listNBT1.size(); j++) {
                CompoundNBT nbt2 = listNBT1.getCompound(j);
                list.set(j, PowahStorage.fromNBT(nbt2));
            }
            this.enderNetworks.put(uuid, list);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT listNBT = new ListNBT();
        this.enderNetworks.forEach((uuid, list) -> {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putUniqueId("UUID", uuid);
            ListNBT listNBT1 = new ListNBT();
            list.forEach(storage -> {
                CompoundNBT nbt1 = new CompoundNBT();
                storage.write(nbt1);
                listNBT1.add(nbt1);
            });
            nbt.put("EnderChannels", listNBT1);
            listNBT.add(nbt);
        });
        compound.put("EnderNetwork", listNBT);
        return compound;
    }

    public PowahStorage getStorage(UUID uuid, int channel) {
        NonNullList<PowahStorage> list = getEnderChannels(uuid);
        if (list.size() > channel) {
            return list.get(channel);
        }
        return new PowahStorage(0);
    }

    public void setStorage(UUID uuid, int channel, PowahStorage storage) {
        NonNullList<PowahStorage> list = getEnderChannels(uuid);
        list.set(channel, storage);
        this.enderNetworks.put(uuid, list);
        markDirty();
    }

    public NonNullList<PowahStorage> getEnderChannels(UUID uuid) {
        if (this.enderNetworks.containsKey(uuid)) {
            return this.enderNetworks.get(uuid);
        }
        return NonNullList.withSize(16, new PowahStorage(0));
    }

    public Map<UUID, NonNullList<PowahStorage>> getEnderNetworks() {
        return this.enderNetworks;
    }
}
