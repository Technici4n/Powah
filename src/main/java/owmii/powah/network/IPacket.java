package owmii.powah.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public interface IPacket {
    void encode(FriendlyByteBuf buffer);

    void handle(Player player);
}
