package owmii.powah.block.ender;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.storage.WorldSavedData;
import owmii.lib.block.IOwnable;
import owmii.lib.logistics.energy.Energy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EnderNetwork extends WorldSavedData {
    public static final EnderNetwork INSTANCE = new EnderNetwork();
    private final Map<UUID, NonNullList<Energy>> map = new HashMap<>();

    public EnderNetwork() {
        super("powah_network");
    }

    @Override
    public void read(CompoundNBT nbt) {
        ListNBT listNBT = nbt.getList("network", 10);
        for (int i = 0; i < listNBT.size(); i++) {
            CompoundNBT nbt1 = listNBT.getCompound(i);
            UUID uuid = nbt1.getUniqueId("owner_id");
            NonNullList<Energy> list = empty();
            ListNBT listNBT1 = nbt1.getList("channels", 10);
            for (int j = 0; j < listNBT1.size(); j++) {
                CompoundNBT nbt2 = listNBT1.getCompound(j);
                Energy energy = Energy.create(0);
                energy.read(nbt2, true, false);
                list.set(j, energy);
            }
            this.map.put(uuid, list);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        ListNBT listNBT = new ListNBT();
        this.map.forEach((uuid, list) -> {
            CompoundNBT nbt1 = new CompoundNBT();
            nbt1.putUniqueId("owner_id", uuid);
            ListNBT listNBT1 = new ListNBT();
            list.forEach(storage -> {
                CompoundNBT nbt2 = new CompoundNBT();
                storage.write(nbt2, true, false);
                listNBT1.add(nbt2);
            });
            nbt1.put("channels", listNBT1);
            listNBT.add(nbt1);
        });
        nbt.put("network", listNBT);
        return nbt;
    }

    public CompoundNBT serialize(UUID uuid) {
        NonNullList<Energy> list = getChannels(uuid);
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("channels", list.stream()
                .map(energy -> energy.write(true, false))
                .collect(Collectors.toCollection(ListNBT::new)));
        return nbt;
    }

    public void deserialize(UUID uuid, CompoundNBT nbt) {
        ListNBT listNBT = nbt.getList("channels", 10);
        NonNullList<Energy> list = empty();
        IntStream.range(0, listNBT.size()).mapToObj(listNBT::getCompound).forEachOrdered(nbt1 -> {
            list.add(Energy.create(0).read(nbt1, true, false));
        });
        this.map.put(uuid, list);
    }

    public Energy getEnergy(UUID uuid, int channel) {
        return getChannels(uuid).get(channel);
    }

    public void setEnergy(UUID uuid, int channel, Energy energy) {
        NonNullList<Energy> list = getChannels(uuid);
        list.set(channel, energy);
        this.map.put(uuid, list);
        markDirty();
    }

    public NonNullList<Energy> getChannels(IOwnable ownable) {
        if (ownable.getOwner() != null) {
            return getChannels(ownable.getOwner().getId());
        }
        return empty();
    }

    public NonNullList<Energy> getChannels(UUID uuid) {
        if (this.map.containsKey(uuid)) {
            return this.map.get(uuid);
        }
        return empty();
    }

    public static NonNullList<Energy> empty() {
        return NonNullList.withSize(12, Energy.create(0));
    }

    public Map<UUID, NonNullList<Energy>> getMap() {
        return this.map;
    }
}
