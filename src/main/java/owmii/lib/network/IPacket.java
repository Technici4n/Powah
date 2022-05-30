package owmii.lib.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface IPacket<T> {
    void encode(T msg, PacketBuffer buffer);

    T decode(PacketBuffer buffer);

    void handle(T msg, Supplier<NetworkEvent.Context> ctx);
}