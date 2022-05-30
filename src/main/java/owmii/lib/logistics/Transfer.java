package owmii.lib.logistics;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import owmii.lib.client.util.Text;

import java.util.Locale;

public enum Transfer {
    ALL(true, true, TextFormatting.DARK_GRAY),
    EXTRACT(true, false, TextFormatting.DARK_GRAY),
    RECEIVE(false, true, TextFormatting.DARK_GRAY),
    NONE(false, false, TextFormatting.DARK_RED);

    public final boolean canExtract;
    public final boolean canReceive;
    private final TextFormatting color;

    Transfer(boolean canExtract, boolean canReceive, TextFormatting color) {
        this.canExtract = canExtract;
        this.canReceive = canReceive;
        this.color = color;
    }

    public Transfer next(Transfer type) {
        if (ALL.equals(type)) {
            int i = ordinal();
            if (i < 3) i++;
            else i = 0;
            return values()[i];
        } else if (EXTRACT.equals(type)) {
            return !NONE.equals(this) ? NONE : EXTRACT;
        } else if (RECEIVE.equals(type)) {
            return !NONE.equals(this) ? NONE : RECEIVE;
        }
        return NONE;
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("info.lollipop.io.mode").append(Text.COLON).mergeStyle(TextFormatting.GRAY)
                .append(new TranslationTextComponent("info.lollipop.io.mode." + name().toLowerCase()).mergeStyle(this.color));
    }

    public ITextComponent getDisplayName2() {
        return new TranslationTextComponent("info.lollipop.io.mode").append(Text.COLON).mergeStyle(TextFormatting.GRAY)
                .append(new TranslationTextComponent("info.lollipop.io.mode." + translate(name().toLowerCase(Locale.ENGLISH))).mergeStyle(this.color));
    }

    private String translate(String s) {
        return s.equals("extract") ? "push" : s.equals("receive") ? "pull" : s;
    }
}
