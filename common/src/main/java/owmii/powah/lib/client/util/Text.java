package owmii.powah.lib.client.util;

import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;

public class Text {
    public static final Component EMPTY = Component.empty();
    public static final Component SPACE = Component.literal(" ");
    public static final Component COLON = Component.literal(": ");
    public static final Component COMA = Component.literal(", ");

    public static Style color(int color) {
        return Style.EMPTY.withColor(TextColor.fromRgb(color));
    }

    public static void drawString(FormattedText text, float x, float y, int w, int h, int color) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        var matrix4f = Transformation.identity().getMatrix();
        for (FormattedCharSequence processor : font.split(text, w)) {
            MultiBufferSource.BufferSource impl = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            font.drawInBatch(processor, x, y, color, false, matrix4f, impl, Font.DisplayMode.NORMAL, 0, 15728880);
            impl.endBatch();
            y += h;
        }
    }

    public static String toRange(long l) {
        long l1 = (l * 2 + 1);
        return l1 + "X" + l1;
    }

    public static String toVolume(long l) {
        long l1 = (l * 2 + 1);
        return l1 + "X" + l1 + "X" + l1;
    }
}
