package owmii.powah.lib.client.wiki.page.panel;

import java.util.LinkedHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import owmii.powah.lib.client.util.Text;

public class InfoBox {
    private final LinkedHashMap<MutableComponent, MutableComponent> lines = new LinkedHashMap<>();
    public static final InfoBox EMPTY = new InfoBox();
    @Nullable
    private Style titleStyle;
    @Nullable
    private Style valueStyle;

    public InfoBox() {
    }

    public InfoBox(ChatFormatting titleStyle, ChatFormatting valueStyle) {
        this(Style.EMPTY.applyFormat(titleStyle), Style.EMPTY.applyFormat(valueStyle));
    }

    public InfoBox(int titleColor, int valueColor) {
        this(Text.color(titleColor), Text.color(valueColor));
    }

    public InfoBox(@Nullable Style titleStyle, @Nullable Style valueStyle) {
        this.titleStyle = titleStyle;
        this.valueStyle = valueStyle;
    }

    public void set(MutableComponent title, MutableComponent value) {
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

    public LinkedHashMap<MutableComponent, MutableComponent> getLines() {
        return this.lines;
    }

    public interface IInfoBoxHolder {
        InfoBox getInfoBox(ItemStack stack, InfoBox box);
    }
}
