package owmii.lib.util;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class FML {
    public static boolean isLoaded(String id) {
        return ModList.get().isLoaded(id);
    }

    public static String getActiveID() {
        return ModLoadingContext.get().getActiveNamespace();
    }

    public static boolean isClient() {
        return FMLEnvironment.dist.isClient();
    }
}
