package owmii.powah.network;

import owmii.lib.Lollipop;
import owmii.powah.network.packet.SSetActiveChannel;
import owmii.powah.network.packet.SSetActiveChannelItem;

public class Packets {
    public static void register() {
        Lollipop.NET.register(new SSetActiveChannel());
        Lollipop.NET.register(new SSetActiveChannelItem());
    }
}
