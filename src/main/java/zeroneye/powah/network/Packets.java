package zeroneye.powah.network;

import zeroneye.lib.Lollipop;
import zeroneye.powah.network.packet.NextPowerMode;
import zeroneye.powah.network.packet.NextRedstoneMode;

public class Packets {
    public static void register() {
        Lollipop.NET.register(NextPowerMode.class, NextPowerMode::encode, NextPowerMode::decode, NextPowerMode::handle);
        Lollipop.NET.register(NextRedstoneMode.class, NextRedstoneMode::encode, NextRedstoneMode::decode, NextRedstoneMode::handle);
    }
}
