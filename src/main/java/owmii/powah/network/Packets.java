package owmii.powah.network;

import owmii.powah.Powah;
import owmii.powah.network.packet.SetChannelPacket;
import owmii.powah.network.packet.SwitchGenModePacket;

public class Packets {
    public static void register() {
        Powah.NET.register(new SetChannelPacket());
        Powah.NET.register(new SwitchGenModePacket());
    }
}
