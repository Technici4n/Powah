package owmii.powah.network;

import owmii.powah.network.packet.SSetActiveChannel;
import owmii.powah.network.packet.SSetActiveChannelItem;

import static owmii.powah.Powah.NET;

public class Packets {
    public static void register() {
        NET.register(new SSetActiveChannel());
        NET.register(new SSetActiveChannelItem());
    }
}
