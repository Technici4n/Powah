package owmii.lib.logistics;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import owmii.lib.client.util.Text;

public enum Redstone {
    IGNORE(TextFormatting.DARK_GRAY),
    ON(TextFormatting.RED),
    OFF(TextFormatting.DARK_RED);

    private final TextFormatting color;

    Redstone(TextFormatting color) {
        this.color = color;
    }

    public Redstone next() {
        int i = ordinal() + 1;
        return values()[i > 2 ? 0 : i];
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("info.lollipop.redstone").append(Text.COLON).mergeStyle(TextFormatting.GRAY)
                .append(new TranslationTextComponent("info.lollipop." + name().toLowerCase()).mergeStyle(this.color));
    }
}
