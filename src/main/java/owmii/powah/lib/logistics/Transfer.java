package owmii.powah.lib.logistics;

import owmii.powah.lib.client.util.Text;

import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public enum Transfer {
    ALL(true, true, ChatFormatting.DARK_GRAY),
    EXTRACT(true, false, ChatFormatting.DARK_GRAY),
    RECEIVE(false, true, ChatFormatting.DARK_GRAY),
    NONE(false, false, ChatFormatting.DARK_RED);

    public final boolean canExtract;
    public final boolean canReceive;
    private final ChatFormatting color;

    Transfer(boolean canExtract, boolean canReceive, ChatFormatting color) {
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

    public Component getDisplayName() {
        return new TranslatableComponent("info.lollipop.io.mode").append(Text.COLON).withStyle(ChatFormatting.GRAY)
                .append(new TranslatableComponent("info.lollipop.io.mode." + name().toLowerCase()).withStyle(this.color));
    }

    public Component getDisplayName2() {
        return new TranslatableComponent("info.lollipop.io.mode").append(Text.COLON).withStyle(ChatFormatting.GRAY)
                .append(new TranslatableComponent("info.lollipop.io.mode." + translate(name().toLowerCase(Locale.ENGLISH))).withStyle(this.color));
    }

    private String translate(String s) {
        return s.equals("extract") ? "push" : s.equals("receive") ? "pull" : s;
    }
}
