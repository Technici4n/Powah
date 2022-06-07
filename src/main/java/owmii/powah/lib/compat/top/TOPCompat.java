package owmii.powah.lib.compat.top;

import net.minecraftforge.fml.ModList;

public class TOPCompat {
    public static final String ID = "theoneprobe";
    private static int loaded;

    public static void register() {
        if (isLoaded()) {
            //  InterModComms.sendTo(ID, "getTheOneProbe", TOPProvider::new);
        }
    }

    public static boolean isLoaded() {
        if (loaded == 0) loaded = ModList.get().isLoaded(ID) ? 1 : -1;
        return loaded == 1;
    }
}
