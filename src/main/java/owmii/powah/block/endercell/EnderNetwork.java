package owmii.powah.block.endercell;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import owmii.lib.energy.Energy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderNetwork extends WorldSavedData {
    private final Map<UUID, NonNullList<Energy>> enderNetworks = new HashMap<>();

    public EnderNetwork() {
        super("powah_ender_network");
    }

    @Override
    public void read(CompoundNBT nbt) {
        ListNBT listNBT = nbt.getList("EnderNetwork", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < listNBT.size(); i++) {
            CompoundNBT nbt1 = listNBT.getCompound(i);
            UUID uuid = nbt1.getUniqueId("UUID");
            NonNullList<Energy> list = NonNullList.withSize(16, Energy.create(0));
            ListNBT listNBT1 = nbt1.getList("EnderChannels", Constants.NBT.TAG_COMPOUND);
            for (int j = 0; j < listNBT1.size(); j++) {
                CompoundNBT nbt2 = listNBT1.getCompound(j);
                Energy energy = Energy.create(0);
                energy.readCapacity(nbt2);
                energy.readStored(nbt2);
                list.set(j, energy);
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
                storage.writeCapacity(nbt1);
                storage.writeStored(nbt1);
                listNBT1.add(nbt1);
            });
            nbt.put("EnderChannels", listNBT1);
            listNBT.add(nbt);
        });
        compound.put("EnderNetwork", listNBT);
        return compound;
    }

    public Energy getStorage(UUID uuid, int channel) {
        NonNullList<Energy> list = getEnderChannels(uuid);
        if (list.size() > channel) {
            return list.get(channel);
        }
        return Energy.create(0);
    }

    public void setStorage(UUID uuid, int channel, Energy storage) {
        NonNullList<Energy> list = getEnderChannels(uuid);
        list.set(channel, storage);
        this.enderNetworks.put(uuid, list);
        markDirty();
    }

    public NonNullList<Energy> getEnderChannels(UUID uuid) {
        if (this.enderNetworks.containsKey(uuid)) {
            return this.enderNetworks.get(uuid);
        }
        return NonNullList.withSize(16, Energy.create(0));
    }

    public Map<UUID, NonNullList<Energy>> getEnderNetworks() {
        return this.enderNetworks;
    }
}
