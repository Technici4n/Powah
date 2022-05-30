package owmii.lib.client.wiki.page.panel;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import owmii.lib.client.util.Text;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;

public class InfoBox {
    private final LinkedHashMap<TextComponent, TextComponent> lines = new LinkedHashMap<>();
    public static final InfoBox EMPTY = new InfoBox();
    @Nullable
    private Style titleStyle;
    @Nullable
    private Style valueStyle;

    public InfoBox() {
    }

    public InfoBox(TextFormatting titleStyle, TextFormatting valueStyle) {
        this(Style.EMPTY.applyFormatting(titleStyle), Style.EMPTY.applyFormatting(valueStyle));
    }

    public InfoBox(int titleColor, int valueColor) {
        this(Text.color(titleColor), Text.color(valueColor));
    }

    public InfoBox(@Nullable Style titleStyle, @Nullable Style valueStyle) {
        this.titleStyle = titleStyle;
        this.valueStyle = valueStyle;
    }

    public void set(TextComponent title, TextComponent value) {
        this.lines.put(title, value);
    }

    @Nullable
    private Style getTitleStyle() {
        return this.titleStyle;
    }

    public InfoBox setTitleStyle(@Nullable Style titleStyle) {
        this.titleStyle = titleStyle;
        return this;
    }

    @Nullable
    private Style getValueStyle() {
        return this.valueStyle;
    }

    public InfoBox setValueStyle(@Nullable Style valueStyle) {
        this.valueStyle = valueStyle;
        return this;
    }

    public LinkedHashMap<TextComponent, TextComponent> getLines() {
        return this.lines;
    }

    public interface IInfoBoxHolder {
        InfoBox getInfoBox(ItemStack stack, InfoBox box);
    }
}
