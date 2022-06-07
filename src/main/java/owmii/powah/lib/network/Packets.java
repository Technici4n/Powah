package owmii.powah.lib.network;

import owmii.powah.lib.network.packets.NextEnergyConfigPacket;
import owmii.powah.lib.network.packets.NextRedstoneModePacket;

import static owmii.powah.lib.Lollipop.NET;

public class Packets {
    public static void register() {
        NET.register(new NextEnergyConfigPacket());
        NET.register(new NextRedstoneModePacket());
    }
}
