package owmii.powah.lib.client.screen.widget;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.powah.lib.client.screen.Texture;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Gauge extends Button {
    private final Screen screen;
    private final boolean horizontal;
    protected final ISubSized info;
    private Consumer<List<Component>> tooltipConsumer = stringList -> {
    };
    protected Texture texture;

    public Gauge(int x, int y, Texture texture, boolean horizontal, ISubSized info, Screen screen) {
        this(x, y, texture, horizontal, info, button -> {
        }, screen);
    }

    public Gauge(int x, int y, Texture texture, boolean horizontal, ISubSized info, OnPress pressable, Screen screen) {
        this(x, y, texture, horizontal, info, pressable, (b, m, i, j) -> {
        }, screen);
    }

    public Gauge(int x, int y, Texture texture, boolean horizontal, ISubSized info, OnPress pressable, OnTooltip onTooltip, Screen screen) {
        super(x, y, texture.getWidth(), texture.getHeight(), new TextComponent(""), pressable, onTooltip);
        this.texture = texture;
        this.horizontal = horizontal;
        this.info = info;
        this.screen = screen;
    }

    @Override
    public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (this.horizontal) {
                float f = this.info.get() / this.width;
                this.texture.drawScalableW(matrix, this.info.get() + f, this.x, this.y);
            } else {
                float f = this.info.get() / this.height;
                this.texture.drawScalableH(matrix, this.info.get() + f, this.x, this.y);
            }
        }
    }

    @Override
    public void renderToolTip(PoseStack matrix, int mouseX, int mouseY) {
        List<Component> tooltip = new ArrayList<>();
        this.tooltipConsumer.accept(tooltip);
        if (!tooltip.isEmpty()) {
            this.screen.renderComponentTooltip(matrix, tooltip, mouseX, mouseY);
        }
    }

    public Consumer<List<Component>> getTooltip() {
        return this.tooltipConsumer;
    }

    public Gauge setTooltip(Consumer<List<Component>> tooltipConsumer) {
        this.tooltipConsumer = tooltipConsumer;
        return this;
    }

    @Override
    public void playDownSound(SoundManager handler) {
    }

    @OnlyIn(Dist.CLIENT)
    public interface ISubSized {
        float get();
    }
}
