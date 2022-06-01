package owmii.lib.logistics;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import owmii.lib.client.util.Text;

public enum Redstone {
    IGNORE(ChatFormatting.DARK_GRAY),
    ON(ChatFormatting.RED),
    OFF(ChatFormatting.DARK_RED);

    private final ChatFormatting color;

    Redstone(ChatFormatting color) {
        this.color = color;
    }

    public Redstone next() {
        int i = ordinal() + 1;
        return values()[i > 2 ? 0 : i];
    }

    public Component getDisplayName() {
        return new TranslatableComponent("info.lollipop.redstone").append(Text.COLON).withStyle(ChatFormatting.GRAY)
                .append(new TranslatableComponent("info.lollipop." + name().toLowerCase()).withStyle(this.color));
    }
}
