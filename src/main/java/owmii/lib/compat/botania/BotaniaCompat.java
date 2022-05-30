package owmii.lib.compat.botania;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.ModList;
import vazkii.botania.api.BotaniaAPI;

public class BotaniaCompat {
    public static final String ID = "botania";
    private static final boolean loaded = ModList.get().isLoaded(ID);

    public static boolean isLoaded() {
        return loaded;
    }

    public static boolean preventCollect(Entity entity) {
        return isLoaded() && BotaniaAPI.instance().hasSolegnoliaAround(entity);
    }
}
