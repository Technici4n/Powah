package owmii.powah.lib.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ScreenBase extends Screen {
    public final Minecraft mc = Minecraft.getInstance();
    public int x, y, w, h;

    protected ScreenBase(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - this.w) / 2;
        this.y = (this.height - this.h) / 2;
    }

    @Override
    public void render(PoseStack matrix, int mx, int my, float pt) {
        super.render(matrix, mx, my, pt);
        for (var widget : renderables) {
            if (getFocused() != null && getFocused() != widget) {
                continue;
            }
            if (widget instanceof AbstractWidget aw && aw.isHoveredOrFocused()) {
                aw.renderToolTip(matrix, mx, my);
            }
        }
    }

    /*
     * TODO ARCH - unclear
     * 
     * @Override
     * public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
     * if (super.keyPressed(keyCode, scanCode, modifiers)) {
     * return true;
     * } else {
     * InputConstants.Key code = InputConstants.getKey(keyCode, scanCode);
     * if (keyCode == 256 || Minecraft.getInstance().options.keyInventory.isActiveAndMatches(code)) {
     * onClose();
     * return true;
     * }
     * }
     * return false;
     * }
     */

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public boolean isMouseOver(int x, int y, int w, int h, double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h;
    }

    public <T extends AbstractWidget> T addButton2(T button) {
        return addRenderableWidget(button);
    }
}
