package owmii.powah.network;

import owmii.lib.Lollipop;
import owmii.powah.network.packet.NextPowerMode;
import owmii.powah.network.packet.NextRedstoneMode;
import owmii.powah.network.packet.SetActiveChannel;

public class Packets {
    public static void register() {
        Lollipop.NET.register(NextPowerMode.class, NextPowerMode::encode, NextPowerMode::decode, NextPowerMode::handle);
        Lollipop.NET.register(NextRedstoneMode.class, NextRedstoneMode::encode, NextRedstoneMode::decode, NextRedstoneMode::handle);
        Lollipop.NET.register(SetActiveChannel.class, SetActiveChannel::encode, SetActiveChannel::decode, SetActiveChannel::handle);

        // Client
    }
}
