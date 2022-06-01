package owmii.lib.util;

import java.util.concurrent.TimeUnit;
import net.minecraft.world.level.Level;

public class Time {
    public static String secToDHMS(long time) {
        int days = (int) TimeUnit.SECONDS.toDays(time);
        long hours = TimeUnit.SECONDS.toHours(time) - (days * 24);
        long minutes = TimeUnit.SECONDS.toMinutes(time) - (TimeUnit.SECONDS.toHours(time) * 60);
        long seconds = TimeUnit.SECONDS.toSeconds(time) - (TimeUnit.SECONDS.toMinutes(time) * 60);
        return String.format("%03d:%02d:%02d:%02d", days, hours, minutes, seconds);
    }

    public static boolean isDay(Level world) {
        float celestialAngle = world.getSunAngle(1.0F);
        return celestialAngle < 0.25F || celestialAngle > 0.75F;
    }
}
