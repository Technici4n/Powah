package owmii.lib.network;

import owmii.lib.network.packets.NextEnergyConfigPacket;
import owmii.lib.network.packets.NextRedstoneModePacket;
import owmii.lib.network.packets.NextTransferConfigPacket;

import static owmii.lib.Lollipop.NET;

public class Packets {
    public static void register() {
        NET.register(new NextEnergyConfigPacket());
        NET.register(new NextRedstoneModePacket());
        NET.register(new NextTransferConfigPacket());
    }
}
