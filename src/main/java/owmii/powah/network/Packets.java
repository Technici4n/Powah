package owmii.powah.network;

import owmii.lib.Lollipop;
import owmii.powah.network.packet.NextPowerMode;
import owmii.powah.network.packet.NextRedstoneMode;
import owmii.powah.network.packet.SetActiveChannel;
import owmii.powah.network.packet.SetActiveChannelItem;

public class Packets {
    public static void register() {
        Lollipop.NET.register(new NextPowerMode());
        Lollipop.NET.register(new NextRedstoneMode());
        Lollipop.NET.register(new SetActiveChannel());
        Lollipop.NET.register(new SetActiveChannelItem());
    }
}
