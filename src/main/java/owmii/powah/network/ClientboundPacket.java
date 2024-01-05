package owmii.powah.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public interface ClientboundPacket extends CustomPacketPayload {
    void handleOnClient(Player player);
}
