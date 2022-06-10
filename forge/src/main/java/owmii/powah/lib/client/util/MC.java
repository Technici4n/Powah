package owmii.powah.lib.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class MC {
    public static long ticks;

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ticks++;
        }
    }

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
}
