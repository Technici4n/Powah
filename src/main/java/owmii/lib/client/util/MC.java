package owmii.lib.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
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

    public static Optional<PlayerEntity> player() {
        return Optional.ofNullable(get().player);
    }

    public static Optional<World> world() {
        return Optional.ofNullable(get().world);
    }

    public static void open(Screen screen) {
        get().displayGuiScreen(screen);
    }

    public static Minecraft get() {
        return Minecraft.getInstance();
    }
}
