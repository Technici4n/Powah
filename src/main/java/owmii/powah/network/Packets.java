package owmii.powah.network;

import owmii.powah.Powah;
import owmii.powah.network.packet.SetChannelPacket;

public class Packets {
    public static void register() {
        Powah.NET.register(new SetChannelPacket());
    }
}
