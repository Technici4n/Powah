package owmii.lib.api;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public interface IClient {
    /**
     * Setting up the client in parallel.
     * Only for code that is safe to run in parallel.
     * e.g. uses a {@link ConcurrentHashMap}.
     *
     * @see IClient#syncClient(FMLClientSetupEvent)
     */
    void client(FMLClientSetupEvent event);

    /**
     * Setting up the client on the main thread.
     * For code that is unsafe to run in parallel.
     * e.g. uses a {@link HashMap}.
     */
    default void syncClient(FMLClientSetupEvent event) {
    }
}
