package owmii.powah.book.content.page;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.util.FML;

public class Paragraph {
    private String title;
    private String text;
    private Object[] args;

    Paragraph(String title, String text, Object... args) {
        this.title = title;
        this.text = text;
        this.args = args;
    }

    public static Paragraph title(String title) {
        return create(title, "");
    }

    public static Paragraph text(String text, Object... args) {
        return create("", text, args);
    }

    public static Paragraph create(String title, String text, Object... args) {
        String s = "book." + FML.getActiveID();
        return new Paragraph(title.isEmpty() ? "" : s + ".title." + title, text.isEmpty() ? "" : s + ".text." + text, args);
    }

    @OnlyIn(Dist.CLIENT)
    private int height = -1;

    @OnlyIn(Dist.CLIENT)
    public int height(int width) {
        // if (this.height == -1) {
        FontRenderer fr = Minecraft.getInstance().fontRenderer;
        int i = fr.listFormattedStringToWidth(I18n.format(this.text), width).size();
        this.height = this.text.isEmpty() ? 0 : 10 * i;
        // }
        return this.height;
    }

    public String getTitle() {
        return this.title;
    }

    public String getText() {
        return this.text;
    }

    public Object[] getArgs() {
        return this.args;
    }
}
