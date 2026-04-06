package owmii.powah.block.ender;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import owmii.powah.Powah;
import owmii.powah.lib.block.IOwnable;
import owmii.powah.lib.logistics.energy.Energy;

public class EnderNetwork extends SavedData {
    private static final String NAME = "powah_network";

    public static final int MAX_CHANNELS = 12;

    private static final Codec<EnderNetwork> CODEC = Packed.CODEC.xmap(EnderNetwork::new, EnderNetwork::getData);

    private static final SavedDataType<EnderNetwork> TYPE = new SavedDataType<>(Powah.id(NAME), EnderNetwork::new, CODEC);

    private final Map<UUID, ImmutableList<Energy>> map = new HashMap<>();

    /**
     * Can only call this on the server!
     */
    public static EnderNetwork get(ServerLevel level) {
        return get(level.getServer());
    }

    public static EnderNetwork get(MinecraftServer server) {
        var overworld = server.getLevel(ServerLevel.OVERWORLD);
        Objects.requireNonNull(overworld, "Server should have an overworld.");
        return overworld.getDataStorage().computeIfAbsent(TYPE);
    }

    private EnderNetwork() {
    }

    private EnderNetwork(Packed packed) {
        for (var network : packed.networks()) {
            var energy = getEnergy(network.owner, network.channel);
            energy.setCapacity(network.capacity);
            energy.setStored(network.stored);
        }
    }

    private Packed getData() {
        var packedNetworks = new ArrayList<PackedNetwork>();
        for (var entry : this.map.entrySet()) {
            var owner = entry.getKey();
            for (int channel = 0; channel < entry.getValue().size(); channel++) {
                var energy = entry.getValue().get(channel);
                packedNetworks.add(new PackedNetwork(owner, channel, energy.getEnergyStored(), energy.getCapacity()));
            }
        }
        return new Packed(packedNetworks);
    }

    public Energy getEnergy(IOwnable ownable, int channel) {
        if (ownable.getOwner() != null) {
            return getEnergy(ownable.getOwner().id(), channel);
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
    }

    public ImmutableList<Energy> getChannels(IOwnable ownable) {
        if (ownable.getOwner() != null) {
            return getChannels(ownable.getOwner().id());
        }
        return empty();
    }

    public ImmutableList<Energy> getChannels(UUID uuid) {
        return this.map.computeIfAbsent(uuid, (k) -> empty());
    }

    public static ImmutableList<Energy> empty() {
        return IntStream.range(0, MAX_CHANNELS).mapToObj(i -> Energy.create(0)).collect(ImmutableList.toImmutableList());
    }

    record PackedNetwork(UUID owner, int channel, long stored, long capacity) {
        public static Codec<PackedNetwork> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                UUIDUtil.STRING_CODEC.fieldOf("owner").forGetter(PackedNetwork::owner),
                Codec.INT.fieldOf("channel").forGetter(PackedNetwork::channel),
                Codec.LONG.fieldOf("stored").forGetter(PackedNetwork::stored),
                Codec.LONG.fieldOf("capacity").forGetter(PackedNetwork::capacity)).apply(builder, PackedNetwork::new));
    }

    record Packed(List<PackedNetwork> networks) {
        public static Codec<Packed> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                PackedNetwork.CODEC.listOf().fieldOf("networks").forGetter(Packed::networks)).apply(builder, Packed::new));
    }
}
