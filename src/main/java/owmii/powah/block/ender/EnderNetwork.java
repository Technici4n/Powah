package owmii.powah.block.ender;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
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
    public static final int MAX_CHANNELS = 12;
    private final Map<UUID, ImmutableList<Energy>> map = new HashMap<>();

    public EnderNetwork() {
        super("powah_network");
    }

    @Override
    public void read(CompoundNBT nbt) {
        ListNBT listNBT = nbt.getList("network", 10);
        this.map.clear();
        for (int i = 0; i < listNBT.size(); i++) {
            CompoundNBT nbt1 = listNBT.getCompound(i);
            UUID uuid = nbt1.getUniqueId("owner_id");
            ListNBT listNBT1 = nbt1.getList("channels", 10);
            for (int j = 0; j < listNBT1.size(); j++) {
                CompoundNBT nbt2 = listNBT1.getCompound(j);
                getEnergy(uuid, j).read(nbt2, true, false);
            }
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
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("channels", getChannels(uuid).stream()
                .map(energy -> energy.write(true, false))
                .collect(Collectors.toCollection(ListNBT::new)));
        return nbt;
    }

    public void deserialize(UUID uuid, CompoundNBT nbt) {
        ListNBT listNBT = nbt.getList("channels", 10);
        for (int i = 0; i < listNBT.size(); i++) {
            getEnergy(uuid, i).read(listNBT.getCompound(i), true, false);
        }
    }

    public Energy getEnergy(IOwnable ownable, int channel) {
        if (ownable.getOwner() != null) {
            return getEnergy(ownable.getOwner().getId(), channel);
        }
        return Energy.create(0);
    }

    public Energy getEnergy(UUID uuid, int channel) {
        if (channel < MAX_CHANNELS) {
            return getChannels(uuid).get(channel);
        }
        return Energy.create(0);
    }

    public void setEnergy(UUID uuid, int channel, Energy energy) {
        if (getEnergy(uuid, channel).clone(energy)) {
            markDirty();
        }
    }

    public ImmutableList<Energy> getChannels(IOwnable ownable) {
        if (ownable.getOwner() != null) {
            return getChannels(ownable.getOwner().getId());
        }
        return empty();
    }

    public ImmutableList<Energy> getChannels(UUID uuid) {
        return this.map.computeIfAbsent(uuid, (k) -> empty());
    }

    public static ImmutableList<Energy> empty() {
        return IntStream.range(0, MAX_CHANNELS).mapToObj(i -> Energy.create(0)).collect(ImmutableList.toImmutableList());
    }
}
