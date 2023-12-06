package owmii.powah.lib.client.util;

import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.TickEvent;

public class MC {
    public static long ticks;

    public static Optional<Player> player() {
        return Optional.ofNullable(get().player);
    }

    public static Optional<Level> world() {
        return Optional.ofNullable(get().level);
    }

    public static void open(Screen screen) {
        get().setScreen(screen);
    }

    public static Minecraft get() {
        return Minecraft.getInstance();
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            ticks++;
        }
    }
}
