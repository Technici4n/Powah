package owmii.powah.network;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import owmii.powah.Powah;
import owmii.powah.network.packet.NextEnergyConfigPacket;
import owmii.powah.network.packet.NextRedstoneModePacket;
import owmii.powah.network.packet.SetChannelPacket;
import owmii.powah.network.packet.SwitchGenModePacket;

public final class Network {
    public static void register(RegisterPayloadHandlerEvent event) {
        var registrar = event.registrar(Powah.MOD_ID);

        registrar.play(NextEnergyConfigPacket.ID, NextEnergyConfigPacket::new, Network::handleServerbound);
        registrar.play(NextRedstoneModePacket.ID, NextRedstoneModePacket::new, Network::handleServerbound);
        registrar.play(SetChannelPacket.ID, SetChannelPacket::new, Network::handleServerbound);
        registrar.play(SwitchGenModePacket.ID, SwitchGenModePacket::new, Network::handleServerbound);
    }

    private static void handleServerbound(ServerboundPacket packet, PlayPayloadContext context) {
        if (!context.flow().isServerbound()) {
            throw new IllegalArgumentException("Trying to handle a serverbound packet on the client: " + packet);
        }

        var player = context.player().orElse(null);
        if (player instanceof ServerPlayer serverPlayer) {
            context.workHandler().execute(() -> packet.handleOnServer(serverPlayer));
        }
    }

    public static void toServer(ServerboundPacket packet) {
        var connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            connection.send(packet);
        }
    }
}
