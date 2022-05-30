package owmii.lib.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenBase extends Screen {
    public final Minecraft mc = Minecraft.getInstance();
    public int x, y, w, h;

    protected ScreenBase(ITextComponent title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - this.w) / 2;
        this.y = (this.height - this.h) / 2;
    }

    @Override
    public void render(MatrixStack matrix, int mx, int my, float pt) {
        super.render(matrix, mx, my, pt);
        for (Widget widget : this.buttons) {
            if (widget.isHovered()) {
                widget.renderToolTip(matrix, mx, my);
                return;
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            InputMappings.Input code = InputMappings.getInputByCode(keyCode, scanCode);
            if (keyCode == 256 || Minecraft.getInstance().gameSettings.keyBindInventory.isActiveAndMatches(code)) {
                closeScreen();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public boolean isMouseOver(int x, int y, int w, int h, double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h;
    }

    public <T extends Widget> T addButton2(T button) {
        return addButton(button);
    }
}
