package owmii.powah.lib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IPacket<T> {
    void encode(T msg, FriendlyByteBuf buffer);

    T decode(FriendlyByteBuf buffer);

    void handle(T msg, Supplier<NetworkEvent.Context> ctx);
}