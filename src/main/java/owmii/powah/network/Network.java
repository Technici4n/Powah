package owmii.powah.network;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import owmii.powah.Powah;
import owmii.powah.network.packet.InteractWithTankPacket;
import owmii.powah.network.packet.NextEnergyConfigPacket;
import owmii.powah.network.packet.NextRedstoneModePacket;
import owmii.powah.network.packet.SetChannelPacket;
import owmii.powah.network.packet.SwitchGenModePacket;

public final class Network {
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(Powah.MOD_ID);

        registrar.playToServer(NextEnergyConfigPacket.TYPE, NextEnergyConfigPacket.STREAM_CODEC, Network::handleServerbound);
        registrar.playToServer(NextRedstoneModePacket.TYPE, NextRedstoneModePacket.STREAM_CODEC, Network::handleServerbound);
        registrar.playToServer(SetChannelPacket.TYPE, SetChannelPacket.STREAM_CODEC, Network::handleServerbound);
        registrar.playToServer(SwitchGenModePacket.TYPE, SwitchGenModePacket.STREAM_CODEC, Network::handleServerbound);
        registrar.playToServer(InteractWithTankPacket.TYPE, InteractWithTankPacket.STREAM_CODEC, Network::handleServerbound);
    }

    private static void handleServerbound(ServerboundPacket packet, IPayloadContext context) {
        if (!context.flow().isServerbound()) {
            throw new IllegalArgumentException("Trying to handle a serverbound packet on the client: " + packet);
        }

        if (context.player() instanceof ServerPlayer serverPlayer) {
            context.enqueueWork(() -> packet.handleOnServer(serverPlayer));
        }
    }

    public static void toServer(ServerboundPacket packet) {
        var connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            connection.send(packet);
        }
    }
}
