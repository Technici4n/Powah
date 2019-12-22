package owmii.powah.energy;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public enum RedstoneMode {
    IGNORE(75, TextFormatting.DARK_GRAY),
    ON(90, TextFormatting.RED),
    OFF(105, TextFormatting.DARK_RED);
    private final int iconXUV;
    private final TextFormatting textFormatting;

    RedstoneMode(int iconXUV, TextFormatting textFormatting) {
        this.iconXUV = iconXUV;
        this.textFormatting = textFormatting;
    }

    public int getIconXUV() {
        return this.iconXUV;
    }

    public String getDisplayName() {
        return TextFormatting.GRAY + I18n.format("info.powah.redstone.signal." + name().toLowerCase(), this.textFormatting);
    }
}
