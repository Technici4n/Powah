package owmii.powah.block.ender;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import owmii.powah.lib.block.IOwnable;
import owmii.powah.lib.logistics.energy.Energy;

public class EnderNetwork extends SavedData {
    // Client-side instance.
    public static final EnderNetwork INSTANCE = new EnderNetwork();

    private static final String NAME = "powah_network";

    public static final int MAX_CHANNELS = 12;
    private final Map<UUID, ImmutableList<Energy>> map = new HashMap<>();

    /**
     * Can only call this on the server!
     */
    public static EnderNetwork get(Level level) {
        Preconditions.checkArgument(level instanceof ServerLevel);
        return get(((ServerLevel) level).getServer());
    }

    public static EnderNetwork get(MinecraftServer server) {
        var overworld = server.getLevel(ServerLevel.OVERWORLD);
        Objects.requireNonNull(overworld, "Server should have an overworld.");
        return overworld.getDataStorage().computeIfAbsent(EnderNetwork::new, EnderNetwork::new, NAME);
    }

    private EnderNetwork() {
    }

    private EnderNetwork(CompoundTag nbt) {
        ListTag listNBT = nbt.getList("network", 10);
        this.map.clear();
        for (int i = 0; i < listNBT.size(); i++) {
            CompoundTag nbt1 = listNBT.getCompound(i);
            UUID uuid = nbt1.getUUID("owner_id");
            ListTag listNBT1 = nbt1.getList("channels", 10);
            for (int j = 0; j < listNBT1.size(); j++) {
                CompoundTag nbt2 = listNBT1.getCompound(j);
                getEnergy(uuid, j).read(nbt2, true, false);
            }
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag listNBT = new ListTag();
        this.map.forEach((uuid, list) -> {
            CompoundTag nbt1 = new CompoundTag();
            nbt1.putUUID("owner_id", uuid);
            ListTag listNBT1 = new ListTag();
            list.forEach(storage -> {
                CompoundTag nbt2 = new CompoundTag();
                storage.write(nbt2, true, false);
                listNBT1.add(nbt2);
            });
            nbt1.put("channels", listNBT1);
            listNBT.add(nbt1);
        });
        nbt.put("network", listNBT);
        return nbt;
    }

    public CompoundTag serialize(UUID uuid) {
        CompoundTag nbt = new CompoundTag();
        nbt.put("channels", getChannels(uuid).stream()
                .map(energy -> energy.write(true, false))
                .collect(Collectors.toCollection(ListTag::new)));
        return nbt;
    }

    public void deserialize(UUID uuid, CompoundTag nbt) {
        ListTag listNBT = nbt.getList("channels", 10);
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
        getEnergy(uuid, channel).clone(energy);
        setDirty();
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
