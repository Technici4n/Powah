package owmii.powah.network;

import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.RunningOnDifferentThreadException;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.INetworkDirection;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.event.EventNetworkChannel;
import owmii.powah.Powah;
import owmii.powah.network.packet.NextEnergyConfigPacket;
import owmii.powah.network.packet.NextRedstoneModePacket;
import owmii.powah.network.packet.SetChannelPacket;
import owmii.powah.network.packet.SwitchGenModePacket;

public final class Network {
    private static final ResourceLocation CHANNEL = Powah.id("packet");
    private static int nextId = 0;
    private static final List<Function<FriendlyByteBuf, ? extends IPacket>> decoders = new ArrayList<>();
    private static final IdentityHashMap<Class<?>, Integer> packetIds = new IdentityHashMap<>();

    public static <T extends IPacket> void register(Class<T> packetClass, Function<FriendlyByteBuf, T> decoder) {
        decoders.add(decoder);
        packetIds.put(packetClass, nextId);
        nextId++;
    }

    public static void register() {
        EventNetworkChannel ec = NetworkRegistry.ChannelBuilder.named(CHANNEL)
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .eventNetworkChannel();
        ec.registerObject(new Network());

        register(NextEnergyConfigPacket.class, NextEnergyConfigPacket::new);
        register(NextRedstoneModePacket.class, NextRedstoneModePacket::new);
        register(SetChannelPacket.class, SetChannelPacket::new);
        register(SwitchGenModePacket.class, SwitchGenModePacket::new);
    }

    @SubscribeEvent
    public void serverPacket(final NetworkEvent.ClientCustomPayloadEvent ev) {
        try {
            var ctx = ev.getSource();
            ctx.setPacketHandled(true);
            var packet = deserializePacket(ev.getPayload());
            var player = ctx.getSender();
            if (player == null) {
                throw new IllegalStateException("Cannot handle a C2S packet without a player");
            }
            ctx.enqueueWork(() -> packet.handle(player));
        } catch (final RunningOnDifferentThreadException ignored) {

        }
    }

    private IPacket deserializePacket(FriendlyByteBuf payload) {
        int packetId = payload.readVarInt();
        return decoders.get(packetId).apply(payload);
    }

    public static void toServer(IPacket msg) {
        var packet = encodePacket(msg, PlayNetworkDirection.PLAY_TO_SERVER);
        Minecraft.getInstance().getConnection().send(packet);
    }

    private static Packet<?> encodePacket(IPacket packet, PlayNetworkDirection direction) {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(packetIds.get(packet.getClass()));
        packet.encode(buf);

        return direction.buildPacket(
                new INetworkDirection.PacketData(buf, 0), CHANNEL);
    }

}
