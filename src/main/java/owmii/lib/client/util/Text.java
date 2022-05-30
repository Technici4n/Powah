package owmii.lib.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Text {
    public static final StringTextComponent EMPTY = new StringTextComponent("");
    public static final StringTextComponent SPACE = new StringTextComponent(" ");
    public static final StringTextComponent COLON = new StringTextComponent(": ");
    public static final StringTextComponent COMA = new StringTextComponent(", ");

    public static Style color(int color) {
        return Style.EMPTY.setColor(Color.fromInt(color));
    }

    public static void drawString(ITextProperties text, float x, float y, int w, int h, int color) {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer font = mc.fontRenderer;
        Matrix4f matrix4f = TransformationMatrix.identity().getMatrix();
        for (IReorderingProcessor processor : font.trimStringToWidth(text, w)) {
            IRenderTypeBuffer.Impl impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
            font.func_238416_a_(processor, x, y, color, false, matrix4f, impl, false, 0, 15728880);
            impl.finish();
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
