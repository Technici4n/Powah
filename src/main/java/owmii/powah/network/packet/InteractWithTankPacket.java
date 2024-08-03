package owmii.powah.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import owmii.powah.Powah;
import owmii.powah.lib.logistics.inventory.AbstractTileContainer;
import owmii.powah.network.ServerboundPacket;

public record InteractWithTankPacket(int containerId, boolean drain) implements ServerboundPacket {

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final Type<InteractWithTankPacket> TYPE = new Type<>(Powah.id("interact_with_tank"));

    public static final StreamCodec<RegistryFriendlyByteBuf, InteractWithTankPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, InteractWithTankPacket::containerId,
            ByteBufCodecs.BOOL, InteractWithTankPacket::drain,
            InteractWithTankPacket::new);

    @Override
    public void handleOnServer(ServerPlayer player) {
        if (player.containerMenu instanceof AbstractTileContainer<?> tileContainer && tileContainer.containerId == containerId) {
            tileContainer.interactWithTank(drain);
        }
    }
}
