package owmii.powah.lib.client.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ColorUtil {
    public static int blend(int color1, int color2, float ratio) {
        if (ratio > 1.0F) ratio = 1.0F;
        else if (ratio < 0.0F) ratio = 0.0F;
        float iRatio = 1.0F - ratio;
        int a1 = color1 >> 24 & 0xff;
        int r1 = (color1 & 0xff0000) >> 16;
        int g1 = (color1 & 0xff00) >> 8;
        int b1 = color1 & 0xff;
        int a2 = color2 >> 24 & 0xff;
        int r2 = (color2 & 0xff0000) >> 16;
        int g2 = (color2 & 0xff00) >> 8;
        int b2 = color2 & 0xff;
        int a = (int) ((a1 * iRatio) + (a2 * ratio));
        int r = (int) ((r1 * iRatio) + (r2 * ratio));
        int g = (int) ((g1 * iRatio) + (g2 * ratio));
        int b = (int) ((b1 * iRatio) + (b2 * ratio));
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int random() {
        Random rand = new Random();
        float r = rand.nextFloat() / 2.0F + 0.5F;
        float g = rand.nextFloat() / 2.0F + 0.5F;
        float b = rand.nextFloat() / 2.0F + 0.5F;
        return new Color(r, b, g).getRGB();
    }
}
