package owmii.powah.energy;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public enum PowerMode {
    ALL(15, true, true, TextFormatting.DARK_GRAY),
    OUT(30, true, false, TextFormatting.DARK_GRAY),
    IN(45, false, true, TextFormatting.DARK_GRAY),
    NON(60, false, false, TextFormatting.DARK_RED);

    private final TextFormatting textFormatting;
    private final boolean out;
    private final boolean in;
    private final int iconXUV;

    PowerMode(int iconXUV, boolean out, boolean in, TextFormatting textFormatting) {
        this.out = out;
        this.in = in;
        this.iconXUV = iconXUV;
        this.textFormatting = textFormatting;
    }

    public boolean isOut() {
        return this.out;
    }

    public boolean isIn() {
        return this.in;
    }

    public int getIconXUV() {
        return this.iconXUV;
    }

    public String getDisplayName() {
        return TextFormatting.GRAY + I18n.format("info.powah.mode." + name().toLowerCase(), this.textFormatting);
    }
}
