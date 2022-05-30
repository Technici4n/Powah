package owmii.lib.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.screen.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Gauge extends Button {
    private final Screen screen;
    private final boolean horizontal;
    protected final ISubSized info;
    private Consumer<List<ITextComponent>> tooltipConsumer = stringList -> {
    };
    protected Texture texture;

    public Gauge(int x, int y, Texture texture, boolean horizontal, ISubSized info, Screen screen) {
        this(x, y, texture, horizontal, info, button -> {
        }, screen);
    }

    public Gauge(int x, int y, Texture texture, boolean horizontal, ISubSized info, IPressable pressable, Screen screen) {
        this(x, y, texture, horizontal, info, pressable, (b, m, i, j) -> {
        }, screen);
    }

    public Gauge(int x, int y, Texture texture, boolean horizontal, ISubSized info, IPressable pressable, ITooltip onTooltip, Screen screen) {
        super(x, y, texture.getWidth(), texture.getHeight(), new StringTextComponent(""), pressable, onTooltip);
        this.texture = texture;
        this.horizontal = horizontal;
        this.info = info;
        this.screen = screen;
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
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
    public void renderToolTip(MatrixStack matrix, int mouseX, int mouseY) {
        List<ITextComponent> tooltip = new ArrayList<>();
        this.tooltipConsumer.accept(tooltip);
        if (!tooltip.isEmpty()) {
            this.screen.func_243308_b(matrix, tooltip, mouseX, mouseY);
        }
    }

    public Consumer<List<ITextComponent>> getTooltip() {
        return this.tooltipConsumer;
    }

    public Gauge setTooltip(Consumer<List<ITextComponent>> tooltipConsumer) {
        this.tooltipConsumer = tooltipConsumer;
        return this;
    }

    @Override
    public void playDownSound(SoundHandler handler) {
    }

    @OnlyIn(Dist.CLIENT)
    public interface ISubSized {
        float get();
    }
}
