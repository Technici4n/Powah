package owmii.powah.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import owmii.powah.Powah;
import owmii.powah.network.packet.NextEnergyConfigPacket;
import owmii.powah.network.packet.NextRedstoneModePacket;
import owmii.powah.network.packet.SetChannelPacket;
import owmii.powah.network.packet.SwitchGenModePacket;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public final class Network {
    private static final ResourceLocation PACKET_ID = Powah.id("packet");
    private static int nextId = 0;
    private static List<Constructor<? extends IPacket>> decoders = new ArrayList<>();
    private static IdentityHashMap<Class<?>, Integer> packetIds = new IdentityHashMap<>();

    public static <T extends IPacket> void register(Class<T> packetClass) {
        Constructor<T> ctor = null;
        try {
            ctor = packetClass.getConstructor(FriendlyByteBuf.class);
            decoders.add(ctor);
            nextId++;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to register packet", e);
        }
    }

    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, PACKET_ID, (buf, ctx) -> {
            int packetId = buf.readVarInt();

            try {
                var packet = decoders.get(packetId).newInstance(buf);
                ctx.queue(() -> {
                    packet.handle(ctx.getPlayer());
                });
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to construct packet of type " + packetId, e);
            }
        });

        register(NextEnergyConfigPacket.class);
        register(NextRedstoneModePacket.class);
        register(SetChannelPacket.class);
        register(SwitchGenModePacket.class);
    }

    private static FriendlyByteBuf encodePacket(IPacket packet) {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(packetIds.get(packet.getClass()));
        packet.encode(buf);
        return buf;
    }

    public static void toServer(IPacket msg) {
        NetworkManager.sendToServer(PACKET_ID, encodePacket(msg));
    }

    public static void toClient(IPacket msg, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkManager.sendToPlayer(serverPlayer, PACKET_ID, encodePacket(msg));
        }
    }

    /* TODO ARCH use or cleanup
    public <T> void toTracking(T msg, Entity entity) {
        this.channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), msg);
    }

    public <T> void toTrackingAndSelf(T msg, Entity entity) {
        this.channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);
    }

     */
}
