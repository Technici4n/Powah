package owmii.powah.lib.compat.top;

import dev.architectury.platform.Platform;

public class TOPCompat {
    public static final String ID = "theoneprobe";
    private static int loaded;

    public static void register() {
        if (isLoaded()) {
            //  InterModComms.sendTo(ID, "getTheOneProbe", TOPProvider::new);
        }
    }

    public static boolean isLoaded() {
        if (loaded == 0) loaded = Platform.isModLoaded(ID) ? 1 : -1;
        return loaded == 1;
    }
}
