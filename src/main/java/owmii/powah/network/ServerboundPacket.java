package owmii.powah.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public interface ServerboundPacket extends CustomPacketPayload {
    void handleOnServer(ServerPlayer player);
}
